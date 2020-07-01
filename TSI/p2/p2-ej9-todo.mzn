% Entrega 2 TSI
% Ejercicio 2

include "globals.mzn";

% 1. Definición de variables y constantes.
% var
array [0..9] of var 0..9: resultado;

% 2. Definición de restricciones
% constraint
constraint forall(i in 0..9)(resultado[i] == count(resultado, i));

% 3. Definición de restricciones
solve satisfy;

% 4. Definición del output (Opcional)
output ["x= \(resultado)"];

