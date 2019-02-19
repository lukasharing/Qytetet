-- Trabajo creado por Lukas Häring García

-- Declaración de tipos.
data FProp = Cierto | Falso | P String | No FProp | Y FProp FProp | O FProp FProp deriving (Show, Eq)

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

substutir::FProp -> FProp -> FProp

substutir a (No (P b)) = let c = (No (P b)) in (if (c == a) then (Cierto) else if ((niega_expresion c) == a) then (Falso) else c)
substutir a (P b)      = let c = (P b) in (if (c == a)      then (Cierto) else if ((niega_expresion c) == a) then (Falso) else c)

substutir a (No b) = niega_expresion (substutir a b)
substutir a (Y b c) = (substutir a b) `Y` (substutir a c)
substutir a (O b c) = (substutir a b) `O` (substutir a c)
substutir a b = b

tableau_cerrado::[FProp] -> Bool

tableau_cerrado ((No (P a)):xs) = tableau_cerrado (map (substutir (No (P a))) xs)
tableau_cerrado ((P a):xs) = tableau_cerrado (map (substutir (P a)) xs)

tableau_cerrado ((O a b):xs) = (tableau_cerrado (a:xs) && tableau_cerrado (b:xs))
tableau_cerrado ((Y a b):xs) = tableau_cerrado (a:(b:xs))
tableau_cerrado ((No a):xs) = tableau_cerrado ((niega_expresion a):xs)


tableau_cerrado [Cierto] = True
tableau_cerrado [] = True

tableau_cerrado (Cierto:xs) = tableau_cerrado xs
tableau_cerrado (Falso:xs) = False

--tableau_cerrado proposiciones = tableau_cerrado (proposiciones !! 0) proposiciones
--or (map tableau_cerrado (concat (map equivalencia(map reducir proposiciones))))

-- Declaración de variables (De la práctica).
f1 = ((P "p") `Y` (No (P "q"))) `O` (No (P "p"))
f2 = (No Falso) `O` ((P "p") `Y` (No ((P "q") `Y` (No (P "q")))))
f3 = Cierto `Y` (P "q") `Y` ((No (P "q")) `O` (P "r"))

-- Main
--main::IO ()
--main = print f1
