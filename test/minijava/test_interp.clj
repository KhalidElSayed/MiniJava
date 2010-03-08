(ns minijava.test-interp
  (:use clojure.test
        (minijava ast interp label tree utility x86))
  (:import (minijava.ir.temp.label))
  (:require [minijava.ir :as ir]))

(import-ast-classes)

(def empty-frame (new-x86 0 ["obj"]))

(deftest test-eval-const
  (let [const (tree (parse-exp "5") empty-frame)]
    (is (= 5 (eval-ir const empty-env)))))

(deftest test-eval-binop
  (let [plus  (tree (parse-exp "5 + 5") empty-frame)
        minus (tree (parse-exp "5 - 5") empty-frame)
        times (tree (parse-exp "5 * 5") empty-frame)]
    (is (= 10 (eval-ir plus empty-env)))
    (is (= 0  (eval-ir minus empty-env)))
    (is (= 25 (eval-ir times empty-env)))))

(deftest test-temp
  (let [tmp (minijava.ir.temp.Temp.)
        prog (list ;(ir/Move (ir/Const 5) (ir/Temp tmp))
                   (ir/Temp tmp))]
    (is (= 5 (eval-prog prog)))))

(deftest test-jump
   (let [t (label)
         tmp (minijava.ir.temp.Temp.)
         prog (list ;(ir/Move (ir/Const 0) (ir/Temp tmp))
                    (ir/Jump t)
                    ;(ir/Move (ir/Const 5) (ir/Temp tmp))
                    (ir/Label t)
                    (ir/Temp tmp))]
     (is (= 5 (eval-prog prog)))))

(deftest test-labels
   (let [t (label)
         prog (list (ir/Label t)
                    (ir/Const 5))]
     (is (= (build-label-map prog (hash-map))
            (hash-map t (list (ir/Const 5)))))))

(deftest test-label
  (let [t (label)
        prog (list (ir/Label t))]
    (is (= (ir/Label t)
           (first prog)))))
