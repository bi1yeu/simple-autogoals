(defproject simple-autogoals "0.1.0-SNAPSHOT"
  :description "Automatic goal management for Simple"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "2.3.0"]
                 [cheshire "5.7.0"]]
  :main ^:skip-aot simple-autogoals.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
