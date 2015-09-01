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
             (hash-map (normalize-key k) v)))
         result (normalize-kv partial-update
                  [:open :high :low :close :volume :adj_close])]
  (dissoc result :symbol)))

(defn >>
  [query]
  (let [query-params {:q query, :format "json", :env table }
        response (client/get yahoo
                   {:query-params query-params :as :json})
        data (->> response :body :query :results :quote)]
      (mapv normalize-stock-result data)))

;; Public query methods
;; *********************************************************

(def ranges
  {:1D ""})

(defn stock-performance [symbol number-of-months]
  (>> (past-n-months-for-stock symbol number-of-months)))
