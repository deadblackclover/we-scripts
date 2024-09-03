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
(load-file "request.clj")

(defn request-peers-connected [url]
  (get (we.request/parse-body (we.request/get (we.api/peers-connected url))) "peers"))

(defn request-privacy-owners [url policy]
  (we.request/parse-body (we.request/get (we.api/privacy-owners url policy))))

(defn handler [url policy]
  (let [peers (reduce #(assoc %1 (get %2 "nodeOwnerAddress") %2) {} (request-peers-connected url))
        addresses (request-privacy-owners url policy)]
    (doseq [address addresses]
      (let [peer (get peers address)]
        (print (str "| " address))
        (if (not (nil? peer))
          (print (str " | Name: " (get peer "peerName")
                      " | Version: " (get peer "applicationVersion")
                      " | Address: " (get peer "address")
                      " | Declared address: " (get peer "declaredAddress"))))
        (println " |")))))

(def cli-spec {:spec
               {:url {:coerce :string
                      :alias :u
                      :ref "<URL>"
                      :desc "Node URL"
                      :require true}
                :policy {:coerce :string
                         :alias :p
                         :ref "<POLICY_ID>"
                         :desc "Policy ID"
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
        {:keys [url policy help]} opts]
    (if help
      (println (cli/format-opts cli-spec))
      (handler url policy))))

(-main *command-line-args*)
