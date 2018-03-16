(ns little-markdown-wiki.generate-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
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

(deftest generate-output-test
  (testing "generates an empty HTML page from an empty wiki"
    (let [input-dir (test-input-dir "empty")
          output-dir (test-output-dir)]
      (generate-output input-dir output-dir)
      (is (= ["index.html"] (filename-vec output-dir))))))
