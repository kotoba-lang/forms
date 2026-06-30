(ns forms.validate
  (:require [forms.model :as model]))

(defn problem [severity code id msg]
  {:forms/severity severity :forms/code code :forms/id id :forms/msg msg})

(defn form-problems [f]
  (vec
   (concat
    (for [fld (:forms/fields f)
          :when (not (contains? model/field-types (:forms/field-type fld)))]
      (problem :error :field/unknown-type (:forms/id fld) "unknown field type"))
    (for [[id n] (frequencies (map :forms/id (:forms/fields f)))
          :when (> n 1)]
      (problem :error :field/duplicate-id id "duplicate field id")))))

(defn submission-problems [f sub]
  (vec
   (concat
    (for [id (model/missing-required f sub)]
      (problem :error :submission/missing-required id "required answer is missing"))
    (for [fld (:forms/fields f)
          :when (= :email (:forms/field-type fld))
          :let [answer (get-in sub [:forms/answers (:forms/id fld)])]
          :when (and answer (not (model/valid-email? answer)))]
      (problem :error :submission/invalid-email (:forms/id fld) "invalid email answer")))))

(defn valid-form? [f]
  (not-any? #(= :error (:forms/severity %)) (form-problems f)))

(defn valid-submission? [f sub]
  (not-any? #(= :error (:forms/severity %)) (submission-problems f sub)))
