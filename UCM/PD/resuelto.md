% Soluciones *Preguntas Tipo*
% Lukas Haring

# Test *listas*.
### NOTAS.
1. Los __paréntesis añaden preferencia__ a las operaciones.
2. La __lista vacía []__ contiene __cualquier tipo__ dentro.

***

1. Dadas las expresiones:  
`[True,[]] True:[] [True]:[] [[True],[]]`

```Haskell
> [True,[]]   = --Error de tipado
> True:[]     = [True]
> [True]:[]   = [[True]]
> [[True],[]] = [[True],[]]
```
\begin{itemize}
\item[$\boxtimes$] Exactamente una de las expresiones está mal tipada
\item[$\square$] Exactamente dos de las expresiones están mal tipadas
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

***
2. Sean las cuatro expresiones:  
`[True:[]] []:[True] [True]:[] [[True],[]]`

```Haskell
> [True:[]]   = [[True]]
> []:[True]   = --Error de tipado
> [True]:[]   = [[True]]
> [[True],[]] = [[True],[]]
```
\begin{itemize}
\item[$\square$] Dos de ellas están mal tipadas
\item[$\boxtimes$] Dos de ellas son sintácticmente equivalentes y una está mal tipada
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

\newpage

3. Dadas las expresiones:  
`0:[1] 0:[1]:[2] 0:[1,2] [0,1]:[[2]] ([]:[],2)`

```Haskell
> 0:[1]       = [0, 1]
> 0:[1]:[2]   = --Error de tipado
> 0:[1,2]     = [0, 1, 2]
> [0,1]:[[2]] = [[0, 1],[2]]
> ([]:[],2)   = ([[]], 2)
```
\begin{itemize}
\item[$\boxtimes$] Exactamente una de las expresiones está mal tipada
\item[$\square$] Exactamente dos de las expresiones están mal tipadas tipada
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

***
4. Dadas las expresiones:  
`[]:[1] [1:[2]]:[] [1:[2]]:[[]] [1,1]:(2:[]) (1:[]):[]`

```Haskell
> []:[1]       = --Error de tipado
> [1:[2]]:[]   = [[1:[2]]]     = [[[1, 2]]]
> [1:[2]]:[[]] = [[1:[2]], []] = [[[1, 2]], []]
> [1,1]:(2:[]) = [1, 1]:([2])  = --Error de tipado
> (1:[]):[]    = ([1]):[]      = [[1]]
```
\begin{itemize}
\item[$\square$] Exactamente tres de las expresiones están mal tipadas
\item[$\square$] Exactamente dos de las expresiones están mal tipadas
\item[$\boxtimes$] Las dos anteriores son falsas.
\end{itemize}

***
5. Dadas las expresiones:  
`0:[1] 0:[1]:[2] 0:[1,2] [0,1]:[2] (1:[]):[]`

```Haskell
> 0:[1]     = [0, 1]
> 0:[1]:[2] = --Error de tipado
> 0:[1,2]   = [0, 1, 2]
> [0,1]:[2] = --Error de tipado
> (1:[]):[] = ([1]):[] = [[1]]
```
\begin{itemize}
\item[$\square$] Exactamente tres de las expresiones están mal tipadas
\item[$\boxtimes$] Exactamente dos de las expresiones están mal tipadas
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

\newpage
6. Dadas las expresiones:  
`[1]:[] [[]]:[] []:[] (1:2):[] 1:(2:[])`

```Haskell
> [1]:[]   = [[1]]
> [[]]:[]  = [[[]]]
> []:[]    = [[]]
> (1:2):[] = --Error de tipado
> 1:(2:[]) = 1:([2]) = [1, 2]
```
\begin{itemize}
\item[$\boxtimes$] Exactamente una de las expresiones está mal tipada
\item[$\square$] Exactamente dos de las expresiones están mal tipadas tipada
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

***
7. Dadas las expresiones:  
`[0]:[1] []:[[]]:[] [0]:[[]]:[] [0]:[[1,2]] ([[]]:[],[1])`

```Haskell
> [0]:[1]       = --Error de tipado
> []:[[]]:[]    = []:[[[]]] = [[],[[]]]
> [0]:[[]]:[]   = [0]:[[[]]] = --Error de tipado
> [0]:[[1,2]]   = [[0], [1, 2]]
> ([[]]:[],[1]) = ([[[]]], [1])
```
\begin{itemize}
\item[$\boxtimes$] Exactamente una de las expresiones está mal tipada
\item[$\square$] Exactamente dos de las expresiones están mal tipadas tipada
\item[$\square$] Exactamente tres de las expresiones están mal tipadas tipada
\end{itemize}

***
8. Dadas las expresiones:  
`[1]:[] [1]:[[]]:[2] [1]:[[[]]] 0:1:2 (0:[1],2)`

```Haskell
> [1]:[]        = [[1]]
> [1]:[[]]:[2]  = --Error de tipado
> [1]:[[[]]]    = --Error de tipado
> 0:1:2         = --Error de tipado
> (0:[1],2)     = ([0,1],2)
```
\begin{itemize}
\item[$\square$] Exactamente una de las expresiones está mal tipada
\item[$\square$] Exactamente dos de las expresiones están mal tipadas tipada
\item[$\boxtimes$] Las dos anteriores son falsas.
\end{itemize}

\newpage
9.  ¿Cuál de las siguientes expresiones es sintácticamente equivalente a `[[[0],[],[2,2]]]`?

```Haskell
> ((0:[]):[[],2:2:[]]):[] = (([0]):[[],2:[2]]):[] =
  ([[0],[],[2,2]]):[] = [[[0],[],[2,2]]]
> [0]:[]:[2,2]:[]:[]      = [0]:[]:[2,2]:[[]] = --Error de tipos
```
\begin{itemize}
\item[$\boxtimes$] \begin{verbatim}((0:[]):[[],2:2:[]]):[]\end{verbatim}
\item[$\square$] \begin{verbatim}[0]:[]:[2,2]:[]:[]\end{verbatim}
\item[$\square$] Ninguna de las anteriores, porque de hecho la expresión está mal tipada
\end{itemize}

***
10.  ¿Cuál de las siguientes expresiones es sintácticamente equivalente a `[[1,[]],[3,4]]`?

\begin{itemize}
\item[$\square$] \begin{verbatim}((1:[]):[[],3:4:[]]):[]\end{verbatim}
\item[$\square$] \begin{verbatim}[0]:[]:[2,2]:[]:[]\end{verbatim}
\item[$\boxtimes$] Ninguna de las anteriores, porque de hecho la expresión está mal tipada
\end{itemize}

***
11.  ¿Cuál de las siguientes expresiones es sintácticamente equivalente a `[[3,4],[]]`?

```Haskell
> 3:4:([],[])     = --Mal tipada
> (3:4:[]):[]:[]  = (3:[4]):[]:[] = ([3, 4]):[[]] = [[3, 4],[]]
```
\begin{itemize}
\item[$\square$] \begin{verbatim}3:4:([],[])\end{verbatim}
\item[$\boxtimes$] \begin{verbatim}(3:4:[]):[]:[]\end{verbatim}
\item[$\square$] Ninguna de las anteriores, porque de hecho la expresión está mal tipada
\end{itemize}

***
12.  ¿Cuál de las siguientes expresiones es sintácticamente equivalente a `(1:[]):(1:2:[]):[]`?

```Haskell
> (1:[]):(1:2:[]):[] = ([1]):(1:[2]):[] = ([1]):([1,2]):[]
  = ([1]):[[1,2]] = [[1],[1,2]]
```
\begin{itemize}
\item[$\boxtimes$] \begin{verbatim}[[1],[1,2]]\end{verbatim}
\item[$\square$] \begin{verbatim}[[1],[1,2],[]]\end{verbatim}
\item[$\square$] Ninguna de las anteriores, porque de hecho la expresión está mal tipada
\end{itemize}

\newpage
13. ¿Cuántas de las siguientes expresiones son sintácticamente equivalentes a `[[1,2],[]]`?
<center>
`[1:2:[]] 1:2:[[]] [1,2]:[[]] [1:[2],[]]`
</center>

```Haskell
> [1:2:[]]    = [1:[2]] = [[1, 2]]
> 1:2:[[]]    = --Error de tipado
> [1,2]:[[]]  = [[1,2],[]]
> [1:[2],[]]  = [[1,2],[]]
```
\begin{itemize}
\item[$\boxtimes$] Exactamente dos
\item[$\square$] Exactamente tres
\item[$\square$] Exactamente cuatro
\end{itemize}

***
14. Considérense las expresiones
<center>
`[[1],[2]] [1]:[[2]]:[] [1]:[[2]] [1]:[2,[]] [1,2]:[]`
</center>
¿Cuál de las siguientes afirmaciones es cierta?

```Haskell
> [[1],[2]]     = [[1],[2]]
> [1]:[[2]]:[]  = [1]:[[[2]],[]] = --Error de tipado
> [1]:[[2]]     = [[1],[2]]
> [1]:[2,[]]    = -- Error de tipado
> [1,2]:[]      = [[1,2]]
```

\begin{itemize}
\item[$\square$] La primera, la tercera y al menos otra más son sintácticamente equivalentes entre sí
\item[$\square$] La segunda, la cuarta y al menos otra más son sintácticamente equivalentes entre sí
\item[$\boxtimes$] Las dos anteriores son falsas.
\end{itemize}

***
15. Considérense las expresiones
<center>
`[[1,2]] ([1]:[[2]]):[] (1:[2]):[] [1,2]:[] [1]:[2]:[]`
</center>
¿Cuál de las siguientes afirmaciones es cierta?

```Haskell
> [[1,2]]         = [[1,2]]
> ([1]:[[2]]):[]  = ([[1],[2]]):[] = [[[1],[2]]]
> (1:[2]):[]      = ([1,2]):[] = [[1,2]]
> [1,2]:[]        = [[1,2]]
> [1]:[2]:[]      = [1]:[[2]] = [[1],[2]]
```

\begin{itemize}
\item[$\boxtimes$] La primera, la tercera y al menos otra más son sintácticamente equivalentes entre sí
\item[$\square$] La segunda, la cuarta y al menos otra más son sintácticamente equivalentes entre sí
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

\newpage
16. ¿Cuántas de las siguientes expresiones son sintácticamente equivalentes a `[[1,2],[1]]`?
<center>
`[1:2:[],1:[]] [1:[2],[1]] [1,2]:[1]:[] (1:2:[]):[[1]]`
</center>

