(ns cards.cards
  (:require [cards.core :refer [in?]]))

(def SUITS "HSDC")
(def FACES "23456789TJQKA")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn create-deck
  "Created a sorted deck, for use in card games."
  []
  (apply concat (map (fn [suit] (map #(str % suit) FACES)) SUITS)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn shuffle-deck
  "Shuffle a deck of cards."
  [deck]
  (shuffle deck))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn face-value
  [card]
  (+ 2 (.indexOf FACES (str (first card)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn in-same-suit
  [card1 card2]
  (= (second card1) (second card2)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn valid-card
  [card]
  (boolean
    (and
      (= (count card) 2)
      (in? FACES (first card))
      (in? SUITS (second card)))))
