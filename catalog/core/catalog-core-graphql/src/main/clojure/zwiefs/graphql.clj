(ns zwiefs.graphql
  (require [clojure.edn :as edn]
           [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
           [com.walmartlabs.lacinia.schema :as schema]
           [ring.adapter.jetty :refer [run-jetty]]
           [com.walmartlabs.lacinia :refer [execute]]
           [clojure.data.json :as json]
           [clojure.string :as str]
           [ring.util.codec :as codec]
           [clojure.walk :as walk]))


(defn get-hero [context arguments value]
  (let [{:keys [episode]} arguments]
    (if (= episode :NEWHOPE)
      {:id          1000
       :name        "Luke"
       :home_planet "Tatooine"
       :appears_in  ["NEWHOPE" "EMPIRE" "JEDI"]}
      {:id          2000
       :name        "Lando Calrissian"
       :home_planet "Socorro"
       :appears_in  ["EMPIRE" "JEDI"]})))


(def my-schema
  "{:enums
 {:episode
  {:description \" The episodes of the original Star Wars trilogy. \"
   :values [:NEWHOPE :EMPIRE :JEDI]}}

 :objects
 {:droid
  {:fields {:primary_functions {:type (list String)}
            :id {:type Int}
            :name {:type String}
            :appears_in {:type (list :episode)}}}

  :human
  {:fields {:id {:type Int}
            :name {:type String}
            :home_planet {:type String}
            :appears_in {:type (list :episode)}}}}

 :queries
 {:hero {:type (non-null :human)
         :args {:episode {:type :episode}}
         :resolve :get-hero}
  :droid {:type :droid
          :args {:id {:type String :default-value \" 2001 \"}}
          :resolve :get-droid}}} ")

(def basic-schema
  (-> my-schema
      edn/read-string
      (attach-resolvers {:get-hero  get-hero
                         :get-droid (constantly {})})
      schema/compile))

(defn get-query-params [s]
  (walk/keywordize-keys (codec/form-decode s)))

(defn app-handler [request]
  ;(println request)
  ;(println (:query (get-query-params (get-in request [:query-string]))))
  ;(println (slurp (:body request)))
  ;(println)
  (let [req-method (:request-method request)]
    (cond
      (= req-method :get) {:status  200
                           :headers {"Content-Type" "application/json"}
                           :body    (let [query (-> request
                                                    :query-string
                                                    get-query-params
                                                    :query)
                                          result (execute basic-schema query nil nil)]
                                      (json/write-str result))}
      (= req-method :post) {:status  200
                            :headers {"Content-Type" "application/json"}
                            :body    (let [query (slurp (:body request))
                                           result (execute basic-schema query nil nil)]
                                       (json/write-str result))}))
  )

(defn test-string []
  "Hello this is a test livereload string. with additions! woot woot ")

;(def ah (ref #'zwiefs.graphql/app-handler))
;(def server (Thread. (fn [] (run-jetty ah {:port 8994}))))
; (.start server)


; (run-jetty app-handler {:port 8994})