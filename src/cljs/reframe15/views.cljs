(ns reframe15.views
  (:require
   [re-frame.core :as re-frame]
   [reframe15.subs :as subs]
   [reframe15.events :as events]))

(defn main-panel []
  (let [tiles @(re-frame/subscribe [::subs/tiles])
        highlighted @(re-frame/subscribe [::subs/highlighted])]
    [:div.game-board
     (for [label tiles]
       (let [class (if (= label highlighted) :highlighted-box :box)]
         [:div
          {:class class
           :on-click #(re-frame/dispatch [::events/highlight-tile label])}
          (if (= label 0) nil label)]))]))
