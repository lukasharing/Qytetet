-- Trabajo creado por Lukas Häring García

-- Declaración de tipos.
data FProp = Cierto | Falso | P String | No FProp | Y FProp FProp | O FProp FProp deriving (Show, Read)
-- Declaración de funciones
-- Por las leyes de de Morgan
-- Niega recursivamente Toda la proposición
niega_expresion::FProp -> FProp
-- ~(A y B) = ~A o ~B
niega_expresion (Y a b) = (niega_expresion a) `O` (niega_expresion b)
-- ~(A o B) = ~A y ~B
niega_expresion (O a b) = (niega_expresion a) `Y` (niega_expresion b)
-- ~True = False
niega_expresion (Cierto) = Falso
-- ~False = True
niega_expresion (Falso) = Cierto
-- ~~A = A
niega_expresion (No a) = a
-- ~A = ~A
niega_expresion a = No a

----------------- Lista abierta , Lista cerrada
tableau__cerrado::[FProp] -> [FProp] -> Bool

-- Si es cierto el elemento, se elimina.
tableau__cerrado ((Cierto):xs) ls = tableau__cerrado xs ls
-- Si es falso, termina y devuelve falso.
tableau__cerrado ((Falso):xs) ls = False

-- Si es "y", se añade a la lista de visitados y se crean dos ramas consecutivas.
tableau__cerrado ((Y a b):xs) ls = tableau__cerrado (xs++[a, b]) ((Y a b):ls)

-- Si es "o", se añade a la lista de visitados y se crean dos ramas distintas
tableau__cerrado ((O a b):xs) ls =
  let x = (O a b) in (
    (tableau__cerrado (xs++[a]) ((Y a b):ls)) || (tableau__cerrado (xs++[b]) ((Y a b):ls))
  )

-- Si es un predicado y este es atómico
-- : Si su inversa está en la rama del árbol, termina
-- : Si no está, se añade a visitados
tableau__cerrado ((P a):xs) ls =
  if (elem (No (P a)) ls) then False
  else tableau__cerrado xs ((P a): ls)

tableau__cerrado ((No (P a)):xs) ls =
  if (elem (P a) ls) then False
  else tableau__cerrado xs ((No (P a)): ls)

-- Si es una negación, se evalua recursivamente su negación (No es atómico).
tableau__cerrado ((No a):xs) ls = tableau__cerrado ((niega_expresion a):xs) ls

-- El conjunto vacío es tautología.
tableau__cerrado [] _ = True

tableau_cerrado::[FProp] -> Bool
tableau_cerrado xs = tableau__cerrado xs []

-- Proposiciones predefinidas
f1 = O (Y (P "p") (No (P "q"))) (No (P "p"))
f2 = (No Falso) `O` ((P "p") `Y` (No ((P "q") `Y` (No (P "q")))))
f3 = Y (Y Cierto (P "q")) (O (No (P "q")) (P "r"))
f4 = ((P "p") `O` (P "q")) `Y` ((No (P "p")) `Y` (No (P "q")))
f5 = O (P "p") (O (No (P "q")) (No (P "q")))

-- {~A} |= []
tautologia::FProp -> Bool
tautologia ex = ((tableau_cerrado [niega_expresion ex]) == True)

-- A u {~B} |= []
consecuencia::[FProp] -> FProp -> Bool
consecuencia ls ex = ((tableau_cerrado (ls++[niega_expresion ex])) == True)

-- A u {~B} |= []
equivalentes::FProp -> FProp -> Bool
equivalentes e1 e2 = (let e3 = (((niega_expresion e1) `O` e2) `Y` ((niega_expresion e2) `O` e1)) in (tableau_cerrado [e3] == True))

-- Instancias
instance Eq FProp where
  No x == No y = (x == y)
  Y x y == Y x' y' = (((x == x') && (y == y')) || ((x == y') && (x' == y)))
  O x y == O x' y' = (((x == x') && (y == y')) || ((x == y') && (x' == y)))
  P a == P b = (a == b)
  Cierto == Cierto = True
  Falso == Falso = True
  a == b = False

instance Ord FProp where
  x <= y = consecuencia [y] x

-- IO
muestraMenu::IO ()
muestraMenu = putStr ("-------- Tableaux Programación declarativa ----------\n 0. Tautología. \n 1. Consecuencia. \n 2. equivalentes. \n Su opción: ")

-- https://stackoverflow.com/questions/5289779/printing-elements-of-a-list-on-new-lines
muestraExpresion::[FProp] -> IO()
muestraExpresion ls = putStr (unlines (map (\x -> show x::String) ls))

accionMenu::[FProp] -> Int -> IO ()
accionMenu ls 0  = do
                    muestraExpresion ls
                    putStrLn ("Cual quiere ver que es tautología (0-" ++ (show (length ls - 1)) ++ ")?")
                    c <- getLine
                    let n = read c::Int
                    if (tautologia (ls !! n)) then putStrLn "Es tautología."
                    else putStrLn "No es tautología."

accionMenu ls 1  = do
                    muestraExpresion ls
                    putStrLn ("Cual quiere ver que es consecuencia de las demás (0-" ++ (show (length ls - 1)) ++ ")?")
                    c <- getLine
                    let n = read c::Int
                    if (consecuencia ((take n ls) ++ (drop (n + 1) ls)) (ls !! n)) then putStrLn "Es consecuencia lógica."
                    else putStrLn "No es consecuencia lógica."

accionMenu ls 2  = do
                    muestraExpresion ls
                    putStrLn ("Primera expresión a comparar (0-" ++ (show (length ls - 1)) ++ ")?")
                    c <- getLine
                    let n = read c::Int
                    putStrLn ("Segunda expresión a comparar (0-" ++ (show (length ls - 1)) ++ ")?")
                    c <- getLine
                    let k = read c::Int
                    if (equivalentes (ls !! k) (ls !! n)) then putStrLn "Son equivalentes."
                    else putStrLn "No son equivalentes."

accionMenu _ _ = putStrLn "Opción elegida incorrecta."


menu::[FProp] -> IO ()
menu ls = do
            muestraMenu
            c <- getLine
            let n = read c::Int
            if ((n >= 0) && (n < 3)) then
              accionMenu ls n
            else
              putStrLn "opción incorrecta"
            menu ls

-- Main
-- https://stackoverflow.com/questions/10285837/read-n-lines-into-a-string
-- >>= print .
main::IO ()
main = do
         putStrLn "Cuantas fórmulas quiere escribir?: "
         ls <- (getLine >>= sequence . flip replicate getLine . read)
         let formulas = map (\x -> read x::FProp) ls
         menu formulas
