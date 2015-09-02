(ns stocks.util-test
  (:require [clojure.test :refer :all]
            [stocks.util :refer :all]))

(deftest camel-to-snake-test
  (testing "should convert a CamelCase string to snake_case"
    (is (= (camel-to-snake "FooBar" "foo_bar")))))
