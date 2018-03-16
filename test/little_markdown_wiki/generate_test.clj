(ns little-markdown-wiki.generate-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [little-markdown-wiki.generate :refer :all])
  (:import [java.nio.file Files]
           [java.nio.file.attribute FileAttribute]))

(defn- test-input-dir [dir-name]
  (-> (str "test-fixtures/" dir-name) io/resource io/file))

(defn- test-output-dir []
  (let [prefix "little-markdown-wiki-test"
        file-attributes (into-array FileAttribute [])
        path (Files/createTempDirectory prefix file-attributes)]
    (-> path .toUri io/file)))

(defn- filename-vec [dir]
  (->> (file-seq dir)
       (filter (memfn isFile))
       (map (memfn getName))
       vec))

(defn- html-for [dir without-extension]
  (->> (str without-extension ".html")
       (io/file dir)
       slurp))

(defn- valid-html? [html]
  (and (string/starts-with? html "<!doctype html>")
       (string/ends-with? html "</html>")
       (string/includes? html "<meta charset=\"utf-8\">")))

(defn- title-eq? [html title]
  (string/includes? html (str "<title>" title "</title>")))

(deftest empty-wiki-test
  (let [input-dir (test-input-dir "empty")
        output-dir (test-output-dir)]
    (generate-output input-dir output-dir)
    (is (= ["index.html"] (filename-vec output-dir)))
    (let [index-html (html-for output-dir "index")]
      (is (valid-html? index-html))
      (is (title-eq? index-html "Wiki"))
      (is (not (string/includes? index-html "<li>"))))))

(deftest index-dot-md-test
  (let [input-dir (test-input-dir "index-only")
        output-dir (test-output-dir)]
    (generate-output input-dir output-dir)
    (is (= ["index.html"] (filename-vec output-dir)))
    (let [index-html (html-for output-dir "index")]
      (is (valid-html? index-html))
      (is (title-eq? index-html "Wiki"))
      (is (string/includes? index-html "Hello <b>world</b>")))))
