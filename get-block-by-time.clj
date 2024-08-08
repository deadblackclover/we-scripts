;; Copyright (c) 2024, DEADBLACKCLOVER.

;; This program is free software; you can redistribute it and/or
;; modify it under the terms of the GNU General Public License as
;; published by the Free Software Foundation, either version 3 of the
;; License, or (at your option) any later version.

;; This program is distributed in the hope that it will be useful, but
;; WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
;; General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with this program.  If not, see <http://www.gnu.org/licenses/>.
(require '[babashka.cli :as cli]
         '[babashka.fs :as fs]
         '[clojure.math :as math])

(load-file "api.clj")
(load-file "request.clj")

(defn request-blocks-last [node]
  (we.request/get (we.api/blocks-last node)))

(defn request-blocks-headers-at [node height]
  (we.request/get (we.api/blocks-headers-at node height)))

(defn request-blocks-headers-seq [node from to]
  (we.request/get (we.api/blocks-headers-seq node from to)))

(defn block->height [block]
  (get (we.request/parse-body block) "height"))

(defn block->timestamp [block]
  (get (we.request/parse-body block) "timestamp"))

(defn handler [url height timestamp]
  (loop [left 0
         right height]
    (if (> (- right left) 99)
      (let [center (+ (math/round (/ (- right left) 2)) left)
            t (block->timestamp (request-blocks-headers-at url center))]
        (if (> t timestamp) (recur left (- right (- right center))) (recur (+ left (- center left)) right)))
      (println (:body (request-blocks-headers-seq url left right))))))

(def cli-spec {:spec
               {:url {:coerce :string
                      :alias :u
                      :ref "<URL>"
                      :desc "Node URL"
                      :require true}
                :timestamp {:coerce :long
                            :alias :t
                            :ref "<TIMESTAMP>"
                            :desc "Approximate block timestamp"
                            :require true}
                :help {:coerce :boolean
                       :alias :h
                       :desc "Print help"}}
               :error-fn
               (fn [{:keys [spec type cause msg option] :as data}]
                 (if (= :org.babashka/cli type)
                   (case cause
                     :require (println (format "%s" msg)))))})

(defn -main [args]
  (let [opts (cli/parse-opts args cli-spec)
        {url :url timestamp :timestamp help :help} opts
        last-block (request-blocks-last url)
        height (block->height last-block)]
    (if help
      (println (cli/format-opts cli-spec))
      (if (> timestamp (block->timestamp last-block))
        (println "There is no block with that timestamp yet!")
        (handler url height timestamp)))))

(-main *command-line-args*)