```Haskell
> [1:2:[],1:[]]   = [1:[2],[1]] = [[1,2],1]
> [1:[2],[1]]     = [[1,2],[1]]
> [1,2]:[1]:[]    = [1,2]:[[1]] = [[1,2],[1]]
> (1:2:[]):[[1]]  = (1:[2]):[[1]] = ([1,2]):[[1]] = [[1,2],[1]]
```

\begin{itemize}
\item[$\square$] Exactamente dos
\item[$\square$] Exactamente tres
\item[$\boxtimes$] Exactamente cuatro
\end{itemize}

\newpage
# Test *equivalencia de expresiones*.
## Aplicaciones
### NOTAS.
1. Añadir paréntesis a la expresión general y aplicar recursivamente, quitar paréntesis aquellos que a su izquierda tenga otro paréntesis de apertura.
2. Equivalencias de operadores.  
$a\oplus  b = (\oplus )ab = (\oplus b)a = ((\oplus) a)b = (a \oplus )b$.
3. Los operadores tienen menos precedencia que las funciones, `(+) (f a b) (g b) = (f a b) + (g b)  = f a b + g b`
4. Si `infixr <o> <0-9> => A<o>B<o>C = A<o>(B<o>C)` o si `infixl <o> <0-9> => A<o>B<o>C = (A<o>B)<o>C`

***

1. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = (f ((x y) y)) (f 0)
\end{verbatim}
\begin{verbatim}
  e_2 = f (x y y) (f 0)
\end{verbatim}
\begin{verbatim}
  e_3 = f (x y y) f 0
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (e_1) = ((f ((x y) y)) (f 0)) = ((f (x y y)) (f 0)) = (f (x y y) (f 0))
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \equiv e_2 \equiv e_3$
\item[$\square$] $e_1 \not\equiv e_2 \equiv e_3$
\item[$\boxtimes$] $e_1 \equiv e_2 \not\equiv e_3$
\end{itemize}

***

2. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f (f x (y^2)) y
\end{verbatim}
\begin{verbatim}
  e_2 = f ((f x) ((^) y 2)) y
\end{verbatim}
\begin{verbatim}
  e_3 = f (f x (y^) 2) y
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  e_2 = f ((f x) ((^) y 2)) y = f ((f x) (y ^ 2)) y = f (f x (y ^ 2)) y
  e_3 = f (f x (y^) 2) y = f (f x (y ^ 2)) y
\end{verbatim}

\begin{itemize}
\item[$\boxtimes$] $e_1 \equiv e_2 \equiv e_3$
\item[$\square$] $e_1 \equiv e_2 \not\equiv e_3$
\item[$\square$] $e_1 \not\equiv e_2 \not\equiv e_3$
\end{itemize}

\newpage

3. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f (z (y x)) ((z 0) x)
\end{verbatim}
\begin{verbatim}
  e_2 = (f (z (y x))) (z 0 x)
\end{verbatim}
\begin{verbatim}
  e_3 = f z (y x) (z 0 x)
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (e_1) = (f (z (y x)) ((z 0) x)) = (f (z (y x)) (z 0 x))
  (e_2) = ((f (z (y x))) (z 0 x)) = (f (z (y x)) (z 0 x))
  (e_3) = (f z (y x) (z 0 x))
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \equiv e_2 \equiv e_3$
\item[$\boxtimes$] $e_1 \equiv e_2 \not\equiv e_3$
\item[$\square$] $e_1 \not\equiv e_2 \not\equiv e_3\not\equiv e_1$
\end{itemize}

***

4. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f x (g x,y/2)
\end{verbatim}
\begin{verbatim}
  e_2 = f x (g x) (y/2)
\end{verbatim}
\begin{verbatim}
  e_3 = (f x) (g x,(/y) 2)
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (e_3) = ((f x) (g x,(/y) 2)) = (f x (g x, 2/y))
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \equiv e_2 \equiv e_3$
\item[$\boxtimes$] $e_1 \not\equiv e_2 \not\equiv e_3\not\equiv e_1$
\item[$\square$] $e_1 \equiv e_3 \not\equiv e_2$
\end{itemize}

***

5. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f x (g (x+1) y)
\end{verbatim}
\begin{verbatim}
  e_2 = (f x) (g (((+) x) 1) y)
\end{verbatim}
\begin{verbatim}
  e_3 = (f x) (g (x+1)) y
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (e_2) = ((f x) (g (((+) x) 1) y)) = (f x (g (x+1) y))
  (e_3) = ((f x) (g (x+1)) y) = (f x (g (x+1)) y)
\end{verbatim}

\begin{itemize}
\item[$\boxtimes$] $e_1 \equiv e_2 \not\equiv e_3$
\item[$\square$] $e_1 \not\equiv e_2 \equiv e_3$
\item[$\square$] $e_1 \equiv e_2 \equiv e_3$
\end{itemize}

\newpage

6. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f x (g x,y+1)
\end{verbatim}
\begin{verbatim}
  e_2 = f x (g x) (y+1)
\end{verbatim}
\begin{verbatim}
  e_3 = (f x) (g x,(+) y 1)
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (e_3) = ((f x) (g x,(+) y 1)) = (f x (g x, y+1))
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \equiv e_2 \equiv e_3$
\item[$\square$] $e_1 \not\equiv e_2 \not\equiv e_3$
\item[$\boxtimes$] $e_1 \equiv e_3 \not\equiv e_2$
\end{itemize}

***

7. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f x g (x+1) y
\end{verbatim}
\begin{verbatim}
  e_2 = (f x) (g (x+1) y)
\end{verbatim}
\begin{verbatim}
  e_3 = (f x) g ((+) x 1) y
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (e_2) = ((f x) (g (x+1) y)) = (f x (g (x+1) y))
  (e_3) = ((f x) g ((+) x 1) y) = (f x g (x+1) y)
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \equiv e_2 \not\equiv e_3$
\item[$\boxtimes$] $e_1 \equiv e_3 \not\equiv e_2$
\item[$\square$] Las dos anteriores son falsas
\end{itemize}

***

8. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f x 1 (x + y)
\end{verbatim}
\begin{verbatim}
  e_2 = (f x 1) (x + y)
\end{verbatim}
\begin{verbatim}
  e_3 = f x 1 ((+) x y)
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (e_2) = ((f x 1) (x + y)) = (f x 1 (x + y))
  (e_3) = (f x 1 ((+) x y)) = (f x 1 (x + y))
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \not\equiv e_2 \not\equiv e_3 \not\equiv e_1$
\item[$\square$] $e_1 \equiv e_3 \not\equiv e_2$
\item[$\boxtimes$] $e_1 \equiv e_2 \equiv e_3$
\end{itemize}

\newpage

9. Suponiendo la declaración `infixr 9 !`, considérense las expresiones (que solo difieren en los paréntesis):

$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = ((! g) f) ! ((h !) i) ! j
\end{verbatim}
\begin{verbatim}
  e_2 = ((!) (f ! g)) ((h ! i) ! j)
\end{verbatim}
\begin{verbatim}
  e_3 = (!) ((!) f g) ((!) ((!) h i) j)
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  e_1 = ((! g) f) ! ((h !) i) ! j = (f ! g) ! (h ! i) ! j
  e_2 = ((!) (f ! g)) ((h ! i) ! j) = (f ! g) ! ((h ! i) ! j)
  e_3 = (!) ((!) f g) ((!) ((!) h i) j) = (f ! g) ! ((h ! i) ! j)
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \equiv e_2 \equiv e_3$
\item[$\square$] $e_1 \equiv e_2 \not\equiv e_3$
\item[$\boxtimes$] $e_1 \not\equiv e_2 \equiv e_3$
\end{itemize}

***

10. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f x (y-1):z
\end{verbatim}
\begin{verbatim}
  e_2 = ((:) f) x ((-) y 1) z
\end{verbatim}
\begin{verbatim}
  e_3 = (: z) ((f x) ((-) y 1))
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (e_2) = (((:) f) x ((-) y 1) z) = (f : x (y-1) z)
  (e_3) = ((: z) ((f x) ((-) y 1))) = (((f x) (y-1)):z) = ((f x (y-1)):z)
  (e_3) = (f x (y-1):z)
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \equiv e_2 \equiv e_3$
\item[$\square$] $e_1 \not\equiv e_2 \not\equiv e_3 \not\equiv e_1$
\item[$\boxtimes$] $e_1 \equiv e_3  \not\equiv e_2$
\end{itemize}

***

11. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f ((g x) (x y)) x
\end{verbatim}
\begin{verbatim}
  e_2 = f (g x) (x y) x
\end{verbatim}
\begin{verbatim}
  e_3 = (f (g x (x y))) x
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  e_1 = f ((g x) (x y)) x = f (g x (x y)) x
  (e_3) = ((f (g x (x y))) x) = (f (g x (x y)) x)
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \equiv e_2 \not\equiv e_3$
\item[$\boxtimes$] $e_1 \equiv e_3 \not\equiv e_2$
\item[$\square$] $e_1 \equiv e_2 \equiv e_3$
\end{itemize}

\newpage

12. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f x y + z 4
\end{verbatim}
\begin{verbatim}
  e_2 = f x ((+) y (z 4))
\end{verbatim}
\begin{verbatim}
  e_3 = (+) ((f x) y) (z 4)
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  e_1 = f x y + z 4 = (f x y) + (z 4)
  e_2 = f x ((+) y (z 4)) = f x (y + (z 4))
  (e_3) = ((+) ((f x) y) (z 4)) = (((f x) y) + (z 4)) = ((f x y) + (z 4))
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \not\equiv e_2 \not\equiv e_3 \not\equiv e_1$
\item[$\square$] $e_1 \equiv e_2 \equiv e_3$
\item[$\boxtimes$] $e_1 \equiv e_3 \not\equiv e_2$
\end{itemize}

***

13. Considérense las expresiones (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  e_1 = f x + g z 4
\end{verbatim}
\begin{verbatim}
  e_2 = f x ((+) g (z 4))
\end{verbatim}
\begin{verbatim}
  e_3 = (+) (f x) (g z 4)
\end{verbatim}
\end{minipage}
\right.$

__Igual que el ejercicio anterior__.

\begin{verbatim}
  e_1 = f x + g z 4 =  (f x) + (g z 4)
  e_2 = f x ((+) g (z 4)) = f x (g + (z 4))
  (e_3) = ((+) (f x) (g z 4)) = ((f x) + (g z 4))
\end{verbatim}

\begin{itemize}
\item[$\square$] $e_1 \not\equiv e_2 \not\equiv e_3 \not\equiv e_1$
\item[$\square$] $e_1 \equiv e_2 \equiv e_3$
\item[$\boxtimes$] $e_1 \equiv e_3 \not\equiv e_2$
\end{itemize}

\newpage

## Flecha de los tipos.
### NOTAS.
1. Añadir paréntesis a la expresión general y aplicar recursivamente, quitar paréntesis aquellos que a su derecha tenga otro paréntesis de apertura.

***

1. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = (a -> a) -> (a -> a) -> (a -> a)
\end{verbatim}
\begin{verbatim}
  t_2 = (a -> a) -> (a -> a -> a -> a)
\end{verbatim}
\begin{verbatim}
  t_3 = (a -> a) -> (a -> a) -> a -> a
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_1) = ((a -> a) -> (a -> a) -> (a -> a))
  (t_1) = ((a -> a) -> (a -> a) -> a -> a)

  (t_2) = ((a -> a) -> (a -> a -> a -> a))
  (t_2) = ((a -> a) -> a -> a -> a -> a)
