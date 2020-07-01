;;;;;;; SISTEMA EXPERTO FINAL ;;;;;;;
;;;;;;; LUKAS HARING GARCÍA ;;;;;;;

(defglobal
    ?*PATH* = "D:/wamp64/www/University_Projects/IC/final/"
)

(defrule Main
        (declare (salience -100))
    =>
        ; Hacemos mandamos el menú
        (clear)
        (load (str-cat ?*PATH* "Recomendador_Rama.clp"))
        (load (str-cat ?*PATH* "Recomendador_Asignatura.clp"))
        (reset)
        (assert (Ask))
)

(defrule Menu
        ?a <- (Ask)
    =>
        (printout t 
            "Indique que tipo de asesoramiento desea:" crlf
                "- Asesoramiento para la rama" crlf
                "- Asesoramiento para un numero de creditos" crlf
            "Indique la opcion tomada: "
        )

        (bind ?selection (explode$ (lowcase (readline))))
        (assert (Respuesta ?selection))
        (retract ?a)
)

; 1.
(defrule Asesoramiento_Rama
        (declare (salience 1))
        ?r <- (Respuesta asesoramiento $? rama $?)
    =>
        (retract ?r)
        (focus Recomendador_Rama)
)

; 2.
(defrule Asesoramiento_Asignaturas
        (declare (salience 1))
        ?r <- (Respuesta asesoramiento $? creditos $?)
    =>
        (retract ?r)
        (focus Recomendador_Asignaturas)
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