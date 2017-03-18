(ns simple-autogoals.core
  (:require [clj-http.client :as client])
  (:gen-class))

(defn- get-csrf []
  (->> (client/get "https://bank.simple.com/signin")
       :body
       (re-find #"<meta name=\"_csrf\" content=\"(.*)\">")
       last))

(defn -main
  [& args]
  (let [csrf (get-csrf)]
    (println csrf)))