\end{verbatim}

\begin{itemize}
\item[$\square$] $t_1 \equiv t_2 \not\equiv t_3$
\item[$\boxtimes$] $t_1 \equiv t_3 \not\equiv t_2$
\item[$\square$] $t_1 \equiv t_2 \equiv t_3$
\end{itemize}

***

2. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = ((b -> a) -> a) -> ((a -> b) -> (b -> b))
\end{verbatim}
\begin{verbatim}
  t_2 = (b -> (a -> a)) -> (a -> b) -> b -> b
\end{verbatim}
\begin{verbatim}
  t_3 = (b -> a -> a) -> (a -> b -> b -> b)
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_1) = (((b -> a) -> a) -> ((a -> b) -> (b -> b)))
  (t_1) = (((b -> a) -> a) -> ((a -> b) -> b -> b))
  (t_1) = (((b -> a) -> a) -> (a -> b) -> b -> b)

  t_2 = (b -> (a -> a)) -> (a -> b) -> b -> b
  t_2 = (b -> a -> a) -> (a -> b) -> b -> b

  (t_3) = ((b -> a -> a) -> (a -> b -> b -> b))
  (t_3) = ((b -> a -> a) -> a -> b -> b -> b)
\end{verbatim}

\begin{itemize}
\item[$\square$] $t_1 \equiv t_2 \not\equiv t_3$
\item[$\square$] $t_1 \equiv t_2 \equiv t_3$
\item[$\boxtimes$] $t_1 \not\equiv t_2 \not\equiv t_3 \not\equiv t_1$
\end{itemize}

\newpage

3. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = (a -> (b -> a) -> a) -> b -> b
\end{verbatim}
\begin{verbatim}
  t_2 =  (a -> ((b -> a) -> a)) -> (b -> b)
\end{verbatim}
\begin{verbatim}
  t_3 = a -> b -> a -> a -> b -> b
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_2) = ((a -> ((b -> a) -> a)) -> (b -> b))
  (t_2) = ((a -> (b -> a) -> a) -> b -> b)
\end{verbatim}

\begin{itemize}
\item[$\boxtimes$] $t_1 \equiv t_2 \not\equiv t_3$
\item[$\square$] $t_1 \not\equiv t_2 \not\equiv t_3 \not\equiv t_1$
\item[$\square$] $t_1 \equiv t_3 \not\equiv t_2$
\end{itemize}

***

4. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = (a -> b -> a) -> (a -> b -> b)
\end{verbatim}
\begin{verbatim}
  t_2 = (a -> b -> a) -> a -> (b -> b)
\end{verbatim}
\begin{verbatim}
  t_3 = (a -> (b -> a)) -> a -> b -> b
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_1) = ((a -> b -> a) -> (a -> b -> b))
  (t_1) = ((a -> b -> a) -> a -> b -> b)

  (t_2) = ((a -> b -> a) -> a -> (b -> b))
  (t_2) = ((a -> b -> a) -> a -> b -> b)

  (t_3) = ((a -> (b -> a)) -> a -> b -> b)
  (t_3) = ((a -> b -> a) -> a -> b -> b)
\end{verbatim}

\begin{itemize}
\item[$\square$] $t_1 \equiv t_2 \not\equiv t_3$
\item[$\square$] $t_1 \equiv t_3 \not\equiv t_2$
\item[$\boxtimes$] $t_1 \equiv t_2 \equiv t_3$
\end{itemize}

***

5. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = a -> (b -> a -> a) -> b -> b
\end{verbatim}
\begin{verbatim}
  t_2 = (a -> ((b -> a) -> a)) -> b -> b
\end{verbatim}
\begin{verbatim}
  t_3 = a -> (b -> (a -> a)) -> (b -> b)
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  t_2 = (a -> ((b -> a) -> a)) -> b -> b
  t_2 = (a -> (b -> a) -> a) -> b -> b

  (t_3) = (a -> (b -> (a -> a)) -> (b -> b))
  (t_3) = (a -> (b -> a -> a) -> b -> b)
\end{verbatim}

\begin{itemize}
\item[$\square$] $t_1 \equiv t_2 \not\equiv t_3$
\item[$\boxtimes$] $t_1 \equiv t_3 \not\equiv t_2$
\item[$\square$] $t_1 \equiv t_2 \equiv t_3$
\end{itemize}

\newpage

6. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = (a -> b -> a -> a) -> b -> b
\end{verbatim}
\begin{verbatim}
  t_2 = a -> b -> a -> a -> b -> b
\end{verbatim}
\begin{verbatim}
  t_3 = (a -> b -> (a -> a)) -> (b -> b)
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_3) = ((a -> b -> (a -> a)) -> (b -> b))
  (t_3) = ((a -> b -> a -> a) -> b -> b)
\end{verbatim}

\begin{itemize}
\item[$\square$] $t_1 \equiv t_2 \not\equiv t_3$
\item[$\boxtimes$] $t_1 \equiv t_3 \not\equiv t_2$
\item[$\square$] $t_1 \equiv t_2 \equiv t_3$
\end{itemize}

***

7. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = a -> a -> (a -> a) -> (b -> b)
\end{verbatim}
\begin{verbatim}
  t_2 = a -> (a -> ((a -> a) -> b -> b))
\end{verbatim}
\begin{verbatim}
  t_3 = (a -> a) -> (a -> a) -> b -> b
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_1) = (a -> a -> (a -> a) -> (b -> b))
  (t_1) = (a -> a -> (a -> a) -> b -> b)

  (t_2) = (a -> (a -> ((a -> a) -> b -> b)))
  (t_2) = (a -> (a -> (a -> a) -> b -> b))
  (t_2) = (a -> a -> (a -> a) -> b -> b)
\end{verbatim}

\begin{itemize}
\item[$\square$] $t_1 \equiv t_2 \equiv t_3$
\item[$\square$] $t_1 \not\equiv t_2 \not\equiv t_3 \not\equiv t_1$
\item[$\boxtimes$] $t_1 \equiv t_2 \not\equiv t_3$
\end{itemize}

***

8. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = a -> ((a -> a) -> a)
\end{verbatim}
\begin{verbatim}
  t_2 = a -> (a -> a) -> a
\end{verbatim}
\begin{verbatim}
  t_3 = a -> a -> a -> a
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_1) = (a -> ((a -> a) -> a))
  (t_1) = (a -> (a -> a) -> a)
\end{verbatim}

\begin{itemize}
\item[$\boxtimes$] $t_1 \equiv t_2 \not\equiv t_3$
\item[$\square$] $t_1 \equiv t_3 \not\equiv t_2$
\item[$\square$] $t_1 \not\equiv t_2 \not\equiv t_3 \not\equiv t_1$
\end{itemize}

\newpage

9. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = (a -> a) -> (a -> a) -> (a -> a)
\end{verbatim}
\begin{verbatim}
  t_2 = a -> a -> (a -> a) -> (a -> a)
\end{verbatim}
\begin{verbatim}
  t_3 = (a -> a) -> (a -> a) -> a -> a
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_1) = ((a -> a) -> (a -> a) -> (a -> a))
  (t_1) = ((a -> a) -> (a -> a) -> a -> a)

  (t_2) = (a -> a -> (a -> a) -> (a -> a))
  (t_2) = (a -> a -> (a -> a) -> a -> a)
\end{verbatim}

\begin{itemize}
\item[$\square$] $t_1 \not\equiv t_2 \not\equiv t_3 \not\equiv t_1$
\item[$\boxtimes$] $t_1 \equiv t_3 \not\equiv t_2$
\item[$\square$] $t_1 \equiv t_2 \not\equiv t_3$
\end{itemize}

***

10. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = (b -> a -> a) -> (a -> b) -> b
\end{verbatim}
\begin{verbatim}
  t_2 = (b -> (a -> a)) -> ((a -> b) -> b)
\end{verbatim}
\begin{verbatim}
  t_3 = (b -> a -> a) -> a -> b -> b
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_2) = ((b -> (a -> a)) -> ((a -> b) -> b))
  (t_2) = ((b -> a -> a) -> (a -> b) -> b)
\end{verbatim}

\begin{itemize}
\item[$\square$] $t_1 \equiv t_2 \equiv t_3$
\item[$\square$] $t_1 \equiv t_3 \not\equiv t_2$
\item[$\boxtimes$] $t_1 \equiv t_2 \not\equiv t_3$
\end{itemize}

***

11. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = a -> b -> (c -> d)
\end{verbatim}
\begin{verbatim}
  t_2 = a -> (b -> c -> d)
\end{verbatim}
\begin{verbatim}
  t_3 = a -> b -> c -> d
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_1) = (a -> b -> (c -> d))
  (t_1) = (a -> b -> c -> d)

  (t_2) = (a -> (b -> c -> d))
  (t_2) = (a -> b -> c -> d)
\end{verbatim}

\begin{itemize}
\item[$\boxtimes$] $t_1 \equiv t_2 \equiv t_3$
\item[$\square$] $t_1 \equiv t_3 \not\equiv t_2$
\item[$\square$] $t_1 \not\equiv t_2 \not\equiv t_3$
\end{itemize}

\newpage

12. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = (a -> a) -> (a -> a) -> (a -> a)
\end{verbatim}
\begin{verbatim}
  t_2 = (a -> a) -> (a -> a) -> a -> a
\end{verbatim}
\begin{verbatim}
  t_3 = (a -> a) -> ((a -> a) -> (a -> a))
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_1) = ((a -> a) -> (a -> a) -> (a -> a))
  (t_1) = ((a -> a) -> (a -> a) -> a -> a)

  (t_3) = ((a -> a) -> ((a -> a) -> (a -> a)))
  (t_3) = ((a -> a) -> (a -> a) -> a -> a)
\end{verbatim}

