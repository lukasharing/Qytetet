% Entrega 2 TSI  - Lukas Häring
% Ejercicio 1

include "globals.mzn";

% 1. Definición de variables y constantes.
% var
array [0..9] of var 0..9: LETTERS;

var int: TESTE = LETTERS[1] + LETTERS[0] * 10 + LETTERS[2] * 100 + LETTERS[1] * 1000 + LETTERS[0] * 10000;
var int: FESTE = LETTERS[1] + LETTERS[0] * 10 + LETTERS[2] * 100 + LETTERS[1] * 1000 + LETTERS[3] * 10000;
var int: DEINE = LETTERS[1] + LETTERS[8] * 10 + LETTERS[7] * 100 + LETTERS[1] * 1000 + LETTERS[4] * 10000;
var int: KRAFTE= LETTERS[1] + LETTERS[0] * 10 + LETTERS[3] * 100 + LETTERS[6] * 1000 + LETTERS[9] * 10000 + LETTERS[5] * 100000;

% 2. Definición de restricciones
constraint alldifferent(LETTERS);
% 142630
constraint (TESTE + FESTE + DEINE == KRAFTE);

% 3. Resolución
solve satisfy;

% 4. Definición del output (Opcional)
output ["T = \(LETTERS[0]),\nE = \(LETTERS[1]),\nS = \(LETTERS[2]),\nF = \(LETTERS[3]),\nD = \(LETTERS[4]),\nK = \(LETTERS[5]),\nA = \(LETTERS[6]),\nI = \(LETTERS[7]),\nN = \(LETTERS[8]),\nR = \(LETTERS[9])\n"];

