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
(require '[babashka.http-client :as http])

(def visible-size 5)

(def nodes (vector "https://voting-node-0.we.vote"
                   "http://213.238.172.133:6862"
                   "http://usdn.wavesbi.com:6862"
                   "http://173.249.26.213:6862"
                   "http://158.160.119.249:6862"
                   "http://vmi376896.contaboserver.net:6862"
                   "http://104.248.83.120:6862"))

(defn blocks-last [url]
  (str url "/blocks/last"))

(defn node-version [url]
  (str url "/node/version"))

(defn request [url]
  (http/get url {:headers {"Accept" "application/json"}}))

(defn parse-body [response]
  (json/parse-string (:body response)))

(defn ellipsis [string]
  (let [len (count string)]
    (str (subs string 0 visible-size) "..." (subs string (- len visible-size) len))))

(defn block->string [block]
  (str "| Height: " (get block "height")
       " | Reference: " (ellipsis (get block "reference"))
       " | Signature: " (ellipsis (get block "signature"))))

(defn version->string [node version]
  (str "| " node " | Version: " (get version "version")))

(defn handler [node]
  (let [block (parse-body (request (blocks-last node)))
        version (parse-body (request (node-version node)))]
    (str (version->string node version) "\n" (block->string block) "\n\n")))

(println (apply str (map #(handler %) nodes)))
