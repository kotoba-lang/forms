(ns forms.model-test
  (:require [clojure.test :refer [deftest is]]
            [forms.model :as f]
            [forms.validate :as v]))

(deftest forms-model
  (let [form (f/seed-form)
        sub (f/submission "contact" {"name" "Alice" "email" "alice@example.com"})]
    (is (v/valid-form? form))
    (is (v/valid-submission? form sub))
    (is (= ["email"] (f/missing-required form (f/submission "contact" {"name" "Alice"}))))))
