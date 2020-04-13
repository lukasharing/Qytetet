;;;;;;; SISTEMA EXPERTO ;;;;;;;
;;;;;;; LUKAS HARING GARCÍA ;;;;;;;

;;; El experto utiliza la calificación de Alta, Media o Baja
;;; -> (Calificacion Alta|Media|Baja)
;;; El experto utiliza el interés por el hardware o el software, solo dos opciones
;;; -> (Interes Software|Hardware)
;;; El experto utiliza el interés por las matemáticas
;;; -> (Gustar_Matematicas Si|No)
;;; El experto utiliza el interés por la programación
;;; -> (Gusta_Programar Mucho|Medio|Poco)
;;; El experto utiliza el interés el puesto de trabajo
;;; -> (Trabaja Docencia|Empresa|Autonomo) 

;;; Los años cursados condicionará a las decisiones tomadas

(defglobal
    ?*NOMBREEXPERTO* = "Lukas Haring Garcia"
)

(deffacts Ramas
    (Rama Computacion_y_Sistemas_Inteligentes)
    (Rama Ingenieria_del_Software)
    (Rama Ingenieria_de_Computadores)
    (Rama Sistemas_de_Informacion)
    (Rama Tecnologias_de_la_Informacion)
)

; Elimina Rama
(defrule Elimina_Rama_Y_Marca
    ; Se da prioridad a eliminar la rama
    (declare (salience 100))
        ?e <- (EliminaRama ?f)
        ?r <- (Rama ?f)
    =>
        (retract ?e)
        (retract ?r)
)

(defrule Elimina_Marca
    ; Se da prioridad a eliminar la rama
    (declare (salience 99))
        ?e <- (EliminaRama ?f)
    =>
        (retract ?e)
)

; Si Solo hay una rama, Añadimos la propuesta
(defrule Aniadimos_Propuesta
    (declare (salience 1000))
        (logical 
            (Rama ?r)
            (not (Rama ?p&~?r))
        )
    =>
        (assert (HayPropuesta))
)


; Main
(defrule Pregunta_Nombre
    =>
        (printout t "Hola! Indique su nombre:")
        (assert (Nombre (read)))
        (printout t "Indique el anio de comienzo de su promocion:")
        (bind ?AniosCursados (- 2020 (read)))
        (assert (AniosCursados ?AniosCursados))
)

; Analiza Nombre y años cursados
(defrule Saluda_Analiza
        (Nombre ?Nombre)
        (AniosCursados ?Anios)
        ; Asusmimos qu elos nombres tienen al menos 3 caracteres
        (test (>= (str-length ?Nombre) 3))
        ; Al menos hemos estudiado 1 año y menos de 8
        ; Ya que se permite como normativa estar 8 años en una carrera
        (test (and (>= ?Anios 1) (<= ?Anios 7)))
    =>
        (printout t "Hola " ?Nombre ", llevas " ?Anios " anios cursados!" crlf)
        (assert (Pedir_Informacion Calificacion))
)

; Si años cursados > 5, entonces quitamos ramas complejas.
(defrule MuchosAniosCursados
            ; Se da prioridad a eliminar la rama
            (declare (salience 14))
            (AniosCursados ?Anios)
            (test (> ?Anios 5))
        =>
            ; Quitamos las dos ramas mas complejas
            (assert (EliminaRama Ingenieria_de_Computadores))
            (assert (EliminaRama Computacion_y_Sistemas_Inteligentes))
)

; Pedimos información sobre la calificacion obtenida.
(defrule Pide_Calificacion
        ?next <- (Pedir_Informacion Calificacion)
        (not (Consejo ? ? ?))
    =>
        (printout t "Que calificacion media tiene?:")
        (assert (Calificacion_Numerica (read)))
        (retract ?next)
)


