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
  (GET "/stocks/:symbol" [symbol]
    (json-handler
      (s/get-quote symbol)))

  (GET "/stocks/:symbol/1M" [symbol]
    (json-handler
      (s/get-performance symbol 1)))

  (GET "/stocks/:symbol/3M" [symbol]
    (json-handler
      (s/get-performance symbol 3)))

  (GET "/stocks/:symbol/6M" [symbol]
    (json-handler
      (s/get-performance symbol 6)))

  (GET "/stocks/:symbol/12M" [symbol]
    (json-handler
      (s/get-performance symbol 12)))
)

(def app
  (-> api-routes))
