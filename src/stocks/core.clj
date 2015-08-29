;; ************************************************************
;;
;; Stocks is a library for stock market analysis
;;
;; It includes functionality to discover important metrics about a given stock such as
;; daily returns, sharpe ratio etc
;;
;; ************************************************************

(ns stocks.core
  (:require
    [clj-http.client :as client]
    [clj-time.format :as f]
    [clj-time.core :as t]))

;; Grunt work

(def yahoo "https://query.yahooapis.com/v1/public/yql")
(def table "store://datatables.org/alltableswithkeys")

(defn stock-quote [stock]
  (format "select * from yahoo.finance.quote where symbol=\"%s\"" stock))

(defn historical-quote
  "Generates a historical YQL query"
  [stock start end]
  (let [parts ["select * from yahoo.finance.historicaldata where "
               "symbol=\"%s\" and startDate=\"%s\" and endDate=\"%s\""]]
  (format (apply str parts)
    stock start end)))

(def fmt
  (partial f/unparse
    (f/formatters :date)))

(defn past-n-months [n]
  (let [now (t/now)
        then (t/minus now (t/months n))]
  (mapv fmt [then now])))

(defn past-n-months-for-stock
  "Return a YQL query that will fetch the last n months of data
   for a given stock"
  [stock n]
  (let [f (partial historical-quote stock)
        duration (past-n-months 12)]
    (apply f duration)))

;; Query


;;{:Symbol "GOOG", :Date "2015-08-28", :Open "632.820007", :High "636.880005", :Low "624.559998", :Close "630.380005", :Volume "1973500", :Adj_Close "630.380005"}

(defn normalize-key [k]
  (->> k (name) (.toLowerCase) (keyword)))

(defn normalize-type [m k]
  (when-let [v (get m k)]
    (assoc m k (read-string v))))

(defn normalize-kv [m ks]
  (into {}
    (for [[k v] m]
      (if (contains? (set ks) k)
        {k (read-string v)}
        {k v}))))

(defn normalize-stock-result
  "Normalize Yahoo results to make them more aesthetic in key structure"
  [result]
  (let [partial-update
         (into {}
           (for [[k v] result]
             (hash-map (normalize-key k) v)))]
;; TODO use a fold or something here. Need to update  bunch of values to be double
   (normalize-kv partial-update [:open :high :low :close :volume :adj_close])))

(defn >>
  [query]
  (let [query-params {:q query, :format "json", :env table }
        response (client/get yahoo
                   {:query-params query-params :as :json})]
    (->> response :body :query :results :quote (map normalize-stock-result))))

;; Analysis

;; 1. Daily returns for a stock

(defn percentage-return [value-then value-now]
  (let [v (- (/ value-now value-then) 1)]
    (double (* v 100))))

(defn return-seq [xs]
  (for [[before after] (map vector (cons 0 xs) xs)]
    (percentage-return before after)))

;; Daily return = (day / day before) - 1

(defn daily-returns-for-last-n-months [stock n]
  (let [stock-results (>> (past-n-months-for-stock stock n))
        values (map :adj_close stock-results)]
    (return-seq values)))