; Discretiza la nota numérica
(defrule Analiza_Nota
        ?c <- (Calificacion_Numerica ?Nota_Num)
        ; El valor numérico debe estar entre 0.0 y 10.0
        (test 
            (and
                (>= ?Nota_Num 0.0)
                (<= ?Nota_Num 10.0)
            )
        )
    => 
        ; https://www.csie.ntu.edu.tw/~sylee/courses/clips/bpg/node12.6.2.html
        ; Evaluamos la nota numerica a la discretización
        ; Se podría haber realizado diferentes defrule, pero creo que queda más claro así.
        (if (<= ?Nota_Num 5) then
            (bind ?Nota_Val Baja)
            ; Si tiene nota baja, nunca recomendar Computacion y Sistemas_Inteligentes
            (assert (EliminaRama Computacion_y_Sistemas_Inteligentes))
            (assert (EliminaRama Ingenieria_de_Computadores))
        else (if (<= ?Nota_Num 7) then
            (bind ?Nota_Val Media)
        else 
            (bind ?Nota_Val Alta)
        ))
        ; Creamos un nuevo hecho con dicho valor
        (assert (Calificacion ?Nota_Val))
        (printout t "Bien, su nota numerica de " ?Nota_Num " corresponde a " ?Nota_Val crlf)
        ; Eliminamos el valor numérico de los hechos
        (retract ?c)
        ; Pedimos información
        (assert (Pedir_Informacion Interes))
)

; Interés Sobre Hardware o Software
(defrule Pide_Gustos
        ?next <- (Pedir_Informacion Interes)
        (not (Consejo ? ? ?))
    =>
        (printout t "Que te interesa mas, el Software o el Hardware?:")
        ; Convertimos en minusculas
        (bind ?text_interes (lowcase (readline)))
        ; Comprueba que contiene la palabra adecuada, permitiendo así:
        ; "Me gusta (el) hardware", "Me gusta más el (software)"
        ; Esto se podría haber realizado dos reglas y el operador "$?"
        ; Pero así, se permite de forma más fácil que cicle si no se pone nada.
        ; https://www.csie.ntu.edu.tw/~sylee/courses/clips/bpg/node12.3.8.html
        (if (integerp (str-index "hardware" ?text_interes)) then
            (assert (Interes Hardware))
            (printout t "Te gusta 'cacharrear', has elegido Hardware!" crlf)
        else (if (integerp (str-index "software" ?text_interes)) then
            (assert (Interes Software))
            (printout t "Bien, te gusta el Software!" crlf)
        else
            ; Cualquier otra respuesta, ofrece una propuesta
            (assert (HayPropuesta))
        ))
        ; Si pusiera información innecesaria, ignoramos esta pregunta.
        (retract ?next)
)

; Preguntamos por matemáticas
(defrule Pide_Matematicas
        ?next <- (Pedir_Informacion Matematicas)
        (not (Consejo ? ? ?))
    =>
        (printout t "Te gustan las matematicas?:")
        (bind ?SiNo (lowcase (readline)))
        ; Para aceptar más diversidad, "si, me gusta" o "no, las odio", vamos a hacerlo como
        ; con hardware/software
        ; Si dice "me da igual", daremos prioridad a que si le gustan
        (if (integerp (str-index "si" ?SiNo)) then
            (assert (Gustar_Matematicas Si))
        else (if (integerp (str-index "no" ?SiNo)) then
            (assert (Gustar_Matematicas No))
        else
            ; Cualquier otra respuesta, ofrece una propuesta
            (assert (HayPropuesta))
        ))
        ; Si pusiera información innecesaria, ignoramos esta pregunta.
        (retract ?next)
)

; Pedimos el oficio
(defrule Pide_Oficio
        ?next <- (Pedir_Informacion Oficio)
        (not (Consejo ? ? ?))
    =>
        (printout t "Donde te ves trabajando? (Docencia, Empresa, Autonomo):")
        (bind ?Trabajo (lowcase (readline)))
        ; Traducimos lo escrito a un hecho
        (if (integerp (str-index "docencia" ?Trabajo)) then
                (assert (Trabaja Docencia))
                (printout t "Veo que te interesa la Docencia." crlf)
            else (if (integerp (str-index "empresa" ?Trabajo)) then
                (assert (Trabaja Empresa))
                (printout t "Uy! Prefieres una Empresa." crlf)
            else (if (integerp (str-index "autonomo" ?Trabajo)) then
                (assert (Trabaja Autonomo))
                (printout t "Aunque sea dificil, prefieres ser Autonomo" crlf)
            else
                ; Cualquier otra respuesta, ofrece una propuesta
                (assert (HayPropuesta))
        )))
        ; Si pusiera información innecesaria, ignoramos esta pregunta.
        (retract ?next)
)

