# stocks

A Clojure library for stock market analysis

## Usage

Fetch the last 3 months data for a stock (e.g GOOGL)

```clojure

(ns example
  (:require [stocks.core :refer :all]))

(defn last-3-months-google-stock-results
  (>> (past-n-months-for-stock "GOOGL" 3)))

;; stocks.core> (first (>> (past-n-months-for-stock "GOOGL" 3)))

;; {:Symbol "GOOGL",
;;  :Date "2015-08-26",
:;	:Open "643.099976",
;;	:High "662.47998",
;;	:Low "630.369995",
;;	:Close "659.73999",
;;	:Volume "4224400",
;;	:Adj_Close "659.73999"}

```

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
