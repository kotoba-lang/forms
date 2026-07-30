(ns forms.model-test
  (:require [clojure.test :refer [deftest is]]
            [forms.model :as f]
            [forms.validate :as v]
            [forms.wire :as wire]))

(deftest forms-model
  (let [form (f/seed-form)
        sub (f/submission "contact" {"name" "Alice" "email" "alice@example.com"})]
    (is (v/valid-form? form))
    (is (v/valid-submission? form sub))
    (is (= ["email"] (f/missing-required form (f/submission "contact" {"name" "Alice"}))))))

(deftest form-envelope-round-trip
  (let [form (f/seed-form)
        envelope (wire/form-envelope form {:request-id "req-1"})
        payload (wire/read-form-envelope (:body envelope))]
    (is (= "application/json" (:content-type envelope)))
    ;; Asserted as plain JSON rather than `(= form payload)`, which is what
    ;; the sibling libraries assert against their older transit pin. The
    ;; projection is lossy on purpose; what has to survive is the
    ;; discriminant and the values.
    (is (= "contact" (get payload "forms/id")))
    (is (= ["name" "email"] (mapv #(get % "forms/id") (get payload "forms/fields"))))
    (is (= ["text" "email"] (mapv #(get % "forms/field-type") (get payload "forms/fields"))))))

(deftest read-form-envelope-refuses-another-resource-kind
  (let [envelope (wire/form-envelope (f/seed-form))
        body (assoc (:body envelope) "kotoba.resource/kind" "docs/document")]
    (is (thrown? #?(:clj clojure.lang.ExceptionInfo
                    :cljs cljs.core.ExceptionInfo)
                 (wire/read-form-envelope body)))))
