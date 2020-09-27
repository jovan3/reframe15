(ns reframe15.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::tiles
 (fn [db]
   (:tiles db)))

(re-frame/reg-sub
 ::highlighted
 (fn [db]
   (:highlighted db)))

(re-frame/reg-sub
 ::moves
 (fn [db]
   (:moves db)))
