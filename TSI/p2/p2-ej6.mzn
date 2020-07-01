% Entrega 2 TSI  - Lukas Häring
% Ejercicio 6

include "globals.mzn";

set of int: PERSONS = 0..4;
set of int: CHARACTERISTICS = 0..4;

% Columns
% 0 -> Vasco
% 1 -> Catalan
% 2 -> Gallego
% 3 -> Navarro
% 4 -> Andaluz

% Rows
% 0 -> Color
  % 0 -> Roja
  % 1 -> Verde
  % 2 -> Blanco
  % 3 -> Amarilla
  % 4 -> Azul
% 1 -> Posicion
  % 0 -> Más a la izquierda
  % .. 4 -> Más a la derecha
% 2 -> Animal
  % 0 -> Perro
  % 1 -> Caracoles
  % 2 -> Zorro
  % 3 -> Caballo
  % 4 -> Cebra
% 3 -> Bebida
  % 0 -> Té
  % 1 -> Café
  % 2 -> Leche
  % 3 -> Zumo
% 4 -> Profesiones
  % 0 -> Pintor
  % 1 -> Escultor
  % 2 -> Diplomático
  % 3 -> Violinista
  % 4 -> Médico
array [CHARACTERISTICS, PERSONS] of var 0..4: solution;

% 2. Definición de restricciones
% Every Element should be different
constraint forall(i in CHARACTERISTICS)(all_different([solution[i,j] | j in PERSONS]));

% Restrictions Exercise (6.)
% Vasco Vive en casa roja
constraint solution[0,0] == 0;

% Catalán tiene perro
constraint solution[2,1] == 0;

% Gallego es un pintor
constraint solution[4,2] == 0;

% Navarro bebe té
constraint solution[3,3] == 0;

% Andaluz Vive en la primera casa de la izquierda
constraint solution[1,4] == 0;

%  Casa verde bebe café
constraint exists(i in PERSONS)(solution[0,i] == 1 /\ solution[3,i] == 1);

%  Casa verde está al lado de la blanca y a su derecha
constraint exists(i,j in PERSONS)(solution[0,i] == 1 /\ solution[0,j] == 2 /\ ((solution[1,j] + 1) == solution[1,i]));

%  El escultor cría caracoles
constraint exists(i in PERSONS)(solution[4,i] == 1 /\ solution[2,i] == 1);

%  El diplomático vive en la casa amarilla
constraint exists(i in PERSONS)(solution[4,i] == 2 /\ solution[0,i] == 3);

%  En la casa central se bebe leche.
constraint exists(i in PERSONS)(solution[1,i] == 2 /\ solution[3,i] == 2);

%  La casa del andaluz está al lado de la azul
constraint exists(i in PERSONS)(solution[0,i] == 4 /\ (((solution[1,4] + 1) == solution[1,i]) \/ ((solution[1,4] - 1) == solution[1,i])));

%  El violinista bebe zumo
constraint exists(i in PERSONS)(solution[4,i] == 3 /\ solution[3,i] == 3);

%  El zorro está en una casa al lado de la del medico
constraint exists(i,j in PERSONS)((solution[4,i] == 4 /\ solution[2,j] == 2) /\ (((solution[1,j] + 1) == solution[1,i]) \/ ((solution[1,j] - 1) == solution[1,i])));

%  El caballo está en una casa al lado de la del diplomático
constraint exists(i,j in PERSONS)((solution[4,i] == 2 /\ solution[2,j] == 3) /\ (((solution[1,j] + 1) == solution[1,i]) \/ ((solution[1,j] - 1) == solution[1,i])));

% 3. Resolución
solve satisfy;

% 4. Definición del output (Opcional)
output ["Cebra \([i| i in PERSONS where solution[2,i] == 4]), Agua \([i| i in PERSONS where solution[3,i] == 4])"];

