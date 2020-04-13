;;;;;;; JUGADOR DE 3 en RAYA ;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 
;;;;;;;;;; Version de 3 en raya clásico: fichas que se pueden poner libremente en cualquier posicion libre (i,j) con 0 < i,j < 4
;;;;;;;;;;;;;;;;;;;;;;; y cuando se han puesto las 3 fichas las jugadas consisten en desplazar una ficha propia 
;;;;;;;;;;;;;;;;;;;;;;; de la posición en que se encuentra (i,j) a una contigua
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;; Hechos para representar un estado del juego

;;;;;;; (Turno X|O)   representa a quien corresponde el turno (X maquina, O jugador)
;;;;;;; (Posicion ?i ?j " "|X|O) representa que la posicion i,j del tablero esta vacia, o tiene una ficha de Clisp o tiene una ficha del contrincante

;;;;;;;;;;;;;;;; Hechos para representar una jugadas

;;;;;;; (Juega X|O ?origen_i ?origen_j ?destino_i ?destino_j) representa que la jugada consiste en desplazar la ficha de la posicion 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; (?origen_i,?origen_j) a la posición (?destino_i,?destino_j)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; las fichas que se ponen inicialmente se supondrá que están en el posición (0,0)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; INICIALIZAR ESTADO

(deffacts Tablero
    (Conectado 1 a horizontal 1 b)
    (Conectado 1 b horizontal 1 c)
    (Conectado 2 a horizontal 2 b)
    (Conectado 2 b horizontal 2 c)
    (Conectado 3 a horizontal 3 b)
    (Conectado 3 b horizontal 3 c)
    (Conectado 1 a vertical 2 a)
    (Conectado 2 a vertical 3 a)
    (Conectado 1 b vertical 2 b)
    (Conectado 2 b vertical 3 b)
    (Conectado 1 c vertical 2 c)
    (Conectado 2 c vertical 3 c)
    (Conectado 1 a diagonal 2 b)
    (Conectado 2 b diagonal 3 c)
    (Conectado 1 c diagonal_inversa 2 b)
    (Conectado 2 b diagonal_inversa 3 a)
)

(deffacts Estado_inicial
    (Posicion 1 a " ")
    (Posicion 1 b " ")
    (Posicion 1 c " ")
    (Posicion 2 a " ")
    (Posicion 2 b " ")
    (Posicion 2 c " ")
    (Posicion 3 a " ")
    (Posicion 3 b " ")
    (Posicion 3 c " ")
    (Fichas_sin_colocar O 3)
    (Fichas_sin_colocar X 3)
)

(defrule Conectado_es_simetrica
        (declare (salience 1))
        (Conectado ?i ?j ?forma ?i1 ?j1)
    =>
        (assert (Conectado ?i1 ?j1 ?forma ?i ?j))
)

;;;;;;; LUKAS HÄRING GARCÍA ;;;;;;;;
; Conectado A -> B implica que estos sean adjacentes, de distancia 1.
(defrule Posicion_Enlinea1
        (declare (salience 1))
		(Conectado ?f1 ?c1 ?forma ?f2 ?c2)
    =>
        (assert (Enlinea ?forma ?f1 ?c1 ?f2 ?c2))
)

; Como tenemos una distancia finita 3x3, bastaría con ver si existe una celda intermedia
; Con la misma dirección y adyacentes a ambas
(defrule Posicion_Enlinea2
        (declare (salience 1))
		; Cogemos 2 Celdas adyacentes A, B Con Dirección D.
		(Conectado ?f1 ?c1 ?forma ?f2 ?c2)
		; Cogemos las celdas adyacentes B Con Dirección D.
		(Conectado ?f2 ?c2 ?forma ?f3 ?c3)
		; Evitamos Ciclos A -> B -> A
		(test (not (and (eq ?f3 ?f1) (eq ?c3 ?c1))))
    =>
		; Decimos que A y C Están en línea 
        (assert (Enlinea ?forma ?f1 ?c1 ?f3 ?c3))
)

; Bastaría con coger la regla anteriormente creada y ver si en dichas posiciones, tenemos
; marcado con el mismo jugador
(defrule Posicion_2_Enlinea
		(declare (salience 2))
		; Buscamos aquellas dos fichas del mismo jugador, que no esté vacío
		; Añadimos logical por si se altera cuando se realiza un movimiento
		(logical (Posicion ?f1 ?c1 ?ficha & ~" ") (Posicion ?f2 ?c2 ?ficha))
		; Comprobamos que estas estén en línea
		(Enlinea ?forma ?f1 ?c1 ?f2 ?c2)
	=>
		(assert (2_en_linea ?forma ?f1 ?c1 ?f2 ?c2 ?ficha))
)

