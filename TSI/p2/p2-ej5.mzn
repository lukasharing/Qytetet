% Entrega 2 TSI - Lukas Häring
% Ejercicio 5

include "globals.mzn";

% 1. Definición de variables y constantes.
set of int: FRANJAS = 1..6;
set of int: ASIGNATURAS = 1..10; % 1 -> Not subject, Free Time
set of int: DIAS = 1..5;
int: RECREO = 4; % Free Time
array [ASIGNATURAS] of int: HORAS_SEMANALES = [5, 4, 2, 4, 4, 4, 2, 2, 2, 1];

array [DIAS, FRANJAS] of var ASIGNATURAS: resultado;


array [1..5] of int: C1 = [2, 4, 5, 6, 9];
array [1..4] of int: C2 = [3, 7, 8, 10];

% 2. Definición de restricciones

% Hrs / Week for each subject Constraint
constraint forall(k in ASIGNATURAS)(count(array1d(resultado), k, HORAS_SEMANALES[k]));

% In one day we can only have one block
constraint forall(d in DIAS)(
            forall(c in C1)(
              let { var int: s = count([resultado[i,j] | i in DIAS, j in FRANJAS where i == d], c) } in 
              (s == 2 \/ s == 0)
            )
           );

constraint forall(d in DIAS)(
            forall(c in C2)(
              let { var int: s = count([resultado[i,j] | i in DIAS, j in FRANJAS where i == d], c) } in 
              (s == 1 \/ s == 0)
            )
           );

% Force 2h blocks in some cases
constraint forall(i in DIAS)(exists(j in FRANJAS)(j < 5 /\ exists(k in C1)(HORAS_SEMANALES[k] > 1 /\ resultado[i,j]==k /\ resultado[i,j+1] == k)));

% Proffesors Constraint
array[1..2] of int: P1 = [2, 4];
constraint forall(d in DIAS)(
      let { var int: c1 = count([resultado[i,j] | i in DIAS, j in FRANJAS where i == d], P1[1]), 
            var int: c2 = count([resultado[i,j] | i in DIAS, j in FRANJAS where i == d], P1[2])
      } in ((c1 == 0 /\ c2 > 0) \/ (c1 > 0 /\ c2 == 0) \/ (c1 == 0 /\ c2 == 0))
);

array[1..2] of int: P2 = [5, 6];
constraint forall(d in DIAS)(
      let { var int: c1 = count([resultado[i,j] | i in DIAS, j in FRANJAS where i == d], P2[1]), 
            var int: c2 = count([resultado[i,j] | i in DIAS, j in FRANJAS where i == d], P2[2])
      } in ((c1 == 0 /\ c2 > 0) \/ (c1 > 0 /\ c2 == 0) \/ (c1 == 0 /\ c2 == 0))
);

array[1..2] of int: P3 = [7, 10];
constraint forall(d in DIAS)(
      let { var int: c1 = count([resultado[i,j] | i in DIAS, j in FRANJAS where i == d], P3[1]), 
            var int: c2 = count([resultado[i,j] | i in DIAS, j in FRANJAS where i == d], P3[2])
      } in ((c1 == 0 /\ c2 > 0) \/ (c1 > 0 /\ c2 == 0) \/ (c1 == 0 /\ c2 == 0))
);

% Free Time
constraint forall(d in DIAS)(resultado[d, RECREO] == 1);

% 3. Resolución
solve satisfy;

% 4. Definición del output (Opcional)
output ["L= \([resultado[1, i]-1 | i in FRANJAS])\nM= \([resultado[2, i]-1 | i in FRANJAS])\nX= \([resultado[3, i]-1 | i in FRANJAS])\nJ= \([resultado[4, i]-1 | i in FRANJAS])\nV= \([resultado[5, i]-1 | i in FRANJAS])\n"];

