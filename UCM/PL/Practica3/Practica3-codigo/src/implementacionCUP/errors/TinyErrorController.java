package implementacionCUP.errors;

import implementacionCUP.alex.*;
import implementacionCUP.asint.*;

public class TinyErrorController {
   public void errorLexico(int fila, String lexema) {
     System.out.println("ERROR fila "+fila+": Caracter inexperado: "+lexema);
     System.exit(1);
   }
   public void errorSintactico(LexicalUnit unidadLexica) {
	   System.out.print("ERROR fila "+unidadLexica.fila()+": Elemento inexperado "+unidadLexica.value);
	   System.exit(1);
   }
   public void errorFatal(Exception e) {
     System.out.println(e);
     e.printStackTrace();
     System.exit(1);
   }
}
