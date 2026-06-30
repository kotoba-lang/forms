(ns forms.model
  (:require [clojure.string :as str]))

(def field-types #{:text :textarea :email :number :date :choice :checkbox})

(defn form
  ([id] (form id {}))
  ([id attrs]
   (merge {:forms/id id
           :forms/type :form
           :forms/title id
           :forms/fields []}
          attrs)))

(defn field [id type attrs]
  (merge {:forms/id id
          :forms/field-type type
          :forms/label id
          :forms/required? false}
         attrs))

(defn add-field [f fld]
  (update f :forms/fields conj fld))

(defn submission [form-id answers]
  {:forms/form-id form-id
   :forms/answers answers})

(defn missing-required [f sub]
  (->> (:forms/fields f)
       (filter :forms/required?)
       (remove #(contains? (:forms/answers sub) (:forms/id %)))
       (mapv :forms/id)))

(defn valid-email? [s]
  (boolean (re-matches #"[^@\s]+@[^@\s]+\.[^@\s]+" (str s))))

(defn seed-form []
  (-> (form "contact" {:forms/title "Contact"})
      (add-field (field "name" :text {:forms/required? true}))
      (add-field (field "email" :email {:forms/required? true}))))
