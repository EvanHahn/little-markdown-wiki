(ns little-markdown-wiki.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [little-markdown-wiki.generate :refer [generate-output]])
  (:gen-class))

(defn- err-println [& args]
  (binding [*out* *err*]
    (apply println args)))

(defn- err-exit [s]
  (err-println s)
  (System/exit 1))

(def ^:private cli-options
  [["-i" "--input-dir DIRECTORY" "Input directory"
    :parse-fn [#(io/file %)]
    :validate [(memfn isDirectory) "Must be a directory"]]
   ["-o" "--output-dir DIRECTORY" "Output directory"
    :parse-fn [#(io/file %)]]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [errors options summary]} (parse-opts args cli-options)
        {:keys [input-dir output-dir help]} options]
    (when help
      (println "usage:")
      (println summary)
      (System/exit 0))
    (when errors
      (err-exit (string/join \newline errors)))
    (when-not input-dir
      (err-exit "Input directory must be specified"))
    (when-not output-dir
      (err-exit "Output directory must be specified"))
    (generate-output input-dir output-dir)))
