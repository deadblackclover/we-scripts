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
         '[babashka.fs :as fs])

(load-file "api.clj")
(load-file "core.clj")
(load-file "request.clj")

(defn read [path]
  (apply str (fs/read-all-lines path)))

(defn request [url body]
  (we.request/post (we.api/utils-script-compile url) body))

(defn handler [opts]
  (let [{url :url path :path} opts]
    (println (:body (request url (read path))))))

(def cli-spec {:spec
               {:url {:coerce :string
                      :alias :u
                      :ref "<URL>"
                      :desc "URL"
                      :require true}
                :path {:coerce :string
                       :alias :p
                       :ref "<PATH>"
                       :desc "PATH"
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
