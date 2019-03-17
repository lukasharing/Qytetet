
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TinySintacticAnalyzer {
   private LexicalUnit anticipo;
   private TinyLexicalAnalyzer alex;
   private TinyErrorController errores;
   public TinySintacticAnalyzer(Reader input) {
      errores = new TinyErrorController();
      alex = new TinyLexicalAnalyzer(input);
      alex.fijaGestionErrores(errores);
      sigToken();
   }
   
   private void empareja(ClaseLexica claseEsperada) {
      if (anticipo.clase() == claseEsperada)
          sigToken();
      else errores.errorSintactico(anticipo.fila(),anticipo.clase(),claseEsperada);
   }
   private void sigToken() {
      try {
        anticipo = alex.yylex();
      }
      catch(IOException e) {
        errores.errorFatal(e);
      }
   }
   
}