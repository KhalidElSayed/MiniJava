(ns minijava.test-canon
  (:use (minijava canon ir interp)
        clojure.test))

(deftest test-remove-eseq-left
  (let [exp (BinaryOp :+ (ExpSeq [(Statement (Const 1))] (Const 3)) (Const 4))
        result [(Statement (Const 1)) (BinaryOp :+ (Const 3) (Const 4))]]
    (is (= (remove-eseq-left exp)
           result)
        "Removes ExpSeq per rule one.")))

(deftest test-remove-double-eseq
  (let [exp (ExpSeq [(Statement (Const 1))] (ExpSeq [(Statement (Const 2))]
                                                    (Const 3)))
        result [(Statement (Const 1)) (Statement (Const 2)) (Const 3)]]
    (is (= (remove-double-eseq exp)
           result)
        "Removes ExpSeq per rule two.")
    (is (= (eval-prog (seq result))
           3))))

(deftest test-remove-eseq-commute
  (let [exp (BinaryOp :+ (Const 2) (ExpSeq [(Statement (Const 0))] (Const 3)))
        result [(Statement (Const 0)) (BinaryOp :+ (Const 2) (Const 3))]]
    (is (= (remove-eseq-commute exp)
           result)
        "Removes ExpSeq per rule three.")
    (is (= (eval-prog (seq result))
           5))))

(deftest test-remove-eseq-no-commute
  (let [exp (BinaryOp :+ (Const 3) (ExpSeq [(Statement (Const 0))] (Const 2)))
        t (minijava.ir.temp.Temp.)
        result [(Move (Const 3) (Temp t))
                (Statement (Const 0))
                (BinaryOp :+ (Temp t) (Const 2))]]
    (is (= (remove-eseq-no-commute exp)
           (seq result))
        "Removes ExpSeq per rule four.")
    (is (= (eval-prog (seq result))
           5))))