; Puede Ganar (2 Formas)
(defrule Puede_Ganar_Mano
		(declare (salience 2))
		; Logical por si estos hechos son modificados
		(logical
			; Obetenemos las posiciones que hacen línea
			(2_en_linea ?forma ?f1 ?c1 ?f2 ?c2 ?ficha)
			; Tiene una ficha en la mano
			(Fichas_sin_colocar ?ficha 1)
			; Miramos todas aquellas celdas vacía
			(Posicion ?f3 ?c3 " ")
		)
		; Obtenemos de las celdas vacías, aquella celda que hace linea ganadora (f3, c3)
		(Enlinea ?forma ?f2 ?c2 ?f3 ?c3)
	=>
		; Devolvemos -1, Para indicar que es de la mano.
		(assert (Puede_ganar -1 -1 ?f3 ?c3 ?ficha))
)

; Comprobamos que puede ganar realizando un movimiento
(defrule Puede_Ganar_Movimiento
		(declare (salience 2))
		; Logical por si estos hechos son modificados
		(logical
			; Obetenemos las posiciones que hacen línea
			(2_en_linea ?forma ?f1 ?c1 ?f2 ?c2 ?ficha)
			; Miramos todas aquellas celdas vacía
			(Posicion ?f3 ?c3 " ")
			; Buscamos Las posiciones de todas las celdas del jugador
			(Posicion ?f4 ?c4 ?ficha)
		)
		; Obtenemos de las celdas vacías, aquella celda que hace linea ganadora (f3, c3)
		(Enlinea ?forma ?f2 ?c2 ?f3 ?c3)
		; Cogemos aquella celda recomendada a mover (Si A -> B => C / C != A, C != B)
		(test
			(not
				(or
					(and (eq ?f4 ?f1) (eq ?c4 ?c1))
					(and (eq ?f4 ?f2) (eq ?c4 ?c2))
				)
			)
		)
		; Miramos si hay conexión adyacente para hacer línea, independientemente de la dirección
		(Conectado ?f4 ?c4 ? ?f3 ?c3)
	=>
		(assert (Puede_ganar ?f4 ?c4 ?f3 ?c3 ?ficha))
)
;;; BOT INTENTA GANAR
; Si el bot tiene fichas en la mano, dará prioridad a donde pueda ganar.
(defrule clisp_juega_con_criterio_fichas_sin_colocar
		(declare (salience -1))
		?f<- (Turno X)
		(Fichas_sin_colocar X ?n)
		; Si existe una posibilidad de ganar, la ejecutamos
		(Puede_ganar ?f1 ?c1 ?f2 ?c2 X)
		?g<- (Posicion ?f2 ?c2 " ")
	=>
		(printout t "Juego poner ficha en " ?f2 ?c2 crlf)
		(retract ?f ?g)
		(assert (Posicion ?f2 ?c2 X) (Turno O) (reducir_fichas_sin_colocar X))
)

; Si el bot tiene todas las fichas en la mesa, buscará el movimiento a ganar
(defrule clisp_juega_con_criterio
		(declare (salience -1))
		?f<- (Turno X)
		(Todas_fichas_en_tablero X)
		; Si existe una posibilidad de ganar, la ejecutamos
		(Puede_ganar ?f1 ?c1 ?f2 ?c2 X)
	=>
		(assert (Juega X ?f1 ?c1 ?f2 ?c2))
		(printout t "Juego mover la ficha de "  ?f1 ?c1 " a " ?f2 ?c2 crlf)
		(retract ?f)
)
;;; BOT EVITA GANAR AL HUMANO
; Si el bot no puede ganar, dará prioridad a tapar al contrincante
(defrule clisp_juega_con_criterio_evita_ganar
		; Tiene menos prioridad para que sea prioritario ganar.
		(declare (salience -2))
		?f<- (Turno X)
		(Todas_fichas_en_tablero X)
		; Si existe una posibilidad de ganar, la ejecutamos
		(Puede_ganar ?f1 ?c1 ?f2 ?c2 O)
		; Existe una ficha del bot
		(Posicion ?f3 ?c3 X)
		; Con connexión a (f2, c2)
		(Conectado ?f3 ?c3 ? ?f2 ?c2)
	=>
		(assert (Juega X ?f3 ?c3 ?f2 ?c2))
		(printout t "Juego mover la ficha de "  ?f3 ?c3 " a " ?f2 ?c2 crlf)
		(retract ?f)
)

