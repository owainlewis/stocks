(ns stocks.server
  (:require [compojure.core :refer :all]
            [stocks.core :as s]
            [cheshire.core :as json]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defn json-handler [body]
  {:status 200
   :headers {
     "Content-Type" "application/json"
     "Access-Control-Allow-Methods" "GET"
     "Access-Control-Allow-Origin" "*" }
   :body (json/generate-string body {:pretty true})})

(defroutes api-routes
  (GET "/" []
    (json-handler
      (s/stock-performance "AAPL" 1))))

(def app
  (-> api-routes))
