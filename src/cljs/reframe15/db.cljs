(ns reframe15.db)

(def default-db
  {:tiles (shuffle (range 0 16))
   :highlighted 0})