; Pedimos el Interés por programar
(defrule Pide_Programar
        ?next <- (Pedir_Informacion Programar)
        (not (Consejo ? ? ?))
    =>
        (printout t "Cuanto te gusta programar? Numerico [0-10]:")
        (bind ?Programar (read))

        ; Es numérico, traducimos
        (if (and (>= ?Programar 0) (<= ?Programar 4)) then
                ; [0-4] => Poco
                (assert (Gusta_Programar Poco))
                (printout t "No pasa nada que no te guste programar." crlf)
            else (if (<= ?Programar 7) then
                ; (4-7] => Normal
                (assert (Gusta_Programar Normal))
                (printout t "Parece que te da igual programar o no." crlf)
            else (if (<= ?Programar 10) then
                ; (7-10] => Alto
                (assert (Gusta_Programar Mucho))
                (printout t "Veo que te gusta programar!" crlf)
            else
                ; Cualquier otra respuesta, ofrece una propuesta
                (assert (HayPropuesta))
        )))

        ; Si pusiera información innecesaria, ignoramos esta pregunta.
        (retract ?next)
)

;;;;; SEGÚN EL INTERÉS ;;;;;
;;;;;; Me interesa El software
(defrule Interesa_Software
        (declare (salience 10))
        (Interes Software)
    =>
        ; Quitamos aquellas ramas que no podemos recomendar
        (assert (EliminaRama Ingenieria_de_Computadores))
        (assert (EliminaRama Tecnologias_de_la_Informacion))
        ; Preguntamos por las Matemáticas
        (assert (Pedir_Informacion Matematicas))
)

; Si le gusta las matemáticas y el Software
(defrule Interesa_Software_Gusta_Matematicas
        (declare (salience 11))
        (Interes Software)
        (Gustar_Matematicas Si)
    =>
        ; Quitamos aquellas ramas que no podemos recomendar
        (assert (EliminaRama Sistemas_de_Informacion))
        ; Pedimos información
        (assert (Pedir_Informacion Oficio))
)

; Si le gusta las matemáticas y el Software y la docencia
(defrule Interesa_Software_Gusta_Matematicas_Docencia
        (declare (salience 12))
        (Interes Software)
        (Gustar_Matematicas Si)
        (Trabaja Docencia)
    =>
        ; Quitamos aquellas ramas que no podemos recomendar
        (assert (EliminaRama Ingenieria_del_Software))
)

; Si le gusta las matemáticas y el Software y No docencia
(defrule Interesa_Software_Gusta_Matematicas_No_Docencia
        (declare (salience 12))
        (Interes Software)
        (Gustar_Matematicas Si)
        (Trabaja ~Docencia)
    =>
        ; Quitamos aquellas ramas que no podemos recomendar
        (assert (EliminaRama Computacion_y_Sistemas_Inteligentes))
)


; Si NO le gusta las matemáticas y el Software
(defrule Interesa_Software_No_Gusta_Matematicas
        (declare (salience 11))
        (Interes Software)
        (Gustar_Matematicas No)
    =>
        ; Quitamos aquellas ramas que no podemos recomendar
        (assert (EliminaRama Computacion_y_Sistemas_Inteligentes))
        (assert (EliminaRama Ingenieria_del_Software))
)


;;;;;; Interesa El hardware
(defrule Interesa_Hardware
        (declare (salience 10))
        (Interes Hardware)
    => 
        ; Quitamos aquellas ramas que no podemos recomendar
        (assert (EliminaRama Computacion_y_Sistemas_Inteligentes))
        (assert (EliminaRama Ingenieria_del_Software))
        ; Preguntamos por la programación
        (assert (Pedir_Informacion Programar))
)

; Si le gusta el hardware y no le gusta programar, Recomendamos Computadores.
(defrule Interesa_Hardware_Programar_Poco
        (declare (salience 11))
        (Interes Hardware)
        (Gusta_Programar Poco)
    =>
        ; Quitamos aquellas ramas que no podemos recomendar
        (assert (EliminaRama Tecnologias_de_la_Informacion))
        (assert (EliminaRama Sistemas_de_Informacion))
)

