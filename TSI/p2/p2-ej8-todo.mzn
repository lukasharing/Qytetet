% Entrega 2 TSI
% Ejercicio 8

include "globals.mzn";

% 1. Definición de variables y constantes.
set of int: TASKS = 1..10;
array[TASKS] of int: DURATION = [0, 7, 3, 1, 8, 2, 1, 1, 3, 2];
array[TASKS] of int: TRABAJTASK = [0, 2, 3, 2, 2, 1, 2, 1, 1, 2]; 

set of int: TRABAJADORES = 1..3;
set of int: DURACIONMAXIMA = 1..19; % Using Last Problem Value

array [TRABAJADORES, DURACIONMAXIMA] of var TASKS: solution;

% 2. Definición de restricciones

% Initial
constraint solution[1, 1] == 2 /\ solution[2, 1] == 2;

array[TASKS, 1..2] of int: PREDECESSOR = [|0,0   % No Work
  |0,0 % A
  |2,0 % B
  |3,0 % C
  |2,0 % D
  |4,5 % E
  |4,5 % F
  |4,5 % G
  |2,0 % H
  |7,9|]; 



constraint forall(t in TRABAJADORES)(
  forall(t0 in DURACIONMAXIMA)(
    sum([ 1 | t1 in 0..(t0-2)
      where (
        (solution[t, t0-t1-1] == solution[t, t0])
    )]) <= TRABAJTASK[solution[t, t0]]
  )
);


% 3. Solve
solve satisfy;

% 4. Definición del output (Opcional)
output ["T\(i) = [\([solution[i, t] | t in DURACIONMAXIMA]))\n" | i in TRABAJADORES];

