;;;;;;; SISTEMA EXPERTO ;;;;;;;
;;;;;;; LUKAS HARING GARCÍA ;;;;;;;

(defmodule Recomendador_Asignaturas)
    ; Nombre | Siglas | Tipo | Matematicas | Hardware | Software
    (deffacts Asignaturas
        ; Primero
        (Asignatura Algebra_Lineal                         ALEM 1); troncal 10 0  5 )
        (Asignatura Calculo                                CA   1); troncal 10 0  5 )
        (Asignatura Fundamentos_Fisicos_Y_Tecnologicos     FFT  1); troncal 8  8  0 )
        (Asignatura Fundamentos_del_Software               FS   1); troncal 1  2  8 )
        (Asignatura Fundamentos_de_programacion            FP   1); troncal 1  0  10)
        
        (Asignatura Logica_Metodos_Discretos               LMD  1); troncal 10 0 5 )
        (Asignatura Estadistica                            ES   1); troncal 9  0 3 )
        (Asignatura Tecnologia_Organiazacion_Computadores  TOC  1); troncal 7  9 0 )
        (Asignatura Metodologia_de_la_Programacion         MP   1); troncal 5  0 10)
        (Asignatura Ingenieria_Empresa_Sociedad            IES  1); troncal 0  0 0 )

        ; Segundo
        (Asignatura Estructura_Computadores                EC   2); obligatoria 3  10 5 )
        (Asignatura Estructura_de_Datos                    ED   2); obligatoria 7  0  9 )
        (Asignatura Sistema_Operativos                     SO   2); obligatoria 6  3  8 )
        (Asignatura Programacion_Diseno_Orientado_Objetos  PDOO 2); obligatoria 1  0  10)
        (Asignatura Sistema_Concurrente_Distribuidos       SCD  2); obligatoria 4  7  8 )

        (Asignatura Arquitectura_de_Computadores           AC   2); obligatoria 4  10 5 )
        (Asignatura Algoritmica                            ALG  2); obligatoria 7  3  8 )
        (Asignatura Inteligencia_Artificial                IA   2); obligatoria 6  0  8 )
        (Asignatura Fundamentos_de_Base_de_Datos           FBD  2); obligatoria 4  6  6 )
        (Asignatura Fundamentos_de_Ingeniera_del_Software  FIS  2); obligatoria 2  0  7 )

        ; Tercero
        (Asignatura Ingenieria_de_Servidores               ISE  3); obligatoria 4  9  0 )
        (Asignatura Fundamentos_de_Redes                   FR   3); obligatoria 6  8  3 )
        (Asignatura Modelos_de_Computacion                 MC   3); obligatoria 7  3  0 )
        (Asignatura Informacion_Grafica                    IG   3); obligatoria 5  0  8 )
        (Asignatura Diseno_Desarrollo_Sistemas_Informacion DDSI 3); obligatoria 0  0  5 )
        
        ; Optativas Tercero 
        ; Computación y Sistemas Inteligentes
        ;(Asignatura Aprendizaje_Automatico                 AA   3); optativa 9  0  7 )
        ;(Asignatura Metaheuristicas                        MH   3); optativa 7  0  9 )
        ;(Asignatura Modelos_Avanzados_de_Computacion       MCA  3); optativa 10 0  2 )
        ;(Asignatura Tecnicas_de_los_Sistemas_Inteligentes  TSI  3); optativa 7  0  8 )
        ;(Asignatura Ingeniera_Conociminento                IC   3); optativa 6  0  8 )
        ; Especialidad en Ingeniería de Computadores
        ;(Asignatura Desarrollo_de_Hardware_Digital         DHD  3); optativa 6  8  6 )
        ;(Asignatura Sistemas_con_Microprocesadores         SMP  3); optativa 4  10 3 )
        ;(Asignatura Arquitectura_de_Sistemas               AS   3); optativa 5  10 4 )
        ;(Asignatura Arquitectura_Computacion_Prestaciones  ACAP 3); optativa 3  9  3 )
        ;(Asignatura Diseno_Sistemas_Electronicos           DSE  3); optativa 4  10 2 )
        ; Especialidad en Ingeniería del Software
        ;(Asignatura Desarrollo_del_Software                DS   3); optativa 4  0 10)
        ;(Asignatura Sistemas_Graficos                      SG   3); optativa 5  0 9 )
        ;(Asignatura Desarrollo_Sistemas_Distribuidos       DSD  3); optativa 6  7 6 ) 
        ;(Asignatura Diseno_de_Interfaces_de_Usuario        DIU  3); optativa 2  0 8 ) 
        ;(Asignatura Sistemas_Informacion_Basado_Web        SIBW 3); optativa 0  0 8 )
        ; Especialidad en Sistemas de Información
        ;(Asignatura Sistemas_de_Informacion_para_Empresas  SIE  3); optativa 0 0 8 )
        ;(Asignatura Programacion_Web                       PW   3); optativa 1 0 9 )
        ;(Asignatura Sistemas_Multidimensionales            SMD  3); optativa 3 5 7 )
        ;(Asignatura Ingenieria_de_Sistemas_de_Informacion  ISI  3); optativa 2 6 8 )
        ;(Asignatura Administracion_de_Base_de_Datos        ABD  3); optativa 3 4 6 )
        ; Especialidad en Tecnologías de la Información
        ;(Asignatura Transmision_de_Datos_y_Redes           TDRC 3); optativa 4 7 3 )
        ;(Asignatura Servidores_Web_De_Altas_Prestaciones   SWAP 3); optativa 5 5 7 )
        ;(Asignatura Tecnologias_Web                        TW   3); optativa 3 0 8 )
        ;(Asignatura Sistemas_Multimedia                    SMM  3); optativa 4 5 7 )
        ;(Asignatura Computacion_Ubicua_I_Ambiental         CUIA 3); optativa 1 6 7 )
    )

    ; Certeza
    (defrule certeza_evidencias
            (declare (salience 100))
            ?ev <- (Evidencia ?e ?r)
        =>
            (assert (FactorCerteza ?e ?r 1))
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

    ; Si te gustan las matematicas, entonces algebra lineal
    (defrule r1
            (FactorCerteza gusta_matematicas si ?f1)
            (test (> ?f1 0))
            
        =>
            (assert (FactorCerteza ALEM si (encadenado ?f1 1.0)))
    )

    ; Si te gustan las matematicas, entonces calculo
    (defrule r2
            (FactorCerteza gusta_matematicas si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza CA si (encadenado ?f1 1.0)))
    )

    ; Si te gustan las matematicas y el hardware, entonces Fundamentos_Fisicos_Y_Tecnologicos a 0.8 de certeza
    (defrule r3
            (FactorCerteza gusta_matematicas si ?f1)
            (FactorCerteza gusta_hardware si ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza FFT si (encadenado (* ?f1 ?f2) 0.8)))
    )

    ; Si te gustan el software, entonces Fundamentos_del_Software
    (defrule r4
            (FactorCerteza gusta_software si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza FFT si (encadenado ?f1 1.0)))
    )

    ; Si te gustan el software, entonces Fundamentos_del_Software
    (defrule r5
            (FactorCerteza gusta_software si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza FP si (encadenado ?f1 1.0)))
    )

    ; Si te gustan las matemáticas, entonces Logica_Metodos_Discretos
    (defrule r6
            (FactorCerteza gusta_matematicas si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza LMD si (encadenado ?f1 1.0)))
    )

    ; Si te gustan las matemáticas, entonces Estadistica
    (defrule r7
            (FactorCerteza gusta_matematicas si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza ES si (encadenado ?f1 1.0)))
    )

    ; Si te gusta el hardware, entonces Tecnologia_Organiazacion_Computadores
    (defrule r8
            (FactorCerteza gusta_hardware si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza TOC si (encadenado ?f1 1.0)))
    )
    
    ; Si te gusta el software, entonces Metodologia_de_la_Programacion
    (defrule r9
            (FactorCerteza gusta_software si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza MP si (encadenado ?f1 1.0)))
    )
    (defrule r9a
            ?a1 <- (FactorCerteza MP si ?f1)
            ?a2 <- (FactorCerteza FP si ?f2)
        =>
            (retract ?a1)
            (assert (FactorCerteza MP si (combinacion ?f1 -0.3)))
            (printout t "Deberias cursar antes Fundamentos de programación que Metodología de la programación")
    )

    ; Si te gusta el , entonces Ingenieria_Empresa_Sociedad
    (defrule r10
            (FactorCerteza gusta_matematicas si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza IES si (encadenado ?f1 0.5)))
    )

    ; Si te gusta el hardware y no el software, entonces Estructura_Computadores
    (defrule r11
            (FactorCerteza gusta_matematicas si ?f1)
            (FactorCerteza gusta_hardware no ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza EC si (encadenado (* ?f1 ?f2) 0.8)))
    )

    ; Si te gustan las matematicas y el software, entonces Estructura_de_Datos
    (defrule r12
            (FactorCerteza gusta_matematicas si ?f1)
            (FactorCerteza gusta_software si ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza ED si (encadenado (* ?f1 ?f2) 0.9)))
    )

    ; Si te gusta el hardware y el software, entonces Sistema_Operativos
    (defrule r13
            (FactorCerteza gusta_hardware si ?f1)
            (FactorCerteza gusta_software si ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza SO si (encadenado (* ?f1 ?f2) 0.7)))
    )
    ; Tienes que cursar antes.
    (defrule r13
            ?a1 <- (FactorCerteza FS si ?f1)
            ?a2 <- (FactorCerteza SO si ?f2)
        =>
            (retract ?a2)
            (assert (FactorCerteza SO si (combinacion ?f2 -0.3)))
            (printout t "Deberias cursar antes Fundamentos del software que Sistemas Operativos")
    )
    ; Si te gustan el software, entonces Programacion_Diseno_Orientado_Objetos
    (defrule r14
            (FactorCerteza gusta_software si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza ED si (encadenado ?f1 0.7)))
    )

    ; Si te gusta el hardware y el software, entonces Sistema_Concurrente_Distribuidos
    (defrule r15
            (FactorCerteza gusta_hardware si ?f1)
            (FactorCerteza gusta_software si ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza SCD si (encadenado (* ?f1 ?f2) 0.6)))
    )

    ; Si te gusta el hardware, entonces Arquitectura_de_Computadores
    (defrule r16
            (FactorCerteza gusta_hardware si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza AC si (encadenado ?f1 1.0)))
    )

    ; Si te gustan las matemáticas y el software, entonces Algoritmica
    (defrule r17
            (FactorCerteza gusta_software si ?f1)
            (FactorCerteza gusta_matematicas si ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza ALG si (encadenado (* ?f1 ?f2) 1.0)))
    )

    ; Debe haberse cursado antes MP que algoritmica
    (defrule r17a
            ?a1 <- (FactorCerteza MP si ?f1)
            ?a2 <- (FactorCerteza ALG si ?f2)
        =>
            (retract ?a2)
            (assert (FactorCerteza ALG si (combinacion ?f2 -0.3)))
    )
    
    ; Si te gustan las matemáticas y el software, entonces Inteligencia_Artificial
    (defrule r18
            (FactorCerteza gusta_software si ?f1)
            (FactorCerteza gusta_matematicas si ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza IA si (encadenado (* ?f1 ?f2) 0.8)))
    )

    ; Si te gustan el software y el hardware, entonces Fundamentos_de_Base_de_Datos
    (defrule r19
            (FactorCerteza gusta_software si ?f1)
            (FactorCerteza gusta_hardware si ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza FBD si (encadenado (* ?f1 ?f2) 0.7)))
    )

    ; Si te gustan el software y el hardware, entonces Fundamentos_de_Ingeniera_del_Software
    (defrule r20
            (FactorCerteza gusta_software si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza FIS si (encadenado ?f1 1.0)))
    )

    ; Si te gustan el software y el hardware, entonces Ingenieria_de_Servidores
    (defrule r21
            (FactorCerteza gusta_software si ?f1)
            (FactorCerteza gusta_hardware si ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza IS si (encadenado (* ?f1 ?f2) 0.8)))
    )

    ; Si te gustan el software y el hardware, entonces Fundamentos_de_Redes
    (defrule r22
            (FactorCerteza gusta_software si ?f1)
            (FactorCerteza gusta_hardware si ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza IS si (encadenado (* ?f1 ?f2) 0.8)))
    )

    ; Si te gustan las matematicas, entonces Modelos_de_Computacion
    (defrule r23
            (FactorCerteza gusta_matematicas si ?f1)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza MC si (encadenado ?f1 0.8)))
    )
    (defrule r23a
            ?mc <- (FactorCerteza MC si ?f1)
            (FactorCerteza nota_media alta ?f2)
            (test (> ?f1 0))
        =>
            (assert (FactorCerteza MC si (encadenado (* ?f1 ?f2) 0.9)))
            (retract ?mc)
    )

    ; Si te gustan el software y el hardware, entonces Informacion_Grafica
    (defrule r24
            (FactorCerteza gusta_matematicas si ?f1)
            (FactorCerteza gusta_software si ?f2)
            (test (and (> ?f1 0) (> ?f2 0)))
        =>
            (assert (FactorCerteza IG si (encadenado (* ?f1 ?f2) 0.7)))
    )

    ; Si te gustan el software, entonces Diseno_Desarrollo_Sistemas_Informacion
    (defrule r25
            (FactorCerteza gusta_software si ?f1)
        =>
            (assert (FactorCerteza DDSI si (encadenado ?f1 0.7)))
    )

    ; Main
    (defrule Main_Recomendador_Asignatura
        =>
            (assert (Pregunta Creditos))
    )

    (defrule Pregunta_Creditos
            ?p <- (Pregunta Creditos)
        =>
            (printout t "Indique el numero de creditos, multiplo de 8, que necesita que le aconsejen: ")
            (assert (NumCreditos (read)))
            (retract ?p)
    )

    (defrule Numero_Creditos
            (NumCreditos ?n)
            ; Comprobamos que es multiplo de 8
            (test (and (> ?n 0) (= (mod ?n 8) 0)))
        =>
            (assert (NumSelAsig 0))
            (assert (CreditosAdjudicados 0))
            (assert (Pregunta Seleccion 1))
    )

    ; Error en el numero de creditos
    (defrule ErrorNumero_Creditos
            (declare (salience -10))
            ?nc <- (NumCreditos ?n)
        =>
            (printout t "Indique el numero de creditos de forma correcta, este debe de ser multiplo de 8. " crlf)
            (assert (Pregunta Creditos))
            (retract ?nc)
    )

    ; Seleccion de Asignaturas
    (defrule Seleccionar_Curso
            (declare (salience 9))
            ?p <- (Pregunta Seleccion ?k)
            (Asignatura ?n ?a ?k)
        =>
            (printout t "Quiere elegir asignaturas de " ?k " curso (s/n)?: ")
            (bind ?r (lowcase (read)))
            (if (= (str-compare ?r "s") 0) then
                    (assert (Descartar ?k))
                else
                    (assert (EliminaAsignaturas ?k))
            )
            (retract ?p)
    )

    ; Eliminamos asignatura
    (defrule Elimina_Curso_Entero
            (declare (salience 10))
            (EliminaAsignaturas ?k)
            ?a <- (Asignatura $? $? ?k)
        =>
            (retract ?a)
    )

    ; Hemos eliminado las posibles asignaturas
    (defrule Termina_Elimina_Curso
            (declare (salience 10))
            ?e <- (EliminaAsignaturas ?k)
            (not (Asignatura $? $? ?k))
        =>
            (assert (Pregunta Seleccion (+ ?k 1)))
            (retract ?e)
    )

    ; Preguntamos por las asignaturas del curso k-ésimo
    (defrule Descartar_Asignaturas
            (declare (salience 10))
            (Descartar ?k)
            ?ns <- (NumSelAsig ?n)
            ?v <- (Asignatura ?nombre ?abreviatura ?k)
        =>
            (printout t "Quiere elegir la asignatrua '" ?nombre "' (s/n)?: ")
            (bind ?r (lowcase (read)))
            (if (= (str-compare ?r "s") 0) then
                (assert (SelAsignatura ?nombre ?abreviatura ?k))
                (retract ?ns)
                (assert (NumSelAsig (+ ?n 1)))
            )
            (retract ?v)
    )

    (defrule Elimina_Preguntadas
            (declare (salience 11))
            ?d <- (Descartar ?k)
            (not (Asignatura ?nombre ?abreviatura ?k))
        =>
            (assert (Pregunta Seleccion (+ ?k 1)))
            (retract ?d)
    )


    ; Hemos terminado de preguntar por los cursos
    (defrule Seleccionar_Asignaturas_Final
            (declare (salience 12))
            ?s <- (Pregunta Seleccion ?k)
            (not (Asignatura $? $? $?))
        =>
            (assert (Recomendar Matematicas))
            (retract ?s)
    )

    ; Preguntamos las cuestiones "importantes".
    (defrule Preguntar_Matematicas
            (declare (salience 20))
            ?r <- (Recomendar Matematicas)
            (not (Evidencia gusta_matematicas ?g))
        =>
            (printout t "Indique si le gusta o no las matematicas (Si/No o Siguiente): ")
            (assert (Respuesta matematicas (lowcase (read))))
            (retract ?r)
    )

    (defrule Recomendado_Matematicas
            (declare (salience 21))
            ?r <- (Respuesta matematicas ?vm&si|no)
        =>
            (assert (Evidencia gusta_matematicas ?vm))
            (assert (Recomendar Hardware))
            (retract ?r)
    )

    (defrule Default_Recomendado_Matematicas
            (declare (salience 21))
            ?r <- (Respuesta matematicas siguiente)
        =>
            (printout t "Se ha tomado por defecto que 'si' tiene interes por las matematicas." crlf)
            (assert (Evidencia gusta_matematicas si))
            (assert (Recomendar Hardware))
            (retract ?r)
    )

    (defrule Error_Recomendado_Matematicas
            (declare (salience 20))
            ?r <- (Respuesta matematicas ?vm)
        =>
            (printout t "Error en la entrada del interes por matematicas." crlf)
            (assert (Recomendar Matematicas))
            (retract ?r)
    )

    ; Hardware
    (defrule Preguntar_Hardware
            (declare (salience 20))
            (not (Evidencia gusta_hardware ?g))
            ?r <- (Recomendar Hardware)
        =>
            (printout t "Indique si le gusta o no el hardware (Si/No o Siguiente): ")
            (assert (Respuesta hardware (lowcase (read))))
            (retract ?r)
    )


    (defrule Recomendado_Hardware
            (declare (salience 21))
            ?r <- (Respuesta hardware ?vm&si|no)
        =>
            (assert (Evidencia gusta_hardware ?vm))
            (assert (Recomendar Software))
            (retract ?r)
    )

    (defrule Default_Recomendado_Hardware
            (declare (salience 21))
            ?r <- (Respuesta hardware siguiente)
        =>
            (printout t "Se ha tomado por defecto que 'si' tiene interes por el hardware." crlf)
            (assert (Evidencia gusta_hardware si))
            (assert (Recomendar Software))
            (retract ?r)
    )

    (defrule Error_Recomendado_Hardware
            (declare (salience 21))
            ?r <- (Respuesta hardware ?vm)
        =>
            (printout t "Error en la entrada del interes por hardware. " crlf)
            (assert (Recomendar Hardware))
            (retract ?r)
    )


    ; Software
    (defrule Preguntar_Software
            (declare (salience 30))
            ?r <- (Recomendar Software)
            (not (Evidencia gusta_software ?g))
        =>
            (printout t "Indique si le gusta o no el software (Si/No o Siguiente): ")
            (assert (Respuesta software (lowcase (read))))
            (retract ?r)
    )


    (defrule Recomendado_Software
            (declare (salience 31))
            ?r <- (Respuesta software ?vm&si|no)
        =>
            (assert (Evidencia gusta_software ?vm))
            (assert (Recomendar NotaMedia))
            (retract ?r)
    )

    (defrule Default_Recomendado_Software
            (declare (salience 31))
            ?r <- (Respuesta software siguiente)
        =>
            (printout t "Se ha tomado por defecto que 'si' tiene interes por el hardware." crlf)
            (assert (Evidencia gusta_software si))
            (assert (Recomendar NotaMedia))
            (retract ?r)
    )

    (defrule Error_Recomendado_Software
            (declare (salience 31))
            ?r <- (Respuesta software ?vm)
        =>
            (printout t "Error en la entrada del interes por software. " crlf)
            (assert (Recomendar Software))
            (retract ?r)
    )
    ; Nota Media
    (defrule Preguntar_NotaMedia
            (declare (salience 40))
            ?r <- (Recomendar NotaMedia)
            (not (Evidencia nota_media ?g))
        =>
            (printout t "Indique su nota media en rango numerico (0-10): ")
            (assert (Respuesta media (read)))
            (retract ?r)
    )

    (defrule Recomendado_NotaMedia
            (declare (salience 41))
            ?r <- (Respuesta media ?vm)
            (test (and (>= ?vm 0) (<= ?vm 10)))
        =>
            (if (<= ?vm 6) then
                (assert (Evidencia nota_media baja))
            else (if (<= ?vm 8) then
                (assert (Evidencia nota_media media))
            else 
                (assert (Evidencia nota_media alta))
            ))
            (retract ?r)
            (assert (Resolver));
    )

    (defrule Default_Preguntar_NotaMedia
            (declare (salience 41))
            ?r <- (Respuesta media siguiente)
        =>
            (printout t "Se ha tomado por defecto que 'baja' como calificación media." crlf)
            (assert (Evidencia nota_media baja))
            (retract ?r)
            (assert (Resolver));
    )

    (defrule Error_Recomendado_NotaMedia
            (declare (salience 41))
            ?r <- (Respuesta nota_media ?vm)
        =>
            (printout t "Error en la entrada nota media. " crlf)
            (assert (Recomendar NotaMedia))
            (retract ?r)
    )

    (defrule Resolver
            (declare (salience 100))
            (Resolver)

            ; Comprobamos créditos encontrados
            ?ca <- (CreditosAdjudicados ?c)
            (NumCreditos ?nc)
            (test (< ?c ?nc))

            ?sa <- (SelAsignatura $? ?p1 $?)
            (SelAsignatura $? ?p2 $?)
            
            ?fc <- (FactorCerteza ?p1 ?yesno1 ?v1)
            (not (FactorCerteza ? ? ?v2&:(> ?v2 ?v1)))

        =>
            (retract ?fc)
            ; Incrementar el numero de creditos ya adjuntados
            (bind ?add (+ ?c 8))
            (assert (CreditosAdjudicados ?add))
            
            (retract ?ca ?sa)
            
            ; Porcentajes
            (bind ?per (* ?v1 100))
            (printout t ?yesno1 " se le recomienda la asignatura " ?p1 "  con una confianza del " ?per "%" crlf)
    )

    
    (defrule Finalizar
            (declare (salience 9999))
            ?ca <- (CreditosAdjudicados ?c)
            (NumCreditos ?nc)
            
            (test (>= ?c ?nc))
        =>
            (assert (temp (read))) ; Paramos.
    )


