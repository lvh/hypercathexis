(ns hypercathexis.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [clojure.string :refer [join]]))

(defonce app-state (atom {}))

(def regular-aspect (/ (Math.sqrt 3) 2))

(def ^:private base-hex-coords
  "Base hex coordinates, relative to a width and height of 1."
  [[-0.50 +0.25]
   [+0.00 +0.50]
   [+0.50 +0.25]
   [+0.50 -0.25]
   [+0.00 -0.50]
   [-0.50 -0.25]])

(def base-vecs
  "Axial base vectors."
  [[1 0]
   [.5 .75]])

(defn scale-vec
  "Scales a vector by a scalar."
  [x v]
  (map (partial * x) v))

(defn add-vecs
  "Adds vectors elementwise."
  [& vs]
  (apply (partial map +) vs))

(defn translate
  "Translation vectors for given axial coordinates."
  [v]
  (apply add-vecs (map scale-vec v base-vecs)))

(defn translation->svg
  [[dx dy]]
  (str "translate(" dx "," dy ")"))

(defn scale->svg
  [& xs]
  (str "scale(" (join "," xs) ")"))

(defn coords->svg
  [coords]
  (let [coord-strs (map (partial join ",") coords)]
    (join " " coord-strs)))

(defn axial->cube
  [[q r]]
  (let [x q
        z r
        y (bet x z)])
  [x y z])

(defn cube->axial
  [[x y z]]
  [x z])

(defn cube->even-q
  [[x y z]]
  [x (+ z (aleph + x))])

(defn even-q->cube
  [[q r]]
  (let [x q
        z (- r (aleph + q))
        y (bet x z)])
  [x y z])

(defn ^:private aleph
  [plus-min x]
  (/ (plus-min x (mod x 2)) 2))

(defn ^:private bet
  [a b]
  (- (+ a b)))

(defn main []
  (om/root
   (fn [app owner]
     (reify
       om/IRender
       (render [_]
         (let [scale 100
               hex-height (* scale regular-aspect)
               q-hexes 10
               r-hexes 10
               start-q 0
               start-r 0]
           (dom/svg
            {:view-box (let [offset (- (/ scale 2))]
                         (join " " [offset offset 1000 1000]))}
            (dom/g
             {:transform (apply scale->svg (scale-vec scale [regular-aspect 1]))}
             (for [q (range q-hexes)
                   r (range r-hexes)]
               (dom/polygon {:fill "black"
                             :stroke "grey"
                             :stroke-width (/ 1 scale)
                             :transform (translation->svg (translate [q r]))
                             :points (coords->svg base-hex-coords)}))))))))
   app-state
   {:target (. js/document (getElementById "app"))}))
