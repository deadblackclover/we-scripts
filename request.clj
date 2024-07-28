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
(ns we.request)

(require '[babashka.http-client :as http]
         '[cheshire.core :as json])

(defn get [url]
  (http/get url {:headers {"Accept" "application/json"}}))

(defn post [url body]
  (http/post url {:body body :throw false}))

(defn parse-body [response]
  (json/parse-string (:body response)))