\begin{itemize}
\item[$\square$] $t_1 \not\equiv t_2 \not\equiv t_3 \not\equiv t_1$
\item[$\square$] $t_1 \equiv t_3 \not\equiv t_2$
\item[$\boxtimes$] $t_1 \equiv t_2 \equiv t_3$
\end{itemize}

***

13. Considérense las expresiones de tipo (solo difieren en los paréntesis):
$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  t_1 = (a -> a -> a) -> (a -> a) -> (a -> a)
\end{verbatim}
\begin{verbatim}
  t_2 = (a -> (a -> a)) -> (a -> a) -> a -> a
\end{verbatim}
\begin{verbatim}
  t_3 = (a -> a -> a) -> ((a -> a) -> a -> a)
\end{verbatim}
\end{minipage}
\right.$

\begin{verbatim}
  (t_1) = ((a -> a -> a) -> (a -> a) -> (a -> a))
  (t_1) = ((a -> a -> a) -> (a -> a) -> a -> a)

  t_2 = (a -> (a -> a)) -> (a -> a) -> a -> a
  t_2 = (a -> a -> a) -> (a -> a) -> a -> a

  t_3 = ((a -> a -> a) -> ((a -> a) -> a -> a))
  t_3 = ((a -> a -> a) -> (a -> a) -> a -> a)
\end{verbatim}

\begin{itemize}
\item[$\square$] $t_1 \not\equiv t_2 \not\equiv t_3 \not\equiv t_1$
\item[$\square$] $t_1 \equiv t_3 \not\equiv t_2$
\item[$\boxtimes$] $t_1 \equiv t_2 \equiv t_3$
\end{itemize}

\newpage
# Test *evaluación de expresiones*.
### NOTAS.
1. Poner Paréntesis desde `(let ... in ...)` para todos los que encontremos, luego mirar si alguna de las variables no está en el scope
2. Una vez hecho eso, sustituir el valor de las variables declaradas en `let` por las que aparecen en `in`.
3. El orden declarado en el `let {}` __no importa el orden de inicialización__.
4. La declaración `let x = x in ...` es totalmente válida, __provoca una recursividad__.
5. El orden de __declaración__ de variables __en listas es importante__.
***

1. Considérense las expresiones siguientes:
```haskell
1> (let x=5 in x+x) + 3
2> let x=2 in let y=x+x in y*y*x
3> let y=(let x=2 in x+x) in y*y
4> let x=2 in let y=x+x in y*y
5> let y=x+x in let x=2 in y*y*x
6> let y=(let x=2 in x+x) in y*y*x
```
¿Cuántas de ellas son sintácticamente erróneas por problemas de ámbito de variables?

```haskell
1> (let x=5 in x+x) + 3              -- Bien, valor 13
2> (let x=2 in (let y=x+x in y*y*x)) -- Bien, valor 32
3> (let y=(let x=2 in x+x) in y*y))  -- Bien, valor 16
4> (let x=2 in (let y=x+x in y*y))   -- Bien, valor 8
5> (let y=x+x in (let x=2 in y*y*x)) -- Mal, x fuera del ámbito
6> (let y=(let x=2 in x+x) in y*y*x) -- Mal, x fuera del ámbito
```

\begin{itemize}
\item[$\square$] Exactamente tres de ellas
\item[$\boxtimes$] Exactamente dos de ellas
\item[$\square$] Todas están correctamente formadas
\end{itemize}

\newpage

2. Considérense las expresiones siguientes:
```haskell
1> (let x=5 in x+x) + 5
2> let x=2 in let y=x+x in y*x
3> let x=2 in let y=x+x in x
4> let x=y in let x=2 in y*y*x
5> let y=(let x=2 in x+x) in y*y
6> let y=(let x=2 in x+x) in y*y*x
```
¿Cuántas de ellas son sintácticamente erróneas por problemas de ámbito de variables?

```haskell
1> (let x=5 in x+x) + 5               -- Bien, valor 15
2> (let x=2 in (let y=x+x in y*x))    -- Bien, valor 8
3> (let x=2 in (let y=x+x in x))      -- Bien, valor 2
4> (let x=y in (let x=2 in y*y*x))    -- Mal, y fuera del ámbito
5> (let y=(let x=2 in x+x) in y*y)    -- Bien, valor 16
6> (let y=(let x=2 in x+x) in y*y*x)  -- Mal, x fuera del ámbito
```

\begin{itemize}
\item[$\square$] Exactamente una de ellas
\item[$\boxtimes$] Exactamente dos de ellas
\item[$\square$] Exactamente tres de ellas
\end{itemize}

***

3. Considérense las expresiones siguientes:
```haskell
1> (let x=5 in x+x) + x
2> let x=2 in let y=x+x in y*y*x
3> let y=x+x in let x=2 in y*y*x
4> let {y=x+x;x=2} in y*y*x
5> let y=(let x=2 in x+x) in y*y*x
6> let y=(let x=2 in 3) in y*y
```
¿Cuántas de ellas son sintácticamente erróneas por problemas de ámbito de variables?

```haskell
1> (let x=5 in x+x) + x               -- Mal, x fuera del ámbito
2> (let x=2 in (let y=x+x in y*y*x))  -- Bien, valor 32
3> (let y=x+x in (let x=2 in y*y*x))  -- Mal, x fuera del ámbito
4> (let {y=x+x;x=2} in y*y*x)         -- Bien, valor 32
5> (let y=(let x=2 in x+x) in y*y*x)  -- Mal, x fuera del ámbito
6> (let y=(let x=2 in 3) in y*y)      -- Bien, valor 9
```

\begin{itemize}
\item[$\square$] Exactamente dos de ellas
\item[$\boxtimes$] Exactamente tres de ellas
\item[$\square$] Exactamente cuatro de ellas
\end{itemize}

\newpage

4. Considérense las expresiones siguientes:
```haskell
1> (let x=5 in x+x) + 5
2> let x=2 in let y=x+x in y*x
3> let x=2 in let y=x+x in x
4> let x=y in let x=2 in y*y*x
5> let y=(let x=x in x+x) in y*y
6> let y=(let x=2 in x+x) in y*y*x
```
¿Cuántas de ellas son sintácticamente erróneas por problemas de ámbito de variables?

```haskell
1> (let x=5 in x+x) + 5               -- Bien, valor 15
2> (let x=2 in (let y=x+x in y*x))    -- Bien, valor 8
3> (let x=2 in (let y=x+x in x))      -- Bien, valor 4
4> (let x=y in (let x=2 in y*y*x))    -- Mal, y fuera del ámbito
5> (let y=(let x=x in x+x) in y*y)    -- Bien, valor indefinido, recursivo.
6> (let y=(let x=2 in x+x) in y*y*x)  -- Mal, x fuera del ámbito.
```

\begin{itemize}
\item[$\square$] Exactamente una de ellas
\item[$\boxtimes$] Exactamente dos de ellas
\item[$\square$] Exactamente tres de ellas
\end{itemize}

***

5. Considérense las expresiones siguientes:
```haskell
1> (let x=5 in x+x) + (let x=3 in 2*x)
2> let y=x+x in let x=2 in y*y*x
3> let x=2 in let y=x+x in y*y*x
4> let {y=x+x;x=2} in y*y*x
5> [i | i<-[1..j],j<-[0..100],mod j 3 == 0]
6> [i | j<-[0..100],i<-[1..j],mod j 3 == 0]
```
¿Cuántas de ellas son sintácticamente erróneas por problemas de ámbito de variables?

```haskell
1> (let x=5 in x+x) + (let x=3 in 2*x)       -- Bien, valor 16
2> (let y=x+x in (let x=2 in y*y*x))         -- Mal, x fuera del ámbito
3> (let x=2 in (let y=x+x in y*y*x))         -- Bien, valor 32
4> (let {y=x+x;x=2} in y*y*x)                -- Bien, valor 32
5> [i | i<-[1..j],j<-[0..100],mod j 3 == 0]  -- Mal, j declarada después
6> [i | j<-[0..100],i<-[1..j],mod j 3 == 0]  -- Bien
```

\begin{itemize}
\item[$\square$] Exactamente una de ellas
\item[$\boxtimes$] Exactamente dos de ellas
\item[$\square$] Exactamente tres de ellas
\end{itemize}

\newpage

6. Considérense las expresiones siguientes:
```haskell
1> let x=1:x in head x
2> (\x -> (\y -> x+y)) x
3> let x=[1,2,3] in let y= x!!2 in y*last x
4> let {y=2*x;x=5} in y*y*x
5> [i+j | i<-[1..j],j<-[0..100],mod j i == 0]
```
¿Cuántas de ellas son sintácticamente erróneas por problemas de ámbito de variables?

```haskell
1> let x=1:x in head x                          -- Bien, valor 1 (Ev. Perezosa)
2> (\x -> (\y -> x+y)) x                        -- Mal, x fuera de ámbito
3> (let x=[1,2,3] in (let y= x!!2 in y*last x)) -- Bien, valor 9
4> let {y=2*x;x=5} in y*y*x                     -- Bien, valor 500
5> [i+j | i<-[1..j],j<-[0..100],mod j i == 0]   -- Mal, j declarada después
```

\begin{itemize}
\item[$\boxtimes$] Exactamente dos de ellas
\item[$\square$] Exactamente tres de ellas
\item[$\square$] Exactamente cuatro de ellas
\end{itemize}

***

7. Considérense las expresiones siguientes:
```haskell
1> \x -> ((\y -> x) x)
2> \x -> ((\y -> x+y) y)
3> let y=[1,2,3] in let x= y!!1 in x*head y
4> let {y=2*x;x=5} in y*y*x
5> [i+j | i<-[1..100],j<-[0..i],mod j i == 0]
```
¿Cuántas de ellas son sintácticamente erróneas por problemas de ámbito de variables?

```haskell
1> \x -> ((\y -> x) x)                         -- Bien, valor es una función
2> \x -> ((\y -> x+y) y)                       -- Mal, y fuera de ámbito
3> let y=[1,2,3] in let x= y!!1 in x*head y    -- Bien, valor 2
4> let {y=2*x;x=5} in y*y*x                    -- Bien, valor 500
5> [i+j | i<-[1..100],j<-[0..i],mod j i == 0]  -- Bien
```

\begin{itemize}
\item[$\boxtimes$] Exactamente una de ellas
\item[$\square$] Tres o más de ellas
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

\newpage

8. Considérense las expresiones siguientes:
```haskell
1> \x -> ((\x y -> x+y) x y)
2> \x -> ((\x y -> x+y) x x)
3> let y= (let x = 1 in x+x) in x+y
4> let y= (let x = 1 in x+x) in y+y
5> [j | i<-[1..100],j<-[0..i]]
```
¿Cuántas de ellas son sintácticamente erróneas por problemas de ámbito de variables?

