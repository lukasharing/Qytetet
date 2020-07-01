;;;;;;; Ejercicios Avanzados - 6 - 7 ;;;;;;;
;;;;;;; LUKAS HARING GARCÍA ;;;;;;;
; Main Menu
(deftemplate Jugadores
    (field Nombre)
    (field Altura)
)

(deffacts Jugadores_Info
    (Jugadores (Nombre "Spud Webb") (Altura 168))
    (Jugadores (Nombre "Tibor Pleiss") (Altura 221))
    (Jugadores (Nombre "Rik Smits") (Altura 224))
    (Jugadores (Nombre "Gheorghe Muresan") (Altura 231))
    (Jugadores (Nombre "Kay Felder") (Altura 175))
)

(defrule Main
    =>
    (assert (AlturaMaxima))
)

(defrule AlturaMaxima
    ?a <- (AlturaMaxima)

    ; Height Player N Maximum
    (Jugadores (Nombre ?n1) (Altura ?h1))
    ; There doesnt exist another with highest Height
    (not (Jugadores (Nombre ?) (Altura ?h2&:(> ?h2 ?h1))))

    =>
    ; Eliminamos Hecho
    (retract ?a)
    ; Buscamos el máximo
    (printout t "La altura Maxima de " ?n1 " es " ?h1 crlf)
)