(defproject stocks "0.1.0-SNAPSHOT"
  :description "Stock market data"
  :url "http://github.com/owainlewis"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.9.5"]]
  :profiles {:uberjar {:aot :all}}
  :min-lein-version "2.0.0"
  :uberjar-name "stocks.jar"
  :ring {:handler stocks.server/app}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.5.0"]
                 [clj-time "0.11.0"]
                 [compojure "1.3.4"]
                 [cheshire "5.5.0"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [clj-http "2.0.0"]])
