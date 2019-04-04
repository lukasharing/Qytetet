package implementacionCUP.alex;
import implementacionCUP.asint.*;

public class MultievaluatedLexicalUnit extends LexicalUnit {
  private String lexema;
  public MultievaluatedLexicalUnit(int fila, ClaseLexica clase, String lexema) {
     super(fila,clase);  
     this.lexema = lexema;
   }
  public String lexema() {return lexema;}
  public String toString() {
    return "[clase:"+clase()+",fila:"+fila()+",lexema:"+lexema()+"]";  
  }
}
