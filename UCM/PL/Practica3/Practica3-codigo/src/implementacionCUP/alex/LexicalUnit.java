package implementacionCUP.alex;
import implementacionCUP.asint.*;
import java_cup.runtime.Symbol;

public abstract class LexicalUnit extends Symbol {
   private int fila;
   public LexicalUnit(int fila, int clase, String lexema) {
     super(clase,lexema);
     this.fila = fila;
   }
   public int clase () {return sym;}
   public String lexema() {return (String)value;}
   public int fila() {return fila;}
}
