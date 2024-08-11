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
(require '[babashka.cli :as cli])

(load-file "api.clj")
(load-file "core.clj")
(load-file "request.clj")

(def seq-size 99)

(defn request-blocks-headers-seq [url from to]
  (we.request/get (we.api/blocks-headers-seq url from to)))

(defn block->string [block]
  (str "| Height: " (get block "height")
       " | Signature: " (we.core/ellipsis (get block "signature"))
       " | Transaction count: " (get block "transactionCount")
       " | Block size: " (get block "blocksize")
       " | Timestamp: " (get block "timestamp")
       " |\n"))

(defn blocks->string [blocks]
  (apply str (map #(block->string %) blocks)))

(defn next [now target]
  (let [new (+ now seq-size)]
    (if (< new target) new target)))

(defn handler [opts]
  (loop [from (:from opts)
         to (+ from seq-size)]
    (when (< from (:to opts))
      (print (blocks->string (we.request/parse-body (request-blocks-headers-seq (:url opts) from to))))
      (recur (+ to 1) (next to (:to opts))))))

(def cli-spec {:spec
               {:url {:coerce :string
                      :alias :u
                      :ref "<URL>"
                      :desc "Node URL"
                      :require true}
                :from {:coerce :long
                       :alias :f
                       :ref "<FROM>"
                       :desc "Height of the starting block"
                       :require true}
                :to {:coerce :long
                     :alias :t
                     :ref "<TO>"
                     :desc "Height of end block"
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
  (let [opts (cli/parse-opts args cli-spec)]
    (if (:help opts)
      (println (cli/format-opts cli-spec))
      (handler opts))))

(-main *command-line-args*)
