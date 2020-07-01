% Entrega 2 TSI
% Ejercicio 7

include "globals.mzn";

% 1. Definición de variables y constantes.
set of int: TASKS = 1..9;
% array[TASKS] of var TASKS : assigned;

array[TASKS] of int: DURATION = [7, 3, 1, 8, 2, 1, 1, 3, 2];

array[TASKS, 1..2] of var int: solution;

% 2. Definición de restricciones
% Set time as positive (0 -> inf)
constraint forall(i in array1d(solution))(i >= 0);
% Set [0] as start, [1] as end
constraint forall(i in TASKS)((solution[i,1] + DURATION[i]) == solution[i,2]);
% Set Task Interval Restriction (Begin Less Priority >= Higher Priority)
constraint solution[2,1] >= solution[1,2];
constraint solution[3,1] >= solution[2,2];
constraint solution[4,1] >= solution[1,2];
constraint solution[5,1] >= max([solution[i,2] | i in 3..4]);
constraint solution[6,1] >= max([solution[i,2] | i in 3..4]);
constraint solution[7,1] >= max([solution[i,2] | i in 3..4]);
constraint solution[8,1] >= solution[1,2];
constraint solution[9,1] >= max([solution[i,2] | i in [6, 8]]);

solve minimize max(array1d(solution));

% 4. Definición del output (Opcional)
output ["\(i), [\(solution[i,1]), \(solution[i,2]))\n" | i in TASKS];