```haskell
1> \x -> ((\x y -> x+y) x y)         -- Mal, y fuera de ámbito
2> \x -> ((\x y -> x+y) x x)         -- Bien, valor una función
3> let y= (let x = 1 in x+x) in x+y  -- Mal, y fuera de ámbito
4> let y= (let x = 1 in x+x) in y+y  -- Bien, valor 4
5> [j | i<-[1..100],j<-[0..i]]       -- Bien
```

\begin{itemize}
\item[$\square$] Exactamente una de ellas
\item[$\square$] Tres o más de ellas
\item[$\boxtimes$] Las dos anteriores son falsas.
\end{itemize}

\newpage

# Test *Funciones*.
### NOTAS.
1. Composición de funciones `(.)::(b -> c) -> (a -> b) -> a -> c`

***

1. En el siguiente fragmento de código
```haskell
> data T a = A | (Int,T a)
> f x (y:xs) = y
```

```haskell
> data T a = A | (Int,T a)  -- Mal, falta <constructor> (Int, T a).
> f x (y:xs) = y            -- Bien, devuelve la cabeza del vector
```

\begin{itemize}
\item[$\boxtimes$] La definición de T contiene algún error sintáctico, pero la de f no.
\item[$\square$] La definición de f contiene algún error sintáctico, pero la de T no.
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

***

2. En el siguiente fragmento de código
```haskell
> data T a = A | (Int,T a)
> f x (x:xs) = True
> f x (y:xs) = f x xs
```

```haskell
> data T a = A | (Int,T a)  -- Mal, falta <constructor> (Int, T a)
> f x (x:xs) = True         -- Mal, doble declaración de x.
> f x (y:xs) = f x xs       -- Bien
```

\begin{itemize}
\item[$\square$] La definición de T contiene algún error, pero la de f no.
\item[$\square$] La definición de f contiene algún error, pero la de T no.
\item[$\boxtimes$] Las dos anteriores son falsas.
\end{itemize}

***

3. En el siguiente fragmento de código
```haskell
> data T a = A | B (Int,T a)  -- Bien
> f x (x:xs) = xs             -- Mal, doble definición de x.
```

\begin{itemize}
\item[$\square$] La definición de T contiene algún error, pero la de f no.
\item[$\boxtimes$] La definición de f contiene algún error, pero la de T no.
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

\newpage

4. Supongamos que `1::Int`, `(+)::Int->Int->Int`, y considérese la función `f` definida por las dos reglas siguientes:
```haskell
> f True x y = (x,y)
> f False y x = (y,x+1)
```
Reescribimos.
```haskell
> f True x y = (x,y)
> f False x y = (x,y+1)
```
Observamos que el __primer argumento es un__ `Bool`, el __segundo argumento puede ser cualquier tipo__ y el __tercero debe ser un número__ ya que se le suma 1, como estamos en los __enteros__ por la definición, es un `Int`.

\begin{itemize}
\item[$\boxtimes$] El tipo que se infiere para \verb|f| es \verb|Bool -> a -> Int -> (a,Int)|
\item[$\square$] El tipo que se infiere para \verb|f| es \verb|Bool -> Int -> Int -> (Int,Int)|
\item[$\square$] \verb|f| está mal tipada.
\end{itemize}

***

5. Considérese la función definida por `f x y = y x x`. El tipo de `f` es:

```haskell
> f x y = y x x
```
El __primer argumento puede cualquier tipo__ (`x=a`), luego vemos que `y` es una __función con dos argumentos__ que depende de `x`, que internamente puede devolver lo que quiera (Independientemente de la operación `a`).
```haskell
> f::a->(a->a->b)->b
```
\begin{itemize}
\item[$\boxtimes$] \verb|a -> (a -> a -> b) -> b|
\item[$\square$] \verb|a -> (a -> a -> a) -> a|
\item[$\square$] No está bien tipada
\end{itemize}

***

6. Considérese la función definida por `f g x = x (g True) g`. El tipo de `f` es:
Descomponemos desde más profundo a menos.
```Haskell
-- g Coge un booleano y devuelve lo que sea.
> g::(Bool -> c)
-- (g True) es constante por lo que es lo que se devolvió.
> x::c -> (Bool -> c) -> b
-- Devuelve el valor de x.
> f::(Bool -> c) -> (c -> (Bool -> c) -> b) -> b
```

\begin{itemize}
\item[$\boxtimes$] $\forall a, b$.\verb|(Bool->a) -> (a -> (Bool->a) -> b) -> b|
\item[$\square$] $\forall a$.\verb|(Bool->a) -> (a -> (Bool->a) -> a) -> a|
\item[$\square$] Está mal tipada.
\end{itemize}

\newpage

7. Considérese la función definida por `f g x = x g g`. El tipo de `f` es:

```Haskell
-- g Puede ser lo que sea
> g::a
-- x tiene como argumento dos g y devuelve tipo a.
> x::a -> a -> b
-- Juntamos todo
> f::a -> (a -> a -> b) -> b
```

\begin{itemize}
\item[$\boxtimes$] $\forall a, b$.\verb|a -> (a -> a -> b) -> b|
\item[$\square$] $\forall a, b$.\verb|(a -> a -> b) -> a -> b|
\item[$\square$] Está mal tipada.
\end{itemize}

***

8. Considérese la función definida por `f g x = x g g`. El tipo de `f` es:

__Igual que el ejercicio anterior__.
\begin{itemize}
\item[$\square$] Está mal tipada.
\item[$\square$] $\forall a$.\verb|(a -> a -> a) -> a -> a|
\item[$\boxtimes$] $\forall a, b$.\verb|a -> (a -> a -> b) -> b|
\end{itemize}

***

9. Considérese la función definida por `f x y = x (y x)`. El tipo de `f` es:

```Haskell
-- Vemos que x acepta y y nos devuelve lo que sea.
> x::a -> b
-- Vemos que y depende de x y lo que se supone que y devolvió.
> y::(a -> b) -> a
-- Juntamos todo
> f::(a -> b) -> ((a -> b) -> a) -> b
```

\begin{itemize}
\item[$\boxtimes$] \verb|(a -> b) -> ((a -> b) -> a) -> b|
\item[$\square$] \verb|(a -> b -> a) -> (b -> a) -> a|
\item[$\square$] Está mal tipada.
\end{itemize}

***

10. Considérese la función definida por `f x y = y (x x)`. El tipo de `f` es:

Vemos que es un __tipo recurivo__ ya que `x` es el propio argumento de su función, por lo que está __mal definido__.

\begin{itemize}
\item[$\square$] \verb|(a -> a -> a) -> (a -> a) -> a|
\item[$\square$] \verb|(a -> b -> a) -> (b -> a) -> a|
\item[$\boxtimes$] No está bien tipada.
\end{itemize}

\newpage

11. Considérese la función definida por `f x y = x x y`. El tipo de `f` es:

Vemos que es un __tipo recurivo__ ya que `x` es el propio argumento de su función, por lo que está __mal definido__.

\begin{itemize}
\item[$\square$] \verb|(a -> a -> a) -> (a -> a) -> a|
\item[$\square$] \verb|(a -> b -> a) -> (b -> a) -> a|
\item[$\boxtimes$] No está bien tipada.
\end{itemize}

***

12. Considérese la función definida por `f x y = x (y y)`. El tipo de `f` es:
__Igual que el ejercicio 10__
\begin{itemize}
\item[$\square$] \verb|(a -> b) -> (a -> a) -> b|
\item[$\square$] \verb|(a -> a) -> (a -> a) -> a|
\item[$\boxtimes$] No está bien tipada.
\end{itemize}

***

13. Considérese la función definida por `f g = g (f g)`. El tipo de `f` es:

```Haskell
-- El tipo de f deve devolverlo también g
> f:: (...) -> a
> g:: (...) -> a
-- Vemos que g necesita un argumento que tiene que ser al mismo de f.
> g:: a -> a
-- Juntamos todo.
> f:: (a -> a) -> a
-- Ejemplo.
> g x = True
> f g = g (f g) = True
```

\begin{itemize}
\item[$\square$] \verb|a -> a -> a|
\item[$\boxtimes$] \verb|(a -> a) -> a|
\item[$\square$] No está bien tipada.
\end{itemize}

\newpage

14. Sea `f` definida por `f g x = x (x g)`. El tipo de `f` es:

```Haskell
-- Suponemos que g es de tipo a.
> g::a
-- Ahora vemos que x tiene como argumento g.
> x::a -> b
-- Ahora vemos que x vuelve a englobar a x. Eso implica que b = a.
> x::a -> a
-- Juntamos esto.
> f a -> (a -> a) -> a
```

\begin{itemize}
\item[$\square$] $\forall a$.\verb|a -> (a -> b) -> b|
\item[$\boxtimes$] $\forall a$.\verb|a -> (a -> a) -> a|
\item[$\square$] Está mal tipada.
\end{itemize}

***

15. Sea `f` definida por `f g x = x x g`. El tipo de `f` es:

```Haskell
-- Vemos que x necesita dos argumentos y que uno de sus argumentos
-- es el tipo que devuelve y g es un valor arbitrario.
> g::a
> x::b -> a -> b
-- juntamos todo.
> f::a -> (b -> a -> b) -> b
```

\begin{itemize}
\item[$\square$] $\forall a$.\verb|a -> (a -> a -> a) -> a|
\item[$\boxtimes$] $\forall a$.\verb|a -> (b -> a -> b) -> b|
\item[$\square$] Está mal tipada.
\end{itemize}

***

16. Sea `f` definida por `f x y z = x (y z)`. El tipo de `f` es:

```Haskell
-- z es un tipo cualquiera e "y" es una función que toma z y devuelve otro tipo.
> z::c
> y::c -> a
-- por último, x recibe otro argumento que es "y" y devuelve otro valor.
> x::a -> b
-- juntamos todo.
> f::(a -> b) -> (c -> a) -> c -> b
```

\begin{itemize}
\item[$\square$] \verb|(a -> b) -> (b -> c) -> a -> c|
\item[$\boxtimes$] \verb|(a -> b) -> (c -> a) -> c -> b|
\item[$\square$] Está mal tipada.
\end{itemize}

\newpage

17. Sea `f` definida por `f x g = x (x (g True)))`. El tipo de `f` es:

