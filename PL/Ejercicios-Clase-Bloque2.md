
1. Considera el siguiente esquema de traducción incompleto:

    * S -> A fin { imprimir S.vlit }
    * A -> abre A cierra
    * A -> lit

    ```
            S
        /     \
        |        \
        A        fin
    /    |   \ 
    abre  A  cierra
    ...
        |
        lit
    ```
    Mostrar en pantalla el valor del literal

    S -> A { S.vlit = A.vlit } fin { imprimir S.vlit }

    A -> abre A1 cierra            { A.vlit = A1.vlit }

    A -> lit                       { A.vlit = lit.lexema }

2. Considera la siguiente gramática:

    Lis -> Ini elem Mas Fin

    Mas -> sep elem Mas | &lambda;

    Ini -> apa | acor

    Fin -> cpar | ccor

    ```
                    Lis
                /  |  | \
            /   |      |   \
            Ini  elem   Mas   Fin
            / \          |    /  \
        apa  acor       |  cpar ccor
                    /  |  \
                sep  elem Mas
                            |
                            ...
                            | 
                            λ
    ```

    Separamos las producciones:

    * Lis -> Ini elem Mas Fin { Lis.ok = Ini.valpar == Fin.valpar }
    * Mas -> sep elem Mas
    * Mas -> &lambda;
    * Ini -> apa { Ini.valpar = true }
    * Ini -> acor { Ini.valpar = false }
    * Fin -> cpar { Fin.valpar = true }
    * Fin -> ccor { Fin.valpar = false }

3. Considera la siguiente gramática:

    * Lis -> Ini elem Mas
    * Mas -> sep elem Mas | Fin
    * Ini -> apa | acor | alla
    * Fin -> cpar | ccor | clla

    ```
                    Lis
                /  |    |
            /     |    |
        Ini     elem   Mas
        / |   \        |
    apa  acor  alla    |
                    /  |  \
                sep  elem Mas
                            |
                            ...
                            | 
                            Fin
                        /   |   \
                        cpar ccor clla
    ```

    Separamos las producciones:

    El error se debe gestionar en la producción "**Mas -> Fin**".

    * Lis -> Ini elem { Ini.val = Mas.val } Mas
    * Mas -> sep elem { Mas1.val = Mas.val } Mas1
    * Mas -> Fin  { if(Fin.val != Mas.val) then trata_error() }
    * Ini -> apa  { Ini.val = 1 }
    * Ini -> acor { Ini.val = 2 }
    * Ini -> alla { Ini.val = 3 }
    * Fin -> cpar { Fin.val = 1 }
    * Fin -> ccor { Fin.val = 2 }
    * Fin -> clla { Fin.val = 3 }

4. Considere la gramática de cadenas de letras **a**, calcular para cada gramática, el número de a's que produce.
    
    A.
    
    * S -> X
    * X -> a X | &lambda; 

    ```
            S
            |
            X
          /   \
         a     X
               |
              ...
               |
               λ
    ```

    * S -> X        { S.na = X.na }
    * X -> a X1     { X.na = 1 + X1.na }
    * X -> &lambda; { X.na = 0 }

    B.

    * S -> a X b
    * X -> a X X | b a X a | c Y
    * Y -> c Y | a

    ```
            S
          / |  \
        a   X   b
            |
           ...
            |
          / |  \
         a  X   X
            |   |
           ...  ...
            |
         / |  | \
        b  a  X  a
              |
             ...
              |
              Y
            / |
           c  Y
              |
             ...
              |
              Y
              |
              a
    ```

    * S -> a X b    { S.na = 1 + X.na }
    * X -> a X1 X2  { X.na = 1 + X1.na + X2.na }  
    * X -> b a X1 a { X.na = X1.na + 2 }
    * X -> c Y      { X.na = Y.na }
    * Y -> c Y1     { Y.na = Y1.na }
    * Y -> a        { Y.na = 1 }

    C. 

    may &in; [A...Z]

    min &in; [a...Z]

    dig &in; [0...9]

    * S -> may X | X | dig Y
    * X -> min X | &lambda;
    * Y -> dig Y | &lambda;

    **Resolucion**:

    * S -> may X    { if may.lex = A then S.na = X.na + 1 else S.na = X.na }
    * S -> X        { S.na = X.na }
    * S -> dig Y    { S.na = Y.na }
    * X -> min X1   { if min.lex = a then X.na = X1.na + 1 else X.na = X1.na }
    * X -> &lambda; { X.na = 0 }
    * Y -> dig Y    { Y.na = 0 }
    * Y -> &lambda; { Y.na = 0 }

    5. Nivel de profundidad nº de paréntesis abiertos en un cierto nivel con x, pero no han sido cerrados. 

    apar = (

    cpar = )

    S -> apar A cpar
    A -> apar A A cpar
    A -> x

    ```
         S
      /  |  \
     (   A   )
       / | | \
      (  A A  )
         | |
        ..... Las alturas son distintas
        |   |
       x ... x
    ```

    > Vamos a calcular el nivel de profundidad del árbol

    S -> apar A cpar { S.mnp = A.mnp }

    A -> apar A1 A2 cpar { if A1.mnp > A2.mnp then A.mnp = A1.mnp + 1 else A.mnp = A2.mnp + 1 }
    
    A -> x { A.mnp = 0 }

    > Vamos a mostrar la altura de cada x, como es herencia, lo pondremos **ANTES** de pasarlo a la siguiente.

    S -> apar **{ A.mnp = 1 }** A cpar

    A -> apar **{ A1.mnp = A.mnp + 1 }** A1 **{ A2.mnp = A.mnp + 1 }** A2 cpar
    
    A -> x { print(A.mnp) }
