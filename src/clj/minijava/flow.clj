(ns minijava.flow
  "Given a list of GAS instructions, creates a dataflow graph (as in 10.1)."
  (:use (minijava gas))
  (:require [minijava.temp :as tm]))

;; helper method to do instanceof
(defn isit? [x t]
  (= (type x) t))

;; create a map mapping temp/labels to the instructions that follow them
(defn mapLabels [program]
  (loop [instrs program
         labelMap (hash-map)]
    (cond (empty? instrs)
            labelMap
          (isit? (first instrs) :minijava.gas/LABEL)
            (recur (rest instrs) (assoc labelMap (:lbl (first instrs)) (second instrs)))
          :else
            (recur (rest instrs) labelMap))))

(defn addSuccessor [succMap mapKey succ]
  (let [cur (get succMap mapKey)
        cur (if (set? cur) cur (hash-set)) ;; ensure cur is a hashset
        updated (conj cur succ)]
    (assoc succMap mapKey updated)))

;; create a map linking each instruction to a list of successors
(defn flow [program]
  ;; for each label, determine what its following instruction is. This
  ;; avoids repeated linear time searches later on.
  (let [labelMap (mapLabels program)]
    (loop [instrs program
           succMap (hash-map)]
      ;; for anything except an unconditional jump, the next
      ;; instruction is a successor
      (let [hasNext (not (isit? (first instrs) :minijava.gas/jmp)) 
            hasLabel (or (isit? (first instrs) :minijava.gas/jmp)
                         (isit? (first instrs) :minijava.gas/jcc))
            succMap (if hasNext (addSuccessor succMap (first instrs)
                                              (second instrs)) succMap)
            succMap (if hasLabel (addSuccessor succMap (first instrs)
                                               (get labelMap (:dst (first instrs)))) succMap)]
        (recur (rest instrs) succMap)))))
