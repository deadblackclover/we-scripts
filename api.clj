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
(ns we.api)

(defn blocks-last [url]
  (str url "/blocks/last"))

(defn blocks-headers-at [url height]
  (str url "/blocks/headers/at/" height))

(defn blocks-headers-seq [url from to]
  (str url "/blocks/headers/seq/" from "/" to))

(defn node-version [url]
  (str url "/node/version"))

(defn utils-script-compile [url]
  (str url "/utils/script/compile"))
