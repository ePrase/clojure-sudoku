(ns clojure-sudoku.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
)

(defn GenRan [lower upper] (+ (rand-int (- (+ 1 upper) lower)) lower))
(defn GetAcrossFromNumber [m] (if (= (mod m 9) 0) 9 (mod m 9)))
(defn GetDownFromNumber [n] 
    (if (= (GetAcrossFromNumber n) 9) 
        (/ n 9) 
        (+ 1 (quot n 9))
    )
 )
(defn GetRegionFromNumber [n]
    (let [a (GetAcrossFromNumber n)]
    (let [d (GetDownFromNumber n)]
        (if (and (<= 1 a) (< a 4) (<= 1 d) (< d 4)) 1
            (if (and (<= 4 a) (< a 7) (<= 1 d) (< d 4)) 2
                (if (and (<= 7 a) (< a 10) (<= 1 d) (< d 4)) 3
                    (if (and (<= 1 a) (< a 4) (<= 4 d) (< d 7)) 4
                        (if (and (<= 4 a) (< a 7) (<= 4 d) (< d 7)) 5
                            (if (and (<= 7 a) (< a 10) (<= 4 d) (< d 7)) 6
                                (if (and (<= 1 a) (< a 4) (<= 7 d) (< d 10)) 7
                                    (if (and (<= 4 a) (< a 7) (<= 7 d) (< d 10)) 8
                                        (if (and (<= 7 a) (< a 10) (<= 7 d) (< d 10)) 9
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        )
)

(defstruct polje :across :down :region :value :index)
(def sudoku (list))

(defn Conflicts [b]
    (for [x sudoku]
                (if (and (or (and (not= (:across x) 0) (= (:across x) (:across b)))
                    (and (not= (:down x) 0) (= (:down x) (:down b)))
                    (and (not= (:region x) 0) (= (:region x) (:region b))))
                    (= (:value x) (:value b))) 1 0
                    )
                 )
)
(defn napraviPolje [n v]
    (struct polje (GetAcrossFromNumber (+ n 1)) (GetDownFromNumber (+ n 1)) (GetRegionFromNumber (+ n 1)) v n)
)
(defn printS [celija]
  (println "across:" (:across celija) ", down:" (:down celija) ", region:" (:region celija) ", value:" (:value celija) ", index:" (:index celija)))

(def Available [])
(dotimes [n 81] (def Available (conj Available [1 2 3 4 5 6 7 8 9])))
(def c 0)
(while (not= c 81) 
    (if (> (count (nth Available c)) 0)
	    (do
            (def i (GenRan 0 (- (count (nth Available c)) 1)))
            (def z (nth (nth Available c) i))
            (def item (napraviPolje c z))
            (def k (Conflicts item))
            (def a (to-array Available))  
            (def x c)          
            (if (contains? (set k) 1)  (aset a c (remove #(= % z) (nth Available c))) 
                (do
                    (def sudoku (conj sudoku item))
                    (aset a c (remove #(= % z) (nth Available c)))
                    (def c (+ c 1))
                    )
            )
            (def Available (vec a))
			(def b (nth Available x))
            (println x (nth Available x))
        )
        (do
            (def a (to-array Available))
            (aset a c (conj (nth a c) 1 2 3 4 5 6 7 8 9))
            (def Available (vec a))
            (def sudoku (rest sudoku))
            (def c (- c 1))
            )
    )
)
(def stampa "")
(dorun
  (for [s sudoku]
      (if (= (:across s) 9)
            (def stampa (apply str [stampa "\r\n" (:value s)]))
            (def stampa (apply str [stampa " " (:value s)]))
          )
     )
  )
(println stampa)
