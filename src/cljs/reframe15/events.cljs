(ns reframe15.events
  (:require
   [re-frame.core :as re-frame]
   [reframe15.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(defn swap [col val1 val2]
  (map #(get {val1 val2 val2 val1} % %) col))

(defn adjecent-indices [position]
  (let [valid-adj-positions (cond (zero? (mod position 4)) [1 4 -4]
                                  (zero? (mod (inc position) 4)) [-1 4 -4]
                                  :else [1 -1 4 -4])]
    (filter (fn [i] (<= 0 i 16))
            (map #(+ position %) valid-adj-positions))))

(defn adjecent? [col val1 val2]
  (let [positions-map (into {} (map-indexed (fn [index item] [item index]) col))
        val1-position (positions-map val1)
        val2-position (positions-map val2)
        val2-adjecent (adjecent-indices val2-position)]
    (some #(= val1-position %) val2-adjecent)))

(defn valid-move? [current-tiles old-tile new-tile]
  (and (adjecent? current-tiles old-tile new-tile)
       (= 0 new-tile)))

(re-frame/reg-event-fx
 ::highlight-tile
 (fn [{:keys [db]} [_ item]]
   (let [current-highlighted (db :highlighted)
         new-highlighted (if (= current-highlighted item) nil item)]
     {:db (assoc db :highlighted new-highlighted)
      :dispatch [::change-tile current-highlighted new-highlighted]})))

(re-frame/reg-event-fx
 ::change-tile
 (fn [{:keys [db]} [_ old-tile new-tile]]
   (let [current-tiles (db :tiles)
         move-tile? (valid-move? current-tiles old-tile new-tile)]
     {:db (if move-tile?
            (assoc db :tiles (swap current-tiles 0 old-tile))
            db)
      :dispatch [::increase-moves-counter move-tile?]})))

(re-frame/reg-event-db
 ::increase-moves-counter
 (fn [db [_ move-tile?]]
   (let [moves (db :moves)]
     (if move-tile? (assoc db :moves (inc moves)) db))))