; Si le gusta el hardware y programa normal
(defrule Interesa_Hardware_Programar_Mas_Poco
        (declare (salience 11))
        (Interes Hardware)
        (or (Gusta_Programar Normal) (Gusta_Programar Mucho))
    =>
        ; Quitamos aquellas ramas que no podemos recomendar
        (assert (EliminaRama Ingenieria_de_Computadores))
        ; Preguntamos por las Matemáticas
        (assert (Pedir_Informacion Matematicas))
)

; Si le gusta el hardware y programar normal y no le gusta las matemáticas
(defrule Interesa_Hardware_Programar_Mas_Poco_No_Matematicas
        (declare (salience 12))
        (Interes Hardware)
        (or (Gusta_Programar Normal) (Gusta_Programar Mucho))
        (Gustar_Matematicas No)
    =>
        ; Quitamos aquellas ramas que no podemos recomendar
        (assert (EliminaRama Tecnologias_de_la_Informacion))
)

; Si le gusta el hardware y programar normal y si le gusta las matemáticas
(defrule Interesa_Hardware_Programar_Mas_Poco_Si_Matematicas
        (declare (salience 12))
        (Interes Hardware)
        (or (Gusta_Programar Normal) (Gusta_Programar Mucho))
        (Gustar_Matematicas Si)
    =>
        ; Quitamos aquellas ramas que no podemos recomendar
        (assert (EliminaRama Sistemas_de_Informacion))
)

;;;;; Recomienda

; Recomienda Computacion y Sistemas Inteligentes
(defrule Recomienda_Computacion_y_Sistemas_Inteligentes
		(declare (salience 999))
        ?p <- (HayPropuesta)
        ?r <- (Rama Computacion_y_Sistemas_Inteligentes)
    =>
        (assert (Consejo Computacion_y_Sistemas_Inteligentes "Te gusta la docencia y las matemáticas, además del Software." ?*NOMBREEXPERTO*))
        (retract ?r)
        (retract ?p)
)

; Recomienda Ingenieria del Software
(defrule Recomienda_Ingenieria_del_Software
		(declare (salience 999))
        ?p <- (HayPropuesta)
        ?r <- (Rama Ingenieria_del_Software)
    =>
        (assert (Consejo Ingenieria_del_Software "El Software te apasiona, las matemáticas te gusta, pero no la docencia." ?*NOMBREEXPERTO*))
        (retract ?r)
        (retract ?p)
)

; Recomienda Ingenieria de Computadores
(defrule Recomienda_Ingenieria_de_Computadores
		(declare (salience 999))
        ?p <- (HayPropuesta)
        ?r <- (Rama Ingenieria_de_Computadores)
    =>
        (assert (Consejo Ingenieria_de_Computadores "Te apasiona el Hardware, por lo que no te gusta programar." ?*NOMBREEXPERTO*))
        (retract ?r)
        (retract ?p)
)

; Recomienda Sistemas de Informacion
(defrule Recomienda_Sistemas_de_Informacion
		(declare (salience 999))
        ?p <- (HayPropuesta)
        ?r <- (Rama Sistemas_de_Informacion)
    =>
        (assert (Consejo Sistemas_de_Informacion "Te gusta el Software y el Hardware, te gusta trabajar para ti o una empresa, programar no te disgusta." ?*NOMBREEXPERTO*))
        (retract ?r)
        (retract ?p)
)

; Recomienda Tecnologias de la Informacion
(defrule Recomienda_Tecnologias_de_la_Informacion
		(declare (salience 999))
        ?p <- (HayPropuesta)
        ?r <- (Rama Tecnologias_de_la_Informacion)
    =>
        (assert (Consejo Tecnologias_de_la_Informacion "Te gusta el hardware y programar también." ?*NOMBREEXPERTO*))
        (retract ?r)
        (retract ?p)
)

; Si hay consejo, se muestra el motivo por pantalla
(defrule HayConsejo
		(declare (salience 1000))
        (Consejo ?Rama ?Texto ?Nombre)
    =>
        ; https://www.csie.ntu.edu.tw/~sylee/courses/clips/bpg/node12.6.9.html
        ; Mostramos un texto
        (printout t ?Nombre ": " crlf " Se recomienda la rama '" ?Rama "', ya que " crlf ?Texto crlf)
)