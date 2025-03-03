(ns cocdan.core.request
  (:require
   [clojure.core.async :refer [go <!]]
   [cljs-http.client :as http]
   [re-frame.core :as rf]))

(rf/reg-event-fx
 :event/patch-to-server
 (fn [_ [_driven-by base-key attrs]]
   (when (and (contains? #{:avatar :stage} base-key) (not (nil? (:id attrs))))
     (go (let [id (:id attrs)
               url (str "/api/" (name base-key) "/" (first (name base-key)) id)
               res (<! (http/patch url {:json-params attrs}))]
           (when (= (:status res) 200)
             (js/console.log res)
             (rf/dispatch [:rpevent/upsert base-key (:body res)])))))
   {}))
