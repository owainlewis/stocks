(ns stocks.util
  (:require [clojure.java.io :as io]))

(def exchanges "data/exchanges.txt")

(defn read-lines [path]
  (with-open [rdr (io/reader path)]
    (into [] (line-seq rdr))))

(defn split-comma [s]
  (clojure.string/split s #","))

(defn load-csv [path]
  (when-let [lines (read-lines path)]
    (mapv #(clojure.string/split % #",") lines)))