```Haskell
-- Vemos que g es una función de un argumento booleano y devuelve cualquier tipo.
> g::Bool -> a
-- x toma el tipo de g y devuelve cualquier otro tipo
> x::a -> c
-- Pero ahora vemos que x vuelve a englobar x, por lo que c == a.
> x::a -> a
-- finalmente.
> (a -> a) -> (Bool -> a) -> a
```

\begin{itemize}
\item[$\square$] $\forall a, b$.\verb|(a -> b) -> (Bool -> a) -> b|
\item[$\boxtimes$] $\forall a$.\verb|(a -> a) -> (Bool -> a) -> a|
\item[$\square$] Está mal tipada.
\end{itemize}

***

18. Sea `f` definida por `f x y = (x y).(x y)`. El tipo de `f` es:

```Haskell
-- Vemos que x tiene 1 argumento, que es y (cuyo valor es un tipo)
> y::a
> x::a -> b
-- Ahora bien la solución es la composición consigomismo
-- (x::a -> b) . b (Ya que (x y) (solución x y = b) )
-- El resultado es otra función (sin evaluar) b -> b
> f::(a -> b -> b) -> a -> (b -> b)
```

\begin{itemize}
\item[$\square$] \verb|(a -> a) -> (a -> a)|
\item[$\boxtimes$] \verb|(a -> b) -> (a -> b) -> (a -> b)|
\item[$\square$] \verb|(a -> b -> b) -> a -> b -> b|
\end{itemize}

***

19. Sea `f` definida por `f x y = x (x y)`. El tipo de `f` es:

```Haskell
-- Vemos que x tiene 1 argumento, que es y (cuyo valor es un tipo)
> y::a
> x::a -> b
-- Ahora vemos que x vuelve a englobar a x => b = a.
> x::a -> a
-- Juntamos todo
> f::(a -> a) -> a -> a
```

\begin{itemize}
\item[$\boxtimes$] \verb|(a -> a) -> (a -> a)|
\item[$\square$] \verb|(a -> b) -> a -> b|
\item[$\square$] \verb|a -> b -> a|
\end{itemize}

\newpage

# Test *Clase de tipos*.
### NOTAS.
1. Los __operadores__  `(<=), (=>), ..., (==), (\=)` obligan a que los tipos sean __necesariamente Ordenables__ (`Ord`) o que deriven de este y __ambos__ sean del __mismo tipo__.
2. Los __operadores aritméticos__ obligan a __derivar de__ `Num`.

***

1. El tipo que inferirá Haskell, teniendo en cuenta clases de tipos, para una función `f` definida por
`f x y z = if x <= y then z + 1 else z` será:

```Haskell
--
> if x <= y ... -- Deben ser comparable, por lo que tipo de x e y son Ord a.
> z + 1         -- Debe ser numérico, para poder sumarse, Num b.
-- Juntamos todo
> (Ord a, Num b) => a -> a -> b -> b
```

\begin{itemize}
\item[$\square$] \verb|f :: Num a => a -> a -> a -> a|
\item[$\boxtimes$] \verb|f :: (Ord a, Num b) => a -> a -> b -> b|
\item[$\square$] \verb|f :: (Ord a, Num a) => a -> a -> a -> a|
\end{itemize}

***

2. El tipo que inferirá Haskell, teniendo en cuenta clases de tipos, para una función `f` definida por
`f x y z = if x <= y z then z + 2 else z` será:

```Haskell
--
> if x <= y z ... -- Deben ser comparable, por lo que tipo de x e y son Ord a.
> y z             -- Es una función de un argumento b y devuleve a. y::b->a
> z + 2           -- z debe ser numérico (y es arg de y), y::Num b->a, z::Num b.
-- Juntamos todo
> (Ord a, Num b) => a -> (b -> a) -> b -> b
```

\begin{itemize}
\item[$\square$] \verb|f :: Num a => a -> a -> a -> a|
\item[$\square$] \verb|f :: (Ord a, Num b) => a -> b -> b -> a|
\item[$\boxtimes$] \verb|f :: (Ord a, Num b) => a -> (b -> a) -> b -> b|
\end{itemize}

\newpage

3. El tipo que inferirá Haskell, teniendo en cuenta clases de tipos, para una función `f` definida por
`f x y z = if x then y <= z else x` será:

```Haskell
--
> if x       -- Debe un booleano, por lo que x::Bool.
> y <= z     -- Deben ser comparables, por lo que y, z::Bool, devulve Bool.
> x          -- Es un Bool, devuelve Bool
-- Juntamos todo
> (Ord a) => Bool -> a -> a -> Bool
```

\begin{itemize}
\item[$\square$] \verb|f :: (Ord a, Bool a) => a -> a -> a -> a|
\item[$\boxtimes$] \verb|f :: Ord a => Bool -> a -> a -> Bool|
\item[$\square$] Esa definición dará un error de tipos
\end{itemize}

***

4. El tipo que inferirá Haskell, teniendo en cuenta clases de tipos, para una función `f` definida por
`f x y z = if x <= y then z+1 else x` será:

```Haskell
--
> if x <= y  -- Deben ser comparables, x, y::Ord x
> z + 1      -- Debe ser numérico.
> x          -- También debe ser numérico (por z + 1) => y también lo es.
-- Juntamos todo
> (Ord a, Num a) => a -> a -> a -> a
```

\begin{itemize}
\item[$\boxtimes$] \verb|f :: (Num a, Ord a) => a -> a -> a -> a|
\item[$\square$] \verb|f :: (Ord a, Num b) => a -> a -> b -> b|
\item[$\square$] \verb|f :: (Ord a, Num b) => a -> a -> b -> a|
\end{itemize}

***

5. El tipo que inferirá Haskell, teniendo en cuenta clases de tipos, para una función `f` definida por
`f x y z = if x <= y then z else not x` será:

```Haskell
--
> if x <= y  -- Deben ser comparables, x, y::Ord x
> z          -- Puede ser cualquier tipo b
> not x      -- x debe ser un Bool => y::Bool, que Bool ya es Ord y z = Bool.
-- Juntamos todo
> f::Bool -> Bool -> Bool -> Bool
```

\begin{itemize}
\item[$\square$] \verb|f :: (Ord a, Bool a) => a -> a -> a -> a|
\item[$\square$] \verb|f :: Ord a => Bool -> a -> a -> Bool|
\item[$\boxtimes$] \verb|Bool -> Bool -> Bool -> Bool|
\end{itemize}

\newpage

6. El tipo que inferirá Haskell, teniendo en cuenta clases de tipos, para una función `f` definida por
`f x y z = z (x <= y+1)` será:

```Haskell
> y + 1 -- Debe ser numérico.
> x <= y + 1 -- Deben ser numéricos y a su vez son comparables.
> z (..<=..) -- Función que acepta un Bool. y devuelve lo que sea )b).
-- Juntamos todo.
> f::(Num a, Ord a) => a -> a -> (Bool -> b) -> b
```

\begin{itemize}
\item[$\boxtimes$] \verb|f :: (Num a, Ord a) => a -> a -> (Bool -> b) -> b|
\item[$\square$] \verb|f :: (Ord a, Num b, Ord b) => a -> b -> (Bool -> c) -> c|
\item[$\square$] Dará un error de tipos
\end{itemize}

***

7. El tipo que inferirá Haskell, teniendo en cuenta clases de tipos, para una función `f` definida por
`f x y z = if x <= y then z + x else z` será:

```Haskell
> z + x   -- Deben ser numéricos para poder sumarse (Es lo que devuelven).
> x <= y  -- x e y deben ser comparables y por el apartado anterior, numéricos.
-- Juntamos todo.
> f::(Num a, Ord a) => a -> a -> a -> a
```

\begin{itemize}
\item[$\square$] \verb|f :: Num a => a -> a -> a -> a|
\item[$\square$] \verb|f :: (Ord a, Num b) => a -> a -> b -> b|
\item[$\boxtimes$] \verb|f :: (Ord a, Num a) => a -> a -> a -> a|
\end{itemize}

***

8. El tipo que inferirá Haskell, teniendo en cuenta clases de tipos, para una función `f` definida por
`f x y = if x <= 0 then y + 1 else y` será:

```Haskell
> y + 1    -- y debe de ser numérico Num b. (Cualquier Numero).
> x <= 0   -- x debe de ser ordenable y numérico. Ord, Num::a.
-- Juntamos todo.
f::(Num b, Num a, Ord a) => a -> b -> b
```

\begin{itemize}
\item[$\square$] \verb|f :: Num a => a -> a -> a|
\item[$\boxtimes$] \verb|f :: (Num a, Ord a, Num b) => a -> b -> b|
\item[$\square$] \verb|f :: (Ord a, Num a) => a -> a -> a|
\end{itemize}

\newpage

9. El tipo que inferirá Haskell, teniendo en cuenta clases de tipos, para una función `f` definida por
`f x y = if x == y+1 then y else y+1` será:

```Haskell
> y + 1    -- y debe de ser numérico Num a.
> x == y+1 -- x e y deben de ser ordenable y numéricos Ord,Num::a
-- Juntamos todo.
f::(Num a, Ord a) => a -> a -> a
```

\begin{itemize}
\item[$\square$] \verb|f :: Eq (Num a) => a -> a -> Num a|
\item[$\boxtimes$] \verb|f :: (Eq a, Num a) => a -> a -> a|
\item[$\square$] \verb|f :: (Eq a, Num b) => a -> b -> b|
\end{itemize}

\newpage

10. El tipo que inferirá Haskell, teniendo en cuenta clases de tipos, para una función `f` definida por
`f x y z = if not x then z <= y else x` será:

```Haskell
> not x  -- x debe ser Bool.
> z <= y -- z e y deben ser Ordeables.
-- Juntamos todo.
f::(Ord a) => Bool -> a -> a -> Bool
```

\begin{itemize}
\item[$\square$] \verb|f :: Ord Bool => Bool -> Bool -> Bool -> Bool|
\item[$\square$] \verb|f :: Bool -> Bool -> Bool -> Bool|
\item[$\boxtimes$] \verb|f :: Ord a => Bool -> a -> a -> Bool|
\end{itemize}

***

11. ¿Cuál de los siguientes tipos para `f` hacen que la expresión `(curry f 0) . (|| True)` esté bien tipada?

``` haskell
-- Supongamos que lo aplicamos a un X
> ((curry f 0) . (|| True)) X = (curry f 0 ((|| True) X))
> (curry f 0 ((|| True) X)) = (curry f 0 (X || True))
> f (0, X || True)
> f::(Int, Bool) -> a
```

\begin{itemize}
\item[$\boxtimes$] \verb|f::(Int,Bool) -> Int|
\item[$\square$] \verb|f:: Int -> Bool -> Int|
\item[$\boxtimes$] Esa expresión está mal tipada, sea cual sea el tipo de \verb|f|.
\end{itemize}

