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
(load-file "core.clj")
(load-file "rest-api.clj")
(load-file "mainnet.clj")

(def visible-size 5)

(defn block->string [block]
  (str "| Height: " (get block "height")
       " | Reference: " (we.core/ellipsis visible-size (get block "reference"))
       " | Signature: " (we.core/ellipsis visible-size (get block "signature"))))

(defn version->string [node version]
  (str "| " node " | Version: " (get version "version")))

(defn handler [node]
  (let [block (we.core/parse-body (we.core/request (we.rest-api/blocks-last node)))
        version (we.core/parse-body (we.core/request (we.rest-api/node-version node)))]
    (str (version->string node version) "\n" (block->string block) "\n\n")))

(println (apply str (map #(handler %) we.mainnet/nodes)))
