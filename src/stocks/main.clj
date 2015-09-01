(ns stocks.main
  (:require [stocks.server :refer :all]
            [ring.adapter.jetty :as jetty]))

(defn -main
  "Run the server"
  [& [port]]
  (let [port 5000]
    (jetty/run-jetty  app {:port port :join? false})))
