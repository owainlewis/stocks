(ns stocks.statistics)

;; Standard deviation

;; Sharpe ratio

(defn percentage-return
  "Get the percentage return given a value before and now"
  [value-then value-now]
  (let [v (- (/ value-now value-then) 1)]
    (double (* v 100))))

(defn daily-returns
  "Given a sequence of adjusted close values,
   calculate a sequence of values representing daily returns
   for a stock"
  [xs]
  (for [[before after] (map vector (cons 0 xs) xs)]
    (percentage-return before after)))

;; Annual return
