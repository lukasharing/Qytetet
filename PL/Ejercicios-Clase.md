# Ejercicio 13
Vamos a realizar el ejercicio A.
> LALR es LR(1) Compactado


1.
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

    I_0 = clausura([S' -> .S]) = {
        [S'-> .S]    // Expandimos S ya que no es terminal   0
        [S -> .Aa]   // Expandimos A ya que no es terminal   1
        [S -> .bAc]  // No expandimos ya que b es terminal   2
        [S -> .Bc]   // Expandimos B ya que no es terminal   3
        [S -> .bBa]  // No expandimos ya que b es termina    4
        [A -> .d]    // No expandimos ya que d es termina    5
        [B -> .d]    // No expandimos ya que d es termina    6
    }

    // Movemos el puntero I_o 0
    I_1 = goto(I_o, S) = {
        [S' -> S.]   // No hay nada que expandimos, S' ha terminado
    }

    // Movemos el puntero I_o 1
    I_2 = goto(I_o, A) = {
        [S -> A.a]  // No expandimos ya que es terminal       0
    }

    // Movemos el puntero I_o 2, 4
    I_3 = goto(I_o, b) = {
        [S -> b.Ac]  // Expandimos ya que A no es terminal    0
        [S -> b.Ba]  // Expandimos ya que B no es terminal    1
        [A -> .d]    // No expandimos ya que d es terminal    2
        [B -> .d]    // No expandimos ya que d es terminal    3
    }

    // Movemos el puntero I_o 3
    I_4 = goto(I_o, B) = {
        [S -> B.c]   // No expandimos ya que c es terminal
    }

    // Movemos el puntero I_o 5, 6
    I_5 = goto(I_o, d) = {
        [A -> d.]    // No expandimos ya A ha terminado
        [B -> d.]    // No expandimos ya A ha terminado
    }

    // Movemos el puntero I_2 0
    I_6 = goto(I_1, a) = {
        [S -> Aa.]   // No expandimos ya que a es terminal    0
    } # Podemos reducir

    // Movemos el puntero I_3 0
    I_7 = goto(I_3, A) = {
        [S -> bA.c]   // No expandimos ya que c es terminal   0
    }

    // Movemos el puntero I_3 1
    I_8 = goto(I_3, B) = {
        [S -> bB.a]   // No expandimos ya que a es terminal   0
    }

    // Movemos el puntero I_3 2, 3
    Vemos que I_9 = I_5, por lo que no se crea I_9
    Hay que señalarlo en la tabla

    // Movemos el puntero I_7 0 #MAL
    I_9 = goto(I_7, c) = {
        [S -> bAc.]   // No expandimos ya S ha terminado
    } # Podemos reducir

    // Movemos el puntero I_8 0
    I_10 = goto(I_8, c) = {
        [S -> bAc.]   // No expandimos ya S ha terminado
    } # Podemos reducir

    // Movemos el puntero I_4 0
    I_11 = goto(I_8, a) = {
        [S -> bBa.]   // No expandimos ya S ha terminado
    }


    > S_n Shift Estado n (Desplazamiento)
    > R_n Reduction Estado n (Reducción)
    > C_n Conflict Estado n (Conficto)
    > Acc Aceptar
    
    > *SIEMPRE* Fila 1, terminal es Aceptar
    > A la derecha, solo transicionamos si no hemos acabado, es decir, I_n = goto(I_k, <Transicion>) no ha acabado.

    > $ \in seguidores(S), SIEMRPE
    > r_n Siempre estará en los terminales seguidores 

    > Si hay conflicto, la gramática *NO* es SLR

    > Sabemos que hay reducción si I_n hay una producción finalizada que se encuentra en S

    Seguidores(I_5) = Seguidores({
        A -> d.
        B -> d.
    }) = Seguidores(A) + Seguidores(B) = {a, c}

    Seguidores(I_6) = Seguidores({
        S -> Aa.
    }) = Seguidores(S) = {$}

    Seguidores(I_9) = 

    | Estado | a | b | c | d | $ || S | A | B |
    |--------|---|---|---|---|---||---|---|---|
    | 0      |   |S_3|   |S_5|   || 1 | 2 | 3 |
    | 1      |   |   |   |   |Acc||   |   |   |
    | 2      |S_6|   |   |   |   ||   |   |   |
    | 3      |   |   |   |S_5|   ||   | 7 | 8 |
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
        [A  -> .d, $] // \beta es iniciales(a$) = {a}
        [B  -> .d, $] // \beta es iniciales(c$) = {c}
    }