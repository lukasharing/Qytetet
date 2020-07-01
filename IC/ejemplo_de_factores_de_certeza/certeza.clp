;;;;;;; Ejemplo de Factores de Certeza ;;;;;;;
;;;;;;; LUKAS HARING GARCÍA ;;;;;;;

; Main 



; (FactorCerteza ?h si|no ?f)
; ?h
; problema_starter
; problema_bujias
; problema_bateria
; motor_llega_gasolina
; ?f -> Valor de certeza

; (Evidencia ?e si|no)
; ?e
; hace_intentos_arrancar
; hay_gasolina_en_deposito
; encienden_las_luces
; gira_motor

(defrule certeza_evidencias
        (declare (salience 1))
        ?ev <- (Evidencia ?e ?r)
    =>
        (assert (FactorCerteza ?r ?e 1))
        (retract ?ev)
)


(deffunction encadenado(?fc_antecedente ?fc_regla)
    (if (> ?fc_antecedente 0) then
        (bind ?rv (* ?fc_antecedente ?fc_regla)) 
    else
        (bind ?rv 0)
    )
    ?rv
)

(deffunction combinacion(?fc1 ?fc2)
    (if (and (> ?fc1 0) (> ?fc2 0)) then
        (bind ?rv(-(+ ?fc1 ?fc2) (* ?fc1 ?fc2)))
    else (if (and (< ?fc1 0) (< ?fc2 0) ) then
        (bind ?rv(+ (+ ?fc1 ?fc2) (* ?fc1 ?fc2)))
    else
        (bind ?rv(/ (+ ?fc1 ?fc2) (- 1  (min (abs ?fc1) (abs ?fc2))))))
    )
    ?rv
)

;R1: SI el motor obtiene gasolina Y  el motor gira ENTONCES problemas
; con las bujías con certeza 0,7
(defrule R1
        (FactorCerteza motor_llega_gasolina si ?f1)
        (FactorCerteza gira_motor si ?f2)
    =>
        (bind ?ecn (encadenado (* ?f1 ?f2) 0.7))
        (assert (FactorCerteza problema_bujias si ?ecn))
)

; R2: SI NO gira el motor  ENTONCES problema con el starter con certeza 0,8
(defrule R2
        (FactorCerteza gira_motor no ?f1)
        (test (> ?f1 0))
    =>
        (assert (FactorCerteza problema_starter si 0.8)))
)

; R3: SI NO encienden las luces ENTONCESproblemas con la batería con certeza 0,9
(defrule R3
        (FactorCerteza encienden_las_luces no ?f1)
        (test (> ?f1 0))
    =>
        (assert (FactorCerteza problema_bateria si 0.9)))
)

; R4: SI hay gasolina en el deposito ENTONCESel motor obtiene gasolina con certeza 0,9
(defrule R4
        (FactorCerteza hay_gasolina_en_deposito si ?f1)
        (test (> ?f1 0))
    =>
        (assert (FactorCerteza motor_llega_gasolina si 0.9)))
)

; R5: SI hace intentos de arrancar ENTONCES problema con el starter con certeza -0,6
(defrule R5
        (FactorCerteza hace_intentos_arrancar si ?f1)
        (test (> ?f1 0))
    =>
        (assert (FactorCerteza problema_starter si -0.6)))
)

; R6: SI hace intentos de arrancar ENTONCES problema con la batería 0,5
(defrule R6
        (FactorCerteza hace_intentos_arrancar si ?f1)
    =>
        (assert (FactorCerteza problema_bateria si 0.5)))
)

;;;;;;  Combinar misma deduccionpor distintos caminos
(defrule combinar
        (declare (salience 1))
        ?f <- (FactorCerteza ?h ?r ?fc1)
        ?g <- (FactorCerteza ?h ?r ?fc2)
        (test (neq ?fc1 ?fc2))
    =>
        (retract ?f ?g)
        (printout t ?fc1 " - " ?fc2 crlf)
        (bind ?cmb (combinacion ?fc1 ?fc2))
        (assert (FactorCerteza ?h ?r ?fc2))
)

;;;;;; Combinar Deducciones en Positivo y Negativo
(defrule combinar_signo
        (declare (salience 2))
        (FactorCerteza ?h si ?fc1)
        (FactorCerteza ?h no ?fc2)
    =>
        (bind ?dfc (- ?fc1 ?fc2))
        (assert (Certeza ?h ?dfc))
)

;;;;; ACABAR
; 1 -> Preguntar por las posibles evidencias.
; 2 -> Añadir el resto de Reglas.
; 3 -> Razonar quedarse con la hipótesis de mayor certeza.
; 4 -> Añadir o modificar las reglas para que el sistema explique el por qué de las afirmaciones.

(defrule Main
    =>
    (assert (Ask))
)

(defrule Menu
        ?a <- (Ask)
    =>
        (retract ?a)

        (printout t "Introduzca (si/no) seguido de una de las siguientes evidencias." crlf)
        (printout t "- Hace intentos de arrancar" crlf)
        (printout t "- Hay gasolina en el deposito" crlf)
        (printout t "- Encienden las luces" crlf)
        (printout t "- Gira el motor" crlf)
        (printout t "+ Ninguna mas" crlf)
        (printout t "Escriba una de las evidencias: ")

        (bind ?selection (explode$ (lowcase (readline))))
        (assert (Respuesta ?selection))
)

; Options
; 1.
(defrule Hace_intentos_de_arrancar
        (declare (salience 1))
        ?r <- (Respuesta ?yesno hace intentos de arrancar)
        (test (or (eq ?yesno si) (eq ?yesno no)))
    =>
        (retract ?r)
        (assert (Evidencia ?yesno hace_intentos_arrancar))
        (assert (Ask)) ; Keep Asking
)

; 2.
(defrule Hay_gasolina_en_el_deposito
        (declare (salience 1))
        ?r <- (Respuesta ?yesno hay gasolina en el deposito)
        (test (or (eq ?yesno si) (eq ?yesno no)))
    =>
        (retract ?r)
        (assert (Evidencia ?yesno hay_gasolina_en_deposito))
        (assert (Ask)) ; Keep Asking
)

;3.
(defrule Encienden_las_luces
        (declare (salience 1))
        ?r <- (Respuesta ?yesno encienden las luces)
        (test (or (eq ?yesno si) (eq ?yesno no)))
    =>
        (retract ?r)
        (assert (Evidencia ?yesno encienden_las_luces))
        (assert (Ask)) ; Keep Asking
)

; 4.
(defrule Gira_Motor
        (declare (salience 1))
        ?r <- (Respuesta ?yesno gira el motor)
        (test (or (eq ?yesno si) (eq ?yesno no)))
    =>
        (retract ?r)
        (assert (Evidencia ?yesno gira_motor))
        (assert (Ask)) ; Keep Asking
)

; 5. End
(defrule Ninguna_Mas
        (declare (salience 1))
        ?r <- (Respuesta ninguna mas)
    =>
        (retract ?r)
        (assert (Resolver))
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

(defrule Resolver
        (declare (salience 100))
        ?a <- (Resolver)

        ; Height Player N Maximum
        (FactorCerteza ?p1&problema_starter|problema_bujias|problema_bateria ?yesno1 ?v1)

        ; There doesnt exist another with highest Height
        (not (FactorCerteza ?p2&problema_starter|problema_bujias|problema_bateria ? ?v2&:(> ?v2 ?v1)))
    =>
        ; Eliminamos Resolucion
        (retract ?a)
        ; Buscamos el máximo
        (printout t "Su coche tiene el problema de " ?yesno1 " " ?p1 "  con una confianza del " ?v1 crlf)
)