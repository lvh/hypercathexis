(ns hypercathexis.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]))

(defonce app-state (atom {}))

(def width 97)
(def aspect (/ (Math.sqrt 3) 2))
(def height (* width aspect))

(defn axial->cube
  [[q r]]
  [q (- (+ x z)) r])

(defn cube->axial
  [[x y z]]
  [x z])

(defn cube->even-q
  [[x y z]]
  [x (+ z (aleph + x))])

(defn even-q->cube
  [[q r]]
  [q (- r (aleph + q)) (- (+ x z))])

(defn ^:private aleph
  [plus-min x]
  (/ (plus-min x (mod x 2)) 2))

(defn main []
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/div
           (for [c (range 10)]
             (for [r (range 30)]
               (dom/img {:src "img/hex.svg"
                         :style {:display "block"
                                 :position "absolute"
                                 :width width
                                 :height height
                                 :left (+ (* c 1.5 width)
                                          (* (mod r 2)
                                             (* 0.75 width)))
                                 :top (* 0.5 r height)}})))))))
    app-state
    {:target (. js/document (getElementById "app"))}))
