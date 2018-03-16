(ns little-markdown-wiki.generate
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn- get-markdown-files [dir]
  (->> (file-seq dir)
       (filter (memfn isFile))
       (filter #(string/ends-with? (.getName %) ".md"))))



(defn generate-output [input-dir output-dir]
  (let [markdown-files (get-markdown-files input-dir)
        index (io/file output-dir "index.html")
        index-html (str "<!doctype html>"
                        "<html>"
                        "<head>"
                          "<meta charset=\"utf-8\">"
                          "<title>Wiki</title>"
                        "</head>"
                        "<body>"
                          "There are no pages yet!"
                        "</body>"
                        "</html>")
        ]
    (spit index index-html)
  ))