; Si el bot tiene fichas en la mano, dará prioridad a bloquear a enemigo.
(defrule clisp_juega_con_criterio_fichas_sin_colocar_evita_ganar
		(declare (salience -2))
		?f<- (Turno X)
		(Fichas_sin_colocar X ?n)
		; Si existe una posibilidad de ganar, la ejecutamos
		(Puede_ganar ?f1 ?c1 ?f2 ?c2 O)
		?g<- (Posicion ?f2 ?c2 " ")
	=>
		(printout t "Juego poner ficha en " ?f2 ?c2 crlf)
		(retract ?f ?g)
		(assert (Posicion ?f2 ?c2 X) (Turno O) (reducir_fichas_sin_colocar X))
)

;;;;;;;;;;;;;;; FIN LUKAS HÄRING GARCÍA ;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;

(defrule Elige_quien_comienza
    =>
        (printout t "Quien quieres que empieze: (escribre X para la maquina u O para empezar tu) ")
        (assert (Turno (read)))
)

;;;;;;;;;;;;;;;;;;;;;;; RECOGER JUGADA DEL CONTRARIO ;;;;;;;;;;;;;;;;;;;;;;;
(defrule muestra_posicion
        (declare (salience 1))
        (muestra_posicion)
        (Posicion 1 a ?p11)
        (Posicion 1 b ?p12)
        (Posicion 1 c ?p13)
        (Posicion 2 a ?p21)
        (Posicion 2 b ?p22)
        (Posicion 2 c ?p23)
        (Posicion 3 a ?p31)
        (Posicion 3 b ?p32)
        (Posicion 3 c ?p33)
    =>
        (printout t crlf)
        (printout t "   a      b      c" crlf)
        (printout t "   -      -      -" crlf)
        (printout t "1 |" ?p11 "| -- |" ?p12 "| -- |" ?p13 "|" crlf)
        (printout t "   -      -      -" crlf)
        (printout t "   |  \\   |   /  |" crlf)
        (printout t "   -      -      -" crlf)
        (printout t "2 |" ?p21 "| -- |" ?p22 "| -- |" ?p23 "|" crlf)
        (printout t "   -      -      -" crlf)
        (printout t "   |   /  |  \\   |" crlf)
        (printout t "   -      -      -" crlf)
        (printout t "3 |" ?p31 "| -- |" ?p32 "| -- |" ?p33 "|"crlf)
        (printout t "   -      -      -" crlf)
)

(defrule muestra_posicion_turno_jugador
        (declare (salience 10))
        (Turno O)
    =>
        (assert (muestra_posicion))
)
 
(defrule jugada_contrario_fichas_sin_colocar
        ?f <- (Turno O)
        (Fichas_sin_colocar O ?n)
    =>
        (printout t "en que posicion colocas la siguiente ficha" crlf)
        (printout t "escribe la fila (1,2 o 3): ")
        (bind ?fila (read))
        (printout t "escribe la columna (a,b o c): ")
        (bind ?columna (read))
        (assert (Juega O 0 0 ?fila ?columna))
        (retract ?f)
)

(defrule juega_contrario_fichas_sin_colocar_check
        (declare (salience 1))
        ?f <- (Juega O 0 0 ?i ?j)
        (not (Posicion ?i ?j " "))
    =>
        (printout t "No puedes jugar en " ?i ?j " porque no esta vacio" crlf)
        (retract ?f)
        (assert (Turno O))
)

(defrule juega_contrario_fichas_sin_colocar_actualiza_estado
        ?f <- (Juega O 0 0 ?i ?j)
        ?g <- (Posicion ?i ?j " ")
    =>
        (retract ?f ?g)
        (assert (Turno X) (Posicion ?i ?j O) (reducir_fichas_sin_colocar O))
)

(defrule reducir_fichas_sin_colocar
        (declare (salience 1))
        ?f <- (reducir_fichas_sin_colocar ?jugador)
        ?g <- (Fichas_sin_colocar ?jugador ?n)
    =>
        (retract ?f ?g)
        (assert (Fichas_sin_colocar ?jugador (- ?n 1)))
)

(defrule todas_las_fichas_en_tablero
        (declare (salience 1))
        ?f <- (Fichas_sin_colocar ?jugador 0)
    =>
        (retract ?f)
        (assert (Todas_fichas_en_tablero ?jugador))
)

(defrule juega_contrario
        ?f <- (Turno O)
        (Todas_fichas_en_tablero O)
    =>
        (printout t "en que posicion esta la ficha que quieres mover?" crlf)
        (printout t "escribe la fila (1,2,o 3): ")
        (bind ?origen_i (read))
        (printout t "escribe la columna (a,b o c): ")
        (bind ?origen_j (read))
        (printout t "a que posicion la quieres mover?" crlf)
        (printout t "escribe la fila (1,2,o 3): ")
        (bind ?destino_i (read))
        (printout t "escribe la columna (a,b o c): ")
        (bind ?destino_j (read))
        (assert (Juega O ?origen_i ?origen_j ?destino_i ?destino_j))
        (printout t "Juegas mover la ficha de "  ?origen_i ?origen_j " a " ?destino_i ?destino_j crlf)
        (retract ?f)
)

