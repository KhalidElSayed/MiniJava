(ns minijava.alloc
  "Register allocation, using a linear scan algorithm.
   See Poletto and Sarkar[1999]"
  (:use [minijava flow gas]))

(declare expire spill)

(def regs #{:EAX :EBX :EDX})

;; liveness interval {:reg REGS, :start lineno, :end lineno}
(defn scan
  "Scan through register intervals, allocating a register/stack pointer
to each. Returns allocated intervals."
  [intrvls]
  (let [dead    (atom '())
        allocd  (atom #{})
        active  (atom '())
        spilled (atom '())]
    (doseq [i (sort-by :start intrvls)]
      (swap! active expire i dead)
      (if (= (count @active) (count regs))
        (swap! spilled conj (spill i active))
        (let [reg (first (clojure.set/difference regs @allocd))
              i   (assoc i :reg reg)]
          (swap! allocd conj reg)
          (swap! active conj i))))
    {:inreg (concat @dead @active), :spilled @spilled}))

(defn- expire
  "Expire registers that've been freed from intrvl and intrvl - 1."
  [active intrvl dead]
  (let [[active died] (split-with #(>= (:end %) (:start intrvl)) active)]
    (swap! dead concat died)
    active))

(defn- spill
  "Spill register with longest time left."
  [intrvl active]
  (let [spill (first @active)]
    (if (> (:end spill) (:end intrvl))
      (let [intrvl (assoc intrvl :reg (:reg spill))]
        (reset! active (conj (rest @active) intrvl))
        intrvl)
      intrvl)))