12. ¿Cuál de los siguientes tipos para `f` hacen que la expresión `(|| True).(uncurry f)` esté bien tipada?

``` haskell
-- Supongamos que lo aplicamos a un X
> ((|| True).(uncurry f)) X = (|| True)(uncurry f X)
> (uncurry f X) || True
> f: X -> Bool y X = (a, b)
> f::(a, b) -> Bool -- Si a, b = Int.
```

\begin{itemize}
\item[$\boxtimes$] \verb|f::(Int,Int)-> Bool|
\item[$\square$] \verb|f:: Int -> Bool -> Int|
\item[$\square$] Esa expresión está mal tipada, sea cual sea el tipo de \verb|f|.
\end{itemize}

\newpage

# Test *Tipo de datos en Métodos Prolog*.
### NOTAS.

***

1. Sea `f` de tipo `t -> t` , y `unaLista` de tipo `[t]`. El tipo de la expresión `map (take 2) (map (iterate f) unaLista)`
es:

```haskell
> Supongamos que unaLista = [a1, .., an]
> map (iterate f) unaLista => [iterate f a1, ..., iterate f an] = ls
> map (take 2) ls => [take 2 (iterate f a1), ..., take 2 (iterate f an)]
-- Sabemos que iterate es un vector [a1, ..., f(..f(a1)..)]
-- Y Por la evaluación Perezosa es equivalente
> [[a1, f a1], ... [an, f an]]
```

\begin{itemize}
\item[$\square$] \verb|[t]|
\item[$\boxtimes$] \verb|[[t]]|
\item[$\square$] Esa expresión está mal tipada.
\end{itemize}

***

2. Sea `f` de tipo `t -> t` , y `unaLista` de tipo `[t]`. El tipo de la expresión `map (iterate f) (map (take 2) unaLista)`
es:

```haskell
> Supongamos que unaLista = [a1, .., an]
> map (take 2) unaLista = [take 2 a1, .., take 2 an]
-- Vemos que da un error si ai != [a11, .., a1k]
> [take 2 a1, .., take 2 an] = [[a11, a12], .., [ak1, akn]] = ls
> map (iterate f) ls = [iterate f [a11, a12], .., iterate f [ak1, akn]]
```

