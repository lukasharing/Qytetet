package implementacionCUP.alex;

public enum ClaseLexica {
 ID,
 
 // Tipos de Números
 INT,  // Número Entero
 REAL, // Número Real
 EXE, // Número (Entero / Real) Exponencial
  
 
 // Operadores matemáticos
 EQ, // Operador asignación
 PLS, // Operador más
 MNS, // Operador menos
 MUL, // Operador multiplicar
 DIV, // Operador dividir
 
 PCE, // Paréntesis cerrado
 PAO, //Paréntesis abierto 

 // Operador binario de comparación
 GT, // Comparador Mayor
 GE, // Comparador Mayor igual
 LT, // Comparador Menor
 LE, // Comparador Menor igual
 BE, // Comparador Igualdad
 NE, // Comparador Desigualdad
 
 // String reservados
 BOOL, // Booleanos
 NUM, // Números
 
 // Binario
 AND, // Operador Binario AND
 OR, // Operador Binario OR
 NOT, // Operador Unario TRUE, FALSE,
 
 // Separadores
 SEP, 
 PYC, 
 
 // Fin de fichero
 EOF
}
