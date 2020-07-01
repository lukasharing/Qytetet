% Entrega 2 TSI - Lukas Häring
% Ejercicio 10

include "globals.mzn";

% 1. Definición de variables y constantes.
int: MAX = 275;
set of int: ELEMENTS = 1..12;
array [ELEMENTS] of var 0..1: resultado;
array [ELEMENTS] of int: weights = [9, 13, 153, 50, 15, 68, 27, 39, 23, 52, 11, 32];
array [ELEMENTS] of int: preferences = [150, 35, 200, 160, 60, 45, 60, 40, 30, 10, 70, 30];

% 2. Definición de restricciones
% Solution has to weight less than 275kg
constraint sum(i in ELEMENTS)(weights[i] * resultado[i]) < MAX;

% 3. Resolución
solve maximize sum(i in ELEMENTS)(preferences[i] * resultado[i]);

% 4. Definición del output (Opcional)
% [1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0]
output ["Elements: \(resultado) with weight \( sum(i in ELEMENTS)(weights[i] * resultado[i]) ) and preference \(sum(i in ELEMENTS)(preferences[i] * resultado[i]))"];