(defrule juega_contrario_check_mueve_ficha_propia
        (declare (salience 1))
        ?f <- (Juega O ?origen_i ?origen_j ?destino_i ?destino_j)
        (Posicion ?origen_i ?origen_j ?X)
        (test (neq O ?X))
    =>
        (printout t "No es jugada valida porque en " ?origen_i ?origen_j " no hay una ficha tuya" crlf)
        (retract ?f)
        (assert (Turno O))
)

(defrule juega_contrario_check_mueve_a_posicion_libre
        (declare (salience 1))
        ?f <- (Juega O ?origen_i ?origen_j ?destino_i ?destino_j)
        (Posicion ?destino_i ?destino_j ?X)
        (test (neq " " ?X))
    =>
        (printout t "No es jugada valida porque " ?destino_i ?destino_j " no esta libre" crlf)
        (retract ?f)
        (assert (Turno O))
)

(defrule juega_contrario_check_conectado
        (declare (salience 1))
        (Todas_fichas_en_tablero O)
        ?f <- (Juega O ?origen_i ?origen_j ?destino_i ?destino_j)
        (not (Conectado ?origen_i ?origen_j ? ?destino_i ?destino_j))
    =>
        (printout t "No es jugada valida porque "  ?origen_i ?origen_j " no esta conectado con " ?destino_i ?destino_j crlf)
        (retract ?f)
        (assert (Turno O))
)

(defrule juega_contrario_actualiza_estado
        ?f <- (Juega O ?origen_i ?origen_j ?destino_i ?destino_j)
        ?h <- (Posicion ?origen_i ?origen_j O)
        ?g <- (Posicion ?destino_i ?destino_j " ")
    =>
  		(retract ?f ?g ?h)
        (assert (Turno X) (Posicion ?destino_i ?destino_j O) (Posicion ?origen_i ?origen_j " ") )
)



;;;;;;;;;;; ACTUALIZAR  ESTADO TRAS JUGADA DE CLISP ;;;;;;;;;;;;;;;;;;

(defrule juega_clisp_actualiza_estado
		?f <- (Juega X ?origen_i ?origen_j ?destino_i ?destino_j)
		?h <- (Posicion ?origen_i ?origen_j X)
		?g <- (Posicion ?destino_i ?destino_j " ")
	=>
		(retract ?f ?g ?h)
		(assert (Turno O) (Posicion ?destino_i ?destino_j X) (Posicion ?origen_i ?origen_j " ") )
)


;;;;;;;;;;; CLISP JUEGA SIN CRITERIO ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defrule clisp_juega_sin_criterio_fichas_sin_colocar
		(declare (salience -9999))
		?f<- (Turno X)
		(Fichas_sin_colocar X ?n)
		?g<- (Posicion ?i ?j " ")
	=>
		(printout t "Juego poner ficha en " ?i ?j crlf)
		(retract ?f ?g)
		(assert (Posicion ?i ?j X) (Turno O) (reducir_fichas_sin_colocar X))
)

(defrule clisp_juega_sin_criterio
		(declare (salience -9999))
		?f<- (Turno X)
		(Todas_fichas_en_tablero X)
		(Posicion ?origen_i ?origen_j X)
		(Posicion ?destino_i ?destino_j " ")
		(Conectado ?origen_i ?origen_j ? ?destino_i ?destino_j)
	=>
		(assert (Juega X ?origen_i ?origen_j ?destino_i ?destino_j))
		(printout t "Juego mover la ficha de "  ?origen_i ?origen_j " a " ?destino_i ?destino_j crlf)
		(retract ?f)
)

(defrule tres_en_raya
		(declare (salience 9999))
		?f <- (Turno ?X)
		(Posicion ?i1 ?j1 ?jugador)
		(Posicion ?i2 ?j2 ?jugador)
		(Posicion ?i3 ?j3 ?jugador)
		(Conectado ?i1 ?j1 ?forma ?i2 ?j2)
		(Conectado ?i2 ?j2 ?forma ?i3 ?j3)
		(test (neq ?jugador " "))
		(test (or (neq ?i1 ?i3) (neq ?j1 ?j3)))
	=>
		(printout t ?jugador " ha ganado pues tiene tres en raya " ?i1 ?j1 " " ?i2 ?j2 " " ?i3 ?j3 crlf)
		(retract ?f)
		(assert (muestra_posicion))
) 


