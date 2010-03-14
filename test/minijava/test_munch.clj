(ns minijava.test-munch
  (:use (minijava gas ir munch)
        clojure.test))

(deftest test-Binop
				(let [tree (BinaryOp :+ (Const 2) (Const 1))]				
				 (is (= (select tree)
           (list (CONST 3)))
				)))

;;Note, for the test cases, you have to manually define the temp names, otherwise they will auto increment
;;and fail to match
(deftest test-Binop-exp-const
				(let [tree (BinaryOp :+ (Temp (minijava.ir.temp.Temp. "t2")) (Const 1))]				
				 (is (= (select tree)
           (list (movl (CONST 1) (Temp (minijava.ir.temp.Temp. "t1")))
           			 (addl (Temp (minijava.ir.temp.Temp. "t2")) (Temp (minijava.ir.temp.Temp. "t1"))))
				))))

(deftest test-Binop-exp-exp
				(let [tree (BinaryOp :+ (Temp (minijava.ir.temp.Temp. "t1")) (Temp (minijava.ir.temp.Temp. "t2")) )]				
				 (is (= (select tree)
           (list (movl  (Temp (minijava.ir.temp.Temp. "t1")) (Temp (minijava.ir.temp.Temp. "t3")))
           			 (addl (Temp (minijava.ir.temp.Temp. "t2")) (Temp (minijava.ir.temp.Temp. "t3")))))
				)))

(deftest test-Move-Mem-Binop
				(let [
				   t1 (Temp (minijava.ir.temp.Temp.))
				   t2 (Temp (minijava.ir.temp.Temp.))
					tree (Move t1 (Mem (BinaryOp :+ (Const 2)  t2)))]
				
				 (is (= (select tree)
          (list(movl (MEMORY t1 2)t2)))
				)))
