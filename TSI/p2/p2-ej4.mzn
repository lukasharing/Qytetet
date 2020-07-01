% Entrega 2 TSI - Lukas Häring
% Ejercicio 4

include "globals.mzn";

% 1. Definición de variables y constantes.
set of int: HORARIOS = 9..13; % 9 - 13

% 4 Professors 4 Classes (we dont need the number of classes information)
% 4 Subjects for Professor 1 with implicit restriction for groups
set of int: GP1 = 1..4;
array [GP1] of var HORARIOS: P1;
% 2 Subjects for Professor 2
set of int: GP2 = 1..2;
array [GP2] of var HORARIOS: P2;
% 4 Subjects for Professor 3
set of int: GP3 = 1..4;
array [GP3] of var HORARIOS: P3;
% 2 Subjects for Professor 4
set of int: GP4 = 1..2;
array [GP4] of var HORARIOS: P4;

% 3. Definición de restricciones
% Groups 1 contraints by hours
constraint (P1[1] != P1[3]) /\ (P1[1] != P2[1]) /\ (P1[3] != P2[1]);
% Groups 2 contraints by hours
constraint (P1[2] != P1[4]) /\ (P1[2] != P2[2]) /\ (P1[4] != P2[2]);
% Groups 3 contraints by hours
constraint (P3[1] != P3[3]) /\ (P3[1] != P4[1]) /\ (P3[3] != P4[1]);
% Groups 4 contraints by hours
constraint (P3[2] != P3[4]) /\ (P3[2] != P4[2]) /\ (P3[4] != P4[2]);

% Hours Restrictions by professors
constraint forall(i in GP2)(P2[i] != 10);
constraint forall(i in GP4)(P4[i] != 9);

% Each hour should be different
constraint alldifferent(P1);
constraint alldifferent(P2);
constraint alldifferent(P3);
constraint alldifferent(P4);

% 3. Resolución
solve satisfy;

% 4. Definición del output (Opcional)
output ["P1: \(P1),\nP2: \(P2),\nP3: \(P3),\nP4: \(P4)"];

