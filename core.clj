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
(ns we.core)

(require '[babashka.http-client :as http])
(require '[cheshire.core :as json])

(defn request [url]
  (http/get url {:headers {"Accept" "application/json"}}))

(defn parse-body [response]
  (json/parse-string (:body response)))

(defn ellipsis [size string]
  (let [len (count string)]
    (str (subs string 0 size) "..." (subs string (- len size) len))))
