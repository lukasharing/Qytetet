% Entrega 2 TSI - Lukas Häring
% Ejercicio 3

include "globals.mzn";

% 1. Definición de variables y constantes.
set of int: PROFESORES = 1..6;
set of int: HORARIOS = 9..14;
array[PROFESORES] of var HORARIOS: seleccion; 

% 2. Definición de restricciones
constraint seleccion[1] >= 11 /\ seleccion[1] <= 14;
constraint seleccion[2] >= 11 /\ seleccion[2] <= 12;
constraint seleccion[3] >= 10 /\ seleccion[3] <= 13;
constraint seleccion[4] >= 10 /\ seleccion[4] <= 12;
constraint seleccion[5] >= 11 /\ seleccion[5] <= 12;
constraint seleccion[6] >= 9 /\ seleccion[6] <= 14;
constraint alldifferent(seleccion);

% 3. Resolución
solve satisfy;

% 4. Definición del output (Opcional)
output ["\(seleccion)\n"];

