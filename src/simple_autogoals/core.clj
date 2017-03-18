(ns simple-autogoals.core
  (:require [clj-http.client :as client]
            [clj-http.cookies :as cookies])
  (:gen-class))

(defn- sign-in [username password cookie-store]
  (println "Attempting to sign-in user:" username)
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
        (do (println "Successfully signed in")
            csrf))
      (catch Exception e
        (println "Could not sign-in" e)
        (throw e)))))

(defn -main
  [& args]
  (let [cookie-store (cookies/cookie-store)
        csrf (sign-in username password cookie-store)]
    (println "Done")))