\begin{itemize}
\item[$\square$] \verb|[t]|
\item[$\boxtimes$] \verb|[[t]]|, si es que t es de la forma \verb|[t']|
\item[$\square$] Esa expresión está en cualquier caso mal tipada.
\end{itemize}

***

3. ¿Cuál de los siguientes tipos para la expresión `e` hace que la expresión `zipWith filter [(> 0),(< 0)] e` esté bien tipada?

```haskell
-- Supongamos que e es una lista, e = [a1, .., an]
> zipWith filter [(> 0),(< 0)] e = [filter (> 0) a1, filter (< 0) a2]
-- Vemos que filter actua con una función y una lista => ai = [ai1, ..., aik]
> [filter (> 0) [a11, ..., a1k], filter (< 0) [a21, a2k]]
```

\begin{itemize}
\item[$\square$] \verb|[Int]|
\item[$\boxtimes$] \verb|[[Int]]|
\item[$\square$] \verb|[(Int, Int)]|
\end{itemize}

\newpage

4. ¿Cuál de los siguientes tipos para la expresión `e` hace que la expresión
`zipWith filter e [[1..4],[-2..3]]` esté bien tipada?

```Haskell
-- Sabemos que e tiene que ser una lista, e = [e1,..,en]
> zipWith filter e [[1..4],[-2..3]] = [filter e1 [1..4], filter e2 [-2..3]]
-- Vemos que e1,2::Int->Bool
```

\begin{itemize}
\item[$\square$] \verb|Int -> Bool|
\item[$\boxtimes$] \verb|[Int -> Bool]|
\item[$\square$] Las dos anteriores son falsas
\end{itemize}

***

5. ¿Cuál de los siguientes tipos para la expresión `e` hace que la expresión
`takeWhile e zip (iterate not True) [0..10]` esté bien tipada?

```Haskell
> iterate not True = [True, False, True, ...] = tf
> zip tf [0..10] = [(True, 0), (False, 1), ... (True, 10)] = zp
-- takeWhile filtra los n primeros de zp hasta encontrar uno que no cumpla.
> e::(Bool, Int) -> Bool
```

\begin{itemize}
\item[$\square$] \verb|Int -> Int|
\item[$\square$] \verb|Int -> Bool|
\item[$\boxtimes$] \verb|(Bool,Int) -> Bool|
\end{itemize}

***

6. ¿Cuál de los siguientes tipos para la expresión `e` hace que la expresión
`zipWith e (iterate not True) (iterate (+ 1) 0)` esté bien tipada?

```Haskell
> iterate not True = [True, False, True, ...] = tf -- Tipo Bool
> iterate (+ 1) 0 = [0, 1, 2, ...] = na            -- Tipo Int
> zipWith e tf na = [e tf1 na1, e tf2 na2, ...]
-- Vemos que
e::Bool -> Int -> a
-- Podemos sustituir a = Char como la 3a opción.
```

\begin{itemize}
\item[$\square$] \verb|[Bool] -> [Int] -> [Bool]|
\item[$\square$] \verb|[Bool] -> [Int] -> [(Bool,Int)]|
\item[$\boxtimes$] \verb|Bool -> Int -> Char|
\end{itemize}

\newpage

8. ¿Cuál de los siguientes tipos para la expresión `e` hace que la expresión
`(head.e) (zip (iterate not True) [1..5])` esté bien tipada?

```Haskell
> iterate not True = [True, False, True, ...] = tf -- Tipo Bool
> zip (iterate not True) [1..5] = [(True, 1), ..., (False, 5)] = zp
> (head.e) zp = head (e zp)
-- Vemos que (e zp) debe devolver un vector para compilar.
> [(Bool, Int)] -> [a]
-- Podemos cambiuar a por Int.
```

\begin{itemize}
\item[$\boxtimes$] \verb|[(Bool,Int)] -> [Int]|
\item[$\square$] \verb|(Bool,Int) -> Int|
\item[$\square$] \verb|(Bool,Int) -> [Int]|
\end{itemize}

\newpage

# Test *Definiciones de Tipos*.
### NOTAS.
```
data <constructor> <template> = <nombre> [<tb1> <tbn>] | ...
```
1. `Template` es un dato básico (`Int`, `Bool`, `Integral`...).
2. Donde tbi son tipos de datos:
    1. `Datos Básicos` (`Int`, `Bool`, `Integral`...).
    2. `[Datos Básicos]` (Un __vector de Tipos__)
    3. El valor del `template`
    4. `(<tbi>, .., <tbk>)`.
        1. Puede ser uno de los anteriores.
        2. Puede ser el propio `<constructor>` (Con argumento si lo tiene).

***

1. ¿Cuántas de las siguientes definiciones de tipos (independientes unas de otras) son correctas?
```haskell
data Tip = A | C Int Tip | (Int,Int,Tip)
data Tap = A | C Int Tap | D Int Int Tap
data Top = A | C a Top | D a b Top
```

```haskell
1> Tip::(Int,Int,Tip) -- Falta nombre => Mal.
2> Tap                -- Bien.
3> Top::a,Top::b      -- ¿a, b? => Mal.
```

\begin{itemize}
\item[$\square$] Las tres
\item[$\square$] Ninguna de las tres
\item[$\boxtimes$] Una de las tres
\end{itemize}

***

2. ¿Cuántas de las siguientes definiciones de tipos (independientes unas de otras) son correctas?
```haskell
data Tip = A | C Int Tip | C (Int,Int,Tip)
data Tap = A | C Int Tap | D (Int,Int,Tap)
data Top a = A | C a | D a a
```

```haskell
1> Tip::C      -- Declarada dos veces.
2> Tap         -- Bien.
3> Top         -- Bien.
```

\begin{itemize}
\item[$\square$] Una de las tres
\item[$\boxtimes$] Dos de las tres
\item[$\square$] Las tres
\end{itemize}

\newpage

> __Comparar los tipos que derivan la clase Eq o Ord, argumentos lexicográficamente__.

3. Considérese la definición del tipo  
`data T = A | B | C T T deriving (Eq,Ord)`.  
¿Cuál de las siguientes afirmaciones es cierta?

```haskell
data T = A | B | C T T deriving (Eq,Ord)
--       1 | 2 | 3
1> A <= B && B <= C A A  
-- Cambiamos por los números.
1> (1 <= 2 && 2 <= 3) = True && True = True
```

\begin{itemize}
\item[$\boxtimes$] \verb|A <= B && B <= C A A| se evalúa a \verb|True|
\item[$\square$] \verb|A <= B && B <= C A A| se evalúa a \verb|False|
\item[$\square$] \verb|C loop loop == C loop loop| se evalúa a \verb|True|, donde \verb|loop| está definido por \verb|loop = loop|.
\end{itemize}

***

4. Considérese la definición del tipo  
`data T = A | B | C T T deriving (Eq,Ord)`  
y la función `mal = head []`.  
¿Cuál de las siguientes afirmaciones es cierta?

```haskell
data T = A | B | C T T deriving (Eq,Ord)
--       1 | 2 | 3
mal = head[]
--    undefined (Bottom _|_)
-- Cambiamos por los números.
1> C mal A <= C mal B
1> 1 _|_ 1 <= 1 _|_ 2 -- Error, _|_ <= _|_
2> C A mal == C B mal
2> 3 1 _|_ == 3 2 _|_ -- False (Evaluación perezosa)

```

\begin{itemize}
\item[$\square$] \verb|C mal A <= C mal B| se evalúa a \verb|True|
\item[$\boxtimes$] \verb|C A mal == C B mal| se evalúa a \verb|False|
\item[$\square$] \verb|A <= C mal mal && B <= C mal mal| se evalúa a \verb|False|.
\end{itemize}

\newpage

5. Considérese la definición del tipo  
`data T = A | B | C T T deriving (Eq,Ord)`  
y la función `loop = loop`.  
¿Cuál de las siguientes afirmaciones es cierta?

```haskell
data T = A | B | C T T deriving (Eq,Ord)
--       1 | 2 | 3
loop = loop -- error (Bottom _|_)
-- Cambiamos por los números.
1> A <= B && B <= C loop loop
1> 1 <= 2 && 2 <= 3 _|_  _|_ -- True (Evaluación perezosa)
```

\begin{itemize}
\item[$\boxtimes$] \verb|A <= B && B <= C loop loop | se evalúa a \verb|True|
\item[$\square$] \verb|A <= B && B <= C loop loop| se evalúa a \verb|False|
\item[$\square$] \verb|C loop loop == C loop loop| se evalúa a \verb|True|.
\end{itemize}

***

6. Considérese la definición del tipo  
`data T = A | B | C T T deriving (Eq,Ord)`  
y la función `loop = loop`.  
¿Cuál de las siguientes afirmaciones es __falsa__?

```haskell
data T = A | B | C T T deriving (Eq,Ord)
--       1 | 2 | 3
loop = loop -- error (Bottom _|_)
-- Cambiamos por los números.
1> A <= C loop loop && B <= C loop loop
1> 1 <= 3 _|_  _|_  && 2 <= 3 _|_  _|_  -- Cierto
2> C A loop == C B loop
2> 3 2 _|_  == 3 1 _|_  -- 3==3,2!=1 => False -- Cierto
```

\begin{itemize}
\item[$\boxtimes$] \verb|loop <= B && B <= C loop loop| se evalúa a \verb|True|
\item[$\square$] \verb|A <= C loop loop && B <= C loop loop| se evalúa a \verb|True|
\item[$\square$] \verb|C A loop == C B loop| se evalúa a \verb|False|.
\end{itemize}

\newpage

7. Considérese la definición del tipo  
`data T = A | B | C T T deriving (Eq,Ord)`  
y la función `mal = head []`.  
¿Cuál de las siguientes afirmaciones es __cierta__?

```haskell
data T = A | B | C T T deriving (Eq,Ord)
--       1 | 2 | 3
mal =  head [] -- error (Bottom _|_)
1> C mal A <= C mal B
1> 3 _|_ 2 <= C _|_ B               -- Mal, _|_=_|_
2> A <= C mal mal && B <= C mal mal
2> 1 <= 3 _|_ _|_ && 2 <= 3 _|_ _|_ -- True, Evaluación Perezosa
```

\begin{itemize}
\item[$\square$] \verb|C mal A <= C mal B| se evalúa a \verb|True|
\item[$\boxtimes$] \verb|A <= C mal mal && B <= C mal mal| se evalúa a \verb|True|
\item[$\square$] \verb|C A mal == C B mal | se evalúa a \verb|True|.
\end{itemize}

***

8. Considérese la definición del tipo  
`data T = A | B | C T T deriving (Eq,Ord)`  
y la función `mal = head []`.  
¿Cuál de las siguientes afirmaciones es __cierta__?

+ `C mal A <= C mal B` se evalúa a `True`
+ `C A mal == C B mal` se evalúa a `True`
+ `A <= C mal mal && B <= C mal mal` se evalúa a `True`

```haskell
data T = A | B | C T T deriving (Eq,Ord)
--       1 | 2 | 3
mal =  head [] -- error (Bottom _|_)
i> C mal A <= C mal B
i> 3 _|_ 1 <= 3 _|_ 2                 -- Error _|_ = _|_
ii> C A mal == C B mal
ii> 3 1 _|_ == 3 2 _|_                -- False 2 != 1
iii> A <= C mal mal && B <= C mal mal
iii> 1 <= 3 _|_ _|_ && 2 <= 3 _|_ _|_ -- Bien
```

\begin{itemize}
\item[$\boxtimes$] Exactamente una es cierta
\item[$\square$] Exactamente dos son ciertas
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

[//]: <> (100 - 106 Hay que hacer)

# Test *Fold*.
### NOTAS.
1. `foldr o A [a1,.., an] = o a1 (... (o an1 (o an A)...))`
1. `foldl o A [a1,.., an] = o (o... (o (o A a1) a2) ... ) an`

***

1. Considérese la función `f` definida como `f xs = foldr g [] xs where g x y = y++[x]`. Entonces:

```haskell
> g x y = y++[x] = \x y -> y ++ [x]
> f xs = foldr g [] xs
-- Es más facil con un ejemplo
> xs = [2, 3]

> ((\x y -> y ++ [x]) 2 ((\x y -> y ++ [x]) (3 [])))
> ((\x y -> y ++ [x]) 2 ([] ++ [3]))
> (([] ++ [3]) ++ [2]) = [3, 2]
```

\begin{itemize}
\item[$\boxtimes$] \verb|f xs| computa la inversa de \verb|xs|
\item[$\square$] \verb|f xs| computa la propia lista \verb|xs|
\item[$\square$] \verb|f| está mal tipada
\end{itemize}

***

2. Considérense las funciones

$\left\{
\begin{minipage}{8cm}
\begin{verbatim}
  f xs = foldr g [] xs where g x y = x:filter (/= x) y
\end{verbatim}
\begin{verbatim}
  f’ xs = foldl g [] xs where g y x = x:filter (/= x) y
\end{verbatim}
\end{minipage}
\right.$

```haskell
> g1 x y = x:filter (/= x) y = (\x y -> x:filter (/= x) y)
> g2 x y = x:filter (/= x) y = (\y x -> x:filter (/= x) y)
> xs = [a1,..., an]

> f xs = (...(\x y -> x:filter (/= x) y) an [])
> f xs = (...(an:filter (/= an) []))
> f xs = (...g1 a(n-1) (an:filter (/= an) []))

> f' xs = ((\y x -> x:filter (/= x) y) [] a1) ...) ...
> f' xs = ((a1:filter (/= a1) []) ...) ...
-- Vemos que ambos tienen los mismos elementos pero desordenados.
```

\begin{itemize}
\item[$\boxtimes$] \verb|f xs| y \verb|f’| xs coinciden, para cualquier lista finita \verb|xs|.
\item[$\square$] Los elementos de \verb|f xs| y \verb|f’ xs| coinciden, quizás en otro orden, para cualquier lista finita \verb|xs|.
\item[$\square$]  Una de las dos está mal tipada.
\end{itemize}

3. Considérese la función `f` definida como `f xs = foldl g [] xs where g y x = y++[x]`. Entonces:

```haskell
> xs = [a1, a2, ..., an]
> g y x = y++[x] = (\x y -> x++[y])
> f xs = foldl g [] xs
> f xs = ((\x y -> x++[y]) [] a1) ... = (..(g ([]++[a1]) a2)...)
> f xs = ((\x y -> x++[y]) ([]++[a1]) a2) ... = (g (([]++[a1])++[a2])) ...
-- Vemos que va [] ++ [a1] ++ ... ++ [an] = xs
```

\begin{itemize}
\item[$\square$] \verb|f xs| computa la inversa de \verb|xs|
\item[$\boxtimes$] \verb|f xs| computa la propia lista \verb|xs|
\item[$\square$] \verb|f| está mal tipada
\end{itemize}

***

4. La evaluación de `foldl (\e x -> x:x:e) [] [1,2,3]` produce como resultado

```haskell
> (((\e x -> x:x:e) [] 1) ... = ((\e x -> x:x:e) (1:1:[]) 2)...
> ((\e x -> x:x:e) (2:2:(1:1:[])) 3)... = 3:3:(2:2:(1:1:[]))
```

\begin{itemize}
\item[$\square$] \verb|[1,1,2,2,3,3]|
\item[$\boxtimes$] \verb|[3,3,2,2,1,1]|
\item[$\square$] \verb|[3,2,1,3,2,1]|
\end{itemize}

***

5. La evaluación de `foldr (\x e -> x:[1..length e]) [0] [1,2,3]` produce como resultado

```haskell
> (..(\x e -> x:[1..length e]) 3 [0]) =
> (..(\x e -> x:[1..length e]) 2 (3:[1..1])) =
> (..(\x e -> x:[1..length e]) 2 [3, 1])
> (..(\x e -> x:[1..length e]) 1 (2:[1..2]))
> (..(\x e -> x:[1..length e]) 1 [2, 1, 2])
> 1:[1..3] = [1, 1, 2, 3]
```


\begin{itemize}
\item[$\square$] \verb|[1,2,3,0]|
\item[$\square$] \verb|[3,1,2,3]|
\item[$\boxtimes$] \verb|[1,1,2,3]|
\end{itemize}

\newpage

6. La evaluación de `foldr (\x y -> x y) 1 [\x -> x*x,\x -> x-1,(+ 3)]` produce como resultado

```haskell
> (...(\x y -> x y) (+ 3)  1) =
> (...(\x y -> x y) (\x -> x-1) ((+3) 1))) =
> (...(\x y -> x y) (\x -> x*x) ((\x -> x-1) 4))
> (\x -> x*x) ((\x -> x-1) 4) = (\x -> x*x) 3) = 3 * 3 = 9
```

\begin{itemize}
\item[$\boxtimes$] \verb|9|
\item[$\square$] \verb|[1,0,4]|
\item[$\square$] Una lista de funciones
\end{itemize}

[//]: <> (113 - 129 Hay que hacer)

\newpage

# Test *Reducción de expresiones*.

### NOTAS.

***

1. La reducción de la expresión `(\x y -> (\z -> y (z+2)) (y x)) 3 (\x -> x+1)` producirá el resultado:

```
> y1 = (\x -> x+1)
> x1 = 3
> (\x1 y1 -> (\z -> y1 (z+2)) (y1 x1)) =
> (\z -> (\x -> x+1) (z+2)) ((\x -> x+1) 3)
> (\z -> (z+2) + 1) 4 = (4+2) + 1 = 7
```

\begin{itemize}
\item[$\square$] 8
\item[$\boxtimes$] 7
\item[$\square$] 6
\end{itemize}

***

2. La reducción de la expresión `(\x y -> x (x y)) (\x -> x + 3) 4` producirá el resultado:

```
> x1 = (\x -> x + 3)
> y1 = 4
> (\x1 y1 -> x1 (x1 y1)) =  
> (\x -> x + 3) ((\x -> x + 3) 4) = (\x -> x + 3) (4 + 3)
> (\x -> x + 3) (4 + 3) = (4 + 3) + 3 = 10
```

\begin{itemize}
\item[$\square$] 7
\item[$\boxtimes$] 10
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}

***

3. La reducción de la expresión `(\x y -> x (x y)) (\x -> x + y) 4` producirá el resultado:

```
> x1 = (\x -> x + 3)
> y1 = 4
> (\x1 y1 -> x1 (x1 y1)) =  
> (\x -> x + 3) ((\x -> x + 3) 4) = (\x -> x + 3) (4 + 3)
> (\x -> x + 3) (4 + 3) = (4 + 3) + 3 = 10
```

\begin{itemize}
\item[$\boxtimes$] 8
\item[$\square$] 12
\item[$\square$] Las dos anteriores son falsas.
\end{itemize}
