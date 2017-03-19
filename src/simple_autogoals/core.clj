(ns simple-autogoals.core
  (:require [clj-http.client :as client]
            [clj-http.cookies :as cookies]
            [cheshire.core :as json]
            [clojure.walk :refer [keywordize-keys]])
  (:gen-class))

(def ^:const ^:private auto-amount-pattern #"auto_transfer=([0-9|.]*)")

(defn- sign-in [username password cookie-store]
  (println "Signing-in" username)
  (let [csrf (->> (client/get "https://bank.simple.com/signin"
                              {:cookie-store cookie-store})
                  :body
                  (re-find #"<meta name=\"_csrf\" content=\"(.*)\">")
                  last)]
    (try
      (if (-> (client/post "https://bank.simple.com/signin"
                           {:form-params {:username username
                                          :password password
                                          :_csrf csrf}
                            :cookie-store cookie-store})
              :body
              (.contains "Your username and passphrase don't match"))
        (throw (Exception. "Invalid username and/or passphrase"))
        csrf)
      (catch Exception e
        (println "Could not sign-in" e)
        (throw e)))))

(defn- transfer-to-goal
  [cookie-store csrf {:keys [name id dollar-amount-to-transfer]}]
  (println (format "Transferring $%.2f to \"%s\""
                   dollar-amount-to-transfer
                   name))
  (client/post (format "https://bank.simple.com/goals/%s/transactions"
                       id)
               {:form-params {:amount (int (* dollar-amount-to-transfer 10000))
                              :_csrf csrf}
                :cookie-store cookie-store}))

(defn- should-transfer? [goal]
  (and (not (:archived goal))
       (not (:paused goal))
       (:description goal)
       (re-find auto-amount-pattern (:description goal))))

(defn- assoc-amount-to-transfer [goal]
  (assoc goal
         :dollar-amount-to-transfer
         (-> (re-find auto-amount-pattern (:description goal))
             last
             read-string
             (* 1.0))))

(defn- get-goals-to-transfer [cookie-store]
  (println "Getting goals")
  (->> (client/get "https://bank.simple.com/goals/data"
                   {:cookie-store cookie-store})
       :body
       json/parse-string
       (map keywordize-keys)
       (filter should-transfer?)
       (map assoc-amount-to-transfer)))

(defn -main []
  (let [username (System/getenv "SIMPLE_USERNAME")
        password (System/getenv "SIMPLE_PASSWORD")
        cookie-store (cookies/cookie-store)
        csrf (sign-in username password cookie-store)
        goals (get-goals-to-transfer cookie-store)]
    (println (count goals) "goals to update")
    (doall (map (partial transfer-to-goal cookie-store csrf) goals))
    (println "Done")))
