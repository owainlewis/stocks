(ns stocks.util
  (:require [clojure.java.io :as io]))

(def exchanges "data/exchanges.txt")

(defn read-lines [path]
  (with-open [rdr (io/reader path)]
    (into [] (line-seq rdr))))

(defn load-csv [path]
  (when-let [lines (read-lines path)]
    (mapv #(clojure.string/split % #",") lines)))

(defn camel-to-snake
  "Convert a CamelCase string to snake_case"
  [input]
  (let [[head tail] ((juxt first rest) input)
        snake-cased (reduce (fn [acc char]
                              (if (Character/isUpperCase char)
                                (conj (conj acc \_)
                                  (Character/toLowerCase char))
                                (conj acc char))) [] tail)]
    (apply str
           (cons (Character/toLowerCase head)
             snake-cased))))
