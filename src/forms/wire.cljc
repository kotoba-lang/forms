(ns forms.wire
  "Transit wire helpers for Kotoba Forms forms.

  ## The payload comes back as plain JSON

  `sheets.wire` and `docs.wire` are the shape this follows, and both of them
  have a round-trip test asserting `(= original (read (:body envelope)))`.
  Those tests pass because sheets and docs pin `transit` at 77e3ce7d, which
  is the last commit before the wire switched from Transit-tagged JSON to
  plain JSON (0d45c30). This library pins the current `transit`, because
  `:forms/form` is only an admitted resource kind there — so the payload it
  reads back has string keys and vectors, not the original EDN.

  That is the documented contract (`transit.core/read-office-envelope-body`:
  \"callers that need EDN back on the payload convert it themselves\"), not a
  defect here. It is written down because the two sibling libraries currently
  demonstrate the opposite, and the difference is a pin rather than a
  decision anyone made about forms."
  (:require [transit.core :as transit]))

(defn form-envelope
  ([form] (form-envelope form {}))
  ([form opts]
   (transit/office-envelope :forms/form form opts)))

(defn read-form-envelope [body]
  (let [envelope (transit/read-office-envelope-body body)]
    (when-not (= :forms/form (:kotoba.resource/kind envelope))
      (throw (ex-info "not a Forms form Transit envelope"
                      {:kind (:kotoba.resource/kind envelope)})))
    (:kotoba.resource/payload envelope)))
