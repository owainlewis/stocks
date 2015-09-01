(ns stocks.core-test
  (:require [clojure.test :refer :all]
            [stocks.core :refer :all]))

(deftest normalize-kv-test
  (testing "should normalize a map"
    (let [before {:a "1.2" :b "foo"}
          after (normalize-kv before [:a])]
    (is (= {:a 1.2 :b "foo"} after)))))
