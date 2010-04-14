(ns minijava.test-alloc
  (:use minijava.alloc
        clojure.test))

(deftest tests-no-spilled-no-dead
  (let [val [{:start 0, :end 1}
             {:start 0, :end 2}
             {:start 1, :end 3}]
        {:keys [inreg spilled]} (scan val)]
    (is (= (count val) (count inreg)) "Every interval is allocated to reg.")
    (is (distinct? (map :reg inreg)) "Interval is given a unique register.")
    (is (empty? spilled) "Nothing is spilled.")))

(defn find-dup-in [feat s]
  (loop [[f & rest] (sort-by :reg s)]
    (when (seq rest)
      (if (= (feat f) (feat (first rest)))
        [f (first rest)]
        (recur rest)))))

(deftest tests-dead-no-spilled
  (let [val [{:start 0, :end 1}
             {:start 0, :end 2}
             {:start 1, :end 3}
             {:start 2, :end 3}]
        {:keys [inreg spilled]} (scan val)
        doubled (find-dup-in :reg inreg)]
    (is (= (count val) (count inreg)) "Every interval is allocated to reg.")
    (is (not (nil? doubled)) "Registers are not unique.")
    (is (empty? spilled) "Nothing is spilled.")))

(deftest tests-spilled
  (let [val [{:start 0, :end 3}
             {:start 0, :end 2}
             {:start 1, :end 4}
             {:start 2, :end 6}]
        {:keys [inreg spilled]} (scan val)
        longest-lived (first (reverse (sort-by :reg val)))]
    (is (not (= (count val) (count inreg))) "Not every interval in reg.")
    (is (not (empty? spilled)) "Something is spilled.")
    (is (= (dissoc (first spilled) :reg) longest-lived)
        "Longest-lived interval spilled.")))
