
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

4. Considere la gramática de cadenas de letras **a**:
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