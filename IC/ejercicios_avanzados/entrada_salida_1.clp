;;;;;;; Ejercicios Avanzados - 8 ;;;;;;;
;;;;;;; LUKAS HARING GARCÍA ;;;;;;;

; Main 
(defrule Main
    =>
        (assert (WRTIE 1 "hola" 2 3 4 "adios"))
        (assert (WRTIE 1 "+" 2 "*" 3 "/" 4.2))
)

;;; Write
(defrule Write
        ?f <- (WRTIE $?write)
    =>
        (printout t "Escribiendo en datos.txt" crlf)
        (open "datos.txt" file "w")
        (loop-for-count (?i 1 (length $?write)) do 
            (printout t "Ecribiendo: " (nth$ ?i $?write) crlf)
            (printout file (nth$ ?i $?write))
        )
        (close file)
        (retract ?f)
)