#!/usr/bin/env nbb
;; The same test namespaces, on the other host. A `.cljc` library that only
;; ever runs on one host is a `.clj` library with extra reader conditionals —
;; `sheets` shipped two bugs that existed only under ClojureScript before
;; anything ran there.
;;
;;   nbb --classpath "src:test:$(clojure -Spath)" scripts/test-cljs.cljs

(require '[clojure.test :as t] 'forms.model-test 'forms.responses-test)

(let [{:keys [fail error]} (t/run-tests 'forms.model-test 'forms.responses-test)]
  (when (pos? (+ (or fail 0) (or error 0)))
    (js/process.exit 1)))
