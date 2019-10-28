# Ejercicio 13
Vamos a realizar el ejercicio A.
> LALR es LR(1) Compactado


1. Ejercicicio 1

    S -> Aa | bAc | Bc | bBa
    
    A -> d
    
    B -> d

    Separamos las reglas

    S -> Aa
    
    S -> bAc
    
    S -> Bc
    
    S -> bBa
    
    A -> d
    
    B -> d

    Aumentamos la gramática S a S'
    S' -> S

    `goto(In, <Símbolo>)`, mueve el punto hacia delante 

    ```
    I_0 = clausura([S' -> .S]) = {
        [S'-> .S]    // Expandimos S ya que no es terminal   0
        [S -> .Aa]   // Expandimos A ya que no es terminal   1
        [S -> .bAc]  // No expandimos ya que b es terminal   2
        [S -> .Bc]   // Expandimos B ya que no es terminal   3
        [S -> .bBa]  // No expandimos ya que b es termina    4
        [A -> .d]    // No expandimos ya que d es termina    5
        [B -> .d]    // No expandimos ya que d es termina    6
    }
    ```
    // Movemos el puntero I_o 0
    ```
    I_1 = goto(I_o, S) = {
        [S' -> S.]   // No hay nada que expandimos, S' ha terminado
    }
    ```

    // Movemos el puntero I_o 1
    ```
    I_2 = goto(I_o, A) = {
        [S -> A.a]  // No expandimos ya que es terminal       0
    }
    ```

    // Movemos el puntero I_o 2, 4
    ```
    I_3 = goto(I_o, b) = {
        [S -> b.Ac]  // Expandimos ya que A no es terminal    0
        [S -> b.Ba]  // Expandimos ya que B no es terminal    1
        [A -> .d]    // No expandimos ya que d es terminal    2
        [B -> .d]    // No expandimos ya que d es terminal    3
    }
    ```

    // Movemos el puntero I_o 3
    ```
    I_4 = goto(I_o, B) = {
        [S -> B.c]   // No expandimos ya que c es terminal
    }
    ```
    // Movemos el puntero I_o 5, 6
    ```
    I_5 = goto(I_o, d) = {
        [A -> d.]    // No expandimos ya A ha terminado
        [B -> d.]    // No expandimos ya A ha terminado
    }
    ```
    // Movemos el puntero I_2 0
    ```
    I_6 = goto(I_1, a) = {
        [S -> Aa.]   // No expandimos ya que a es terminal    0
    } # Podemos reducir
    ```
    // Movemos el puntero I_3 0
    ```
    I_7 = goto(I_3, A) = {
        [S -> bA.c]   // No expandimos ya que c es terminal   0
    }
    ```
    // Movemos el puntero I_3 1
    ```
    I_8 = goto(I_3, B) = {
        [S -> bB.a]   // No expandimos ya que a es terminal   0
    }
    ```
    // Movemos el puntero I_3 2, 3
    
    Vemos que I<sub>9</sub> = I<sub>5</sub>, por lo que no se crea I<sub>9</sub>. **Hay que señalarlo en la tabla**

    // Movemos el puntero I_7 0 #MAL
    ```
    I_9 = goto(I_7, c) = {
        [S -> bAc.]   // No expandimos ya S ha terminado
    } # Podemos reducir
    ```
    // Movemos el puntero I_8 0
    ```
    I_10 = goto(I_8, c) = {
        [S -> bAc.]   // No expandimos ya S ha terminado
    } # Podemos reducir
    ```
    // Movemos el puntero I_4 0
    ```
    I_11 = goto(I_8, a) = {
        [S -> bBa.]   // No expandimos ya S ha terminado
    }
    ```

    > S<sub>n</sub> Shift Estado n (Desplazamiento)
    
    > R<sub>n</sub> Reduction Estado n (Reducción)
    
    > C<sub>n</sub> Conflict Estado n (Conficto)
    
    > **SIEMPRE** Fila 1, terminal es Aceptar (Acc)

    > A la derecha, solo transicionamos si no hemos acabado, es decir, I<sub>n</sub> = goto(I<sub>k</sub>, \<Transicion\>) no ha acabado.

    > $ &in; Seguidores(S) **SIEMRPE**
    
    > R<sub>n</sub> Siempre estará en los terminales seguidores 

    > Si hay conflicto, la gramática **no es SLR**

    > Sabemos que hay reducción si I<sub>n</sub> hay una producción finalizada que se encuentra en S

    ```
    Seguidores(I_5) = Seguidores({
        A -> d.
        B -> d.
    }) = Seguidores(A) + Seguidores(B) = {a, c}
    ```
    ```
    Seguidores(I_6) = Seguidores({
        S -> Aa.
    }) = Seguidores(S) = {$}
    ```
    ```
    Seguidores(I_9) = 
    ```

    | Estado | a | b | c | d | $ || S | A | B |
    |--------|---|---|---|---|---|-|---|---|---|
    | 0      |   |S_3|   |S_5|   || 1 | 2 | 3 |
    | 1      |   |   |   |   |Acc||   |   |   |
    | 2      |S_6|   |   |   |   ||   |   |   |
    | 3      |   |   |   |S_9|   ||   | 7 | 8 |
    | 4      |   |   |   |   |   ||   |   |   |
    | 5      |C_1|   |C_1|   |   ||   |   |   |
    | 6      |   |   |   |   |   ||   |   |   |
    | 7      |   |   |   |   |   ||   |   |   |
    | 8      |   |   |   |   |   ||   |   |   |
    | 9      |   |   |   |   |   ||   |   |   |
    | 10     |   |   |   |   |   ||   |   |   |
    | 11     |   |   |   |   |   ||   |   |   |

    > C_1 = S5/S6

    ## LR(1)

    > [Producción, Padre]
    > Iniciales(\beta a), siendo \beta, lo que hay detrás justo detrás del punto, a es el valor del padre

    I_0 = clausura([S' -> .S, $]) = {
        [S' -> .S  , $]
        [S  -> .Aa , $] // \beta es iniciales($) = {$}, Expandimos A
        [S  -> .bAc, $] // \beta es iniciales($) = {$}
        [S  -> .Bc , $] // \beta es iniciales($) = {$}, Expandimos B
        [S  -> .bBa, $] // \beta es iniciales($) = {$}
        [A  -> .d, a] // \beta es iniciales(a$) = {a}
        [B  -> .d, c] // \beta es iniciales(c$) = {c}
    }

    ```
    I_1 = goto(I_0, S) = {
        [S' -> S., $] 
    }
    ```
    ```
    I_2 = goto(I_0, A) = {
        [S -> A.a, $]
    } 
    ```
    ```
    I_3 = goto(I_0, b) = {
        [S -> b.Ac, $],
        [S -> b.Ba, $],
        [A -> .d, c],
        [B -> .d, a],
    } 
    ```
    ```
    I_4 = goto(I_0, B) = {
        [S -> B.c, $]
    }
    ```
    ```
    I_5 = goto(I_0, d) = {
        [A -> d., a]
        [B -> d., c]
    }
    ```
    ```
    I_6 = goto(I_2, a) = {
        [A -> Aa., $]
    }
    ```
    ```
    I_7 = goto(I_3, A) = {
        [S -> bA.c, $]
    }
    ```
    ```
    I_8 = goto(I_3, B) = {
        [S -> bB.a, $]
    }
    ```
    ```
    I_9 = goto(I_3, d) = {
        [A -> d., c]
        [B -> d., a]
    }
    ```
    ```
    I_10 = goto(I_4, c) = {
        [S -> B.c, $]
    }
    ```
    ```
    I_11 = goto(I_7, c) = {
        [S -> bAc., $]
    }
    ```
    ```
    I_12 = goto(I_8, a) = {
        [S -> bBa., $]
    }
    ```

    > El simbolo de anticipación nos dice dónde ponemos las reducciones

    | Estado | a  | b | c  | d | $ | | S | A | B |
    |--------|----|---|----|---|---|-|---|---|---|
    | 0      |    |S_3|    |S_5|   | | 1 | 2 | 3 |
    | 1      |    |   |    |   |Acc| |   |   |   |
    | 2      |S_6 |   |    |   |   | |   |   |   |
    | 3      |    |   |    |S_9|   | |   | 7 | 8 |
    | 4      |    |   |S_10|   |   | |   |   |   |
    | 5      |R_5 |   |R_6 |   |   | |   |   |   |
    | 6      |    |   |    |   |R_1| |   |   |   |
    | 7      |    |   |R_11|   |   | |   |   |   |
    | 8      |S_12|   |    |   |   | |   |   |   |
    | 9      |R_6 |   |R_5 |   |   | |   |   |   |
    | 10     |    |   |    |   |R_3| |   |   |   |
    | 11     |    |   |    |   |R_2| |   |   |   |
    | 12     |    |   |    |   |R_4| |   |   |   |

    > No hay conflicto, por lo que la gramática **es LR(1)**

    > Para saber si es LALR tenemos que crear la tabla, unir las producciones creadas en LR sin tener encuenta la anticipación

    I<sub>5</sub> e I<sub>9</sub> se pueden unir, creamos el estado:
    ```
    I_59 = {
        [A -> d., a|c]
        [A -> d., c|a]
    }
    ```

    | Estado | a     | b | c     | d  | $ | | S | A | B |
    |--------|-------|---|-------|----|---|-|---|---|---|
    | 0      |       |S_3|       |S_59|   | | 1 | 2 | 4 |
    | 1      |       |   |       |    |Acc| |   |   |   |
    | 2      |S_6    |   |       |    |   | |   |   |   |
    | 3      |       |   |       |S_59|   | |   | 7 | 8 |
    | 4      |       |   |S_10   |    |   | |   |   |   |
    | 59     |R_5/R_6|   |R_5/R_6|    |R_1| |   |   |   |
    | 6      |       |   |       |    |R_1| |   |   |   |
    | 7      |       |   |R_11   |    |   | |   |   |   |
    | 8      |S_12   |   |       |    |   | |   |   |   |
    | 10     |       |   |       |    |R_3| |   |   |   |
    | 11     |       |   |       |    |R_2| |   |   |   |
    | 12     |       |   |       |    |R_4| |   |   |   |

# Ejercicio 17
La siguiente gramática abstrae la gramática IF de acuerdo a las siguientes producciones:
    
    S -> iEtSS' | a

    S' -> eS | &epsilon;

    E -> c

    donde i = IF, e = ELSE, a = sentencia, t = THEN y c = CONDICION

    a. Demostrar que no es SLR, LR(1) ni LALR(1)
    > Bastaría con demostrar que no es **SLR**

    
    | Estado | a | c | e     | i | t | $ | | E | S | S'|
    |--------|---|---|-------|---|---|---|-|---|---|---|
    | 0      |   |   |       |S_2|   |   | |   |1  |   |
    | 1      |   |   |       |   |   |Acc| |   |   |   |
    | 2      |   |S_5|       |   |   |   | |4  |   |   |
    | 3      |   |   |R_2    |   |   |R_2| |   |   |   |
    | 4      |   |   |       |   |S_6|   | |   |   |   |
    | 5      |   |   |       |   |R_5|   | |   |   |   |
    | 6      |S_3|   |       |S_2|   |   | |   |7  |   |
    | 7      |   |   |R_4/S_9|   |   |R_4| |   |   |8  |
    | 8      |   |   |R_1    |   |   |R_1| |   |   |   |
    | 9      |S_3|   |       |S_2|   |   | |   |10 |   |
    | 10     |   |   |R_3    |   |   |R_3| |   |   |   |

    b. Modifique la tabla de análisis para que cada ELSE se asocie con el IF más cercano.

    Como hace yacc, bastaría con desplazar en la producción 7 con el else.

# Ejercicio 20
Dando la siguiente especificación obtenida del archivo `y.output` que produce el YACC a la hora de generar el analizador sintáctico:

    a. Error de **expresión**.

    > Se incluye una producción de error.

    b. Error en el token **COMA**.

    Cambiariamos la gramática con:
        lista_expr : lista_expr **coma** expresion
        coma : COMA
             | error 
    ```
    Seguirdores(coma) = Iniciales(expresion) - {epsilon}
    Iniciales(expresion) = {
        ABREPARENT,
        OPUNABIN,
        IDENTIFICADOR,
        CONSTANTE
    }
    ```

    c. Error en el token **PCOMA**.

    Modificar la gramática con:

    sentencias : sentencias sentencia punto_y_coma
               | sentencia punto_y_coma
      
    punto_y_coma : PCOMA
                 | error
    
    ```
    Seguirdores(punto_y_coma) c Seguidores(sentencias)
    Seguirdores(sentencias)


    por regla 2: FINBLOQUE
    por regla 3: Iniciales(sentencia) - {epsilon}
    
    Iniciales(sentencia) = Iniciales(bloque) u Iniciales(sentencia_asig) u Iniciales (Subprograma) u Iniciales(expresion)

    Iniciales(bloque) = { INICIOBLOQUE }
    Iniciales(sentencia_asig) = { IDENTIFICADOR }
    Iniciales(Subprograma) = { IDENTIFICADOR }
    Iniciales(expresion) = {
        ABREPARENT,
        OPUNABIN,
        IDENTIFICADOR,
        CONSTANTE
    }

    Iniciales(sentencia) = {
        FINBLOQUE,
        INICIOBLOQUE,
        ABREPARENT,
        OPUNABIN,
        IDENTIFICADOR,
        CONSTANTE
    }

    ```