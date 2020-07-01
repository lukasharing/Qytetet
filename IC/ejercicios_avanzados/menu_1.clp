;;;;;;; Ejercicios Avanzados - 1 ;;;;;;;
;;;;;;; LUKAS HARING GARCÍA ;;;;;;;
; Main Menu
(defrule Main
    =>
    (assert (Ask))
)

(defrule Menu
        ?a <- (Ask)
    =>
        (retract ?a)

        (printout t "Cual es tu animal favorito?" crlf)
        (printout t "- El caballo negro" crlf)
        (printout t "- El perro blanco" crlf)
        (printout t "- El gato pardo" crlf)
        (printout t "- El canario amarillo" crlf)
        (printout t "- El conejo marron" crlf)
        (printout t "Escriba cual es: ")

        (bind ?selection (explode$ (lowcase (readline))))
        (assert (Respuesta ?selection))
)
; Options
; 1.
(defrule El_Caballo_Negro
    (declare (salience 1))
    ?r <- (Respuesta el caballo negro)
    =>
    (retract ?r)
    (assert (OpcionElegida El_Caballo_Negro))
)

; 2.
(defrule El_Perro_Blanco
    (declare (salience 1))
    ?r <- (Respuesta el perro blanco)
    =>
    (retract ?r)
    (assert (OpcionElegida El_Perro_Blanco))
)

;3.
(defrule El_Gato_Pardo
    (declare (salience 1))
    ?r <- (Respuesta el gato pardo)
    =>
    (retract ?r)
    (assert (OpcionElegida El_Gato_Pardo))
)

; 4.
(defrule El_Canario_Amarillo
    (declare (salience 1))
    ?r <- (Respuesta el canario amarillo)
    =>
    (retract ?r)
    (assert (OpcionElegida El_Canario_Amarillo))
)

; 5.
(defrule El_Conejo_Marron
    (declare (salience 1))
    ?r <- (Respuesta el conejo marron)
    =>
    (retract ?r)
    (assert (OpcionElegida El_Conejo_Marron))
)

; None
(defrule Some_Other_Response
    (declare (salience -10))
    ?r <- (Respuesta $?)
    =>
    (retract ?r); Retract all facts
    (assert (Ask)) ; Aask Again
    (printout t "Opción no válida!" crlf)
)