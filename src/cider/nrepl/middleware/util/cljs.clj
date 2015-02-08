(ns cider.nrepl.middleware.util.cljs)

(defn try-piggieback
  []
  (try
    (require 'cemerick.piggieback)
    (resolve 'cemerick.piggieback/wrap-cljs-repl)
    (catch Exception _)))

(defn maybe-piggieback
  "Helper to modify a descriptor to support piggieback"
  [descriptor]
  (if-let [piggieback (try-piggieback)]
    (update-in descriptor [:requires] #(set (conj % piggieback)))
    descriptor))

(defn grab-cljs-env
  [msg]
  (when-let [piggieback-key (resolve 'cemerick.piggieback/*cljs-repl-env*)]
    (some-> msg
            :session
            deref
            (get piggieback-key)
            :cljs.env/compiler
            deref)))
