public class MultievaluatedLexicalUnit extends LexicalUnit {
  private String lexema;
  public MultievaluatedLexicalUnit(int fila, int columna, ClaseLexica clase, String lexema) {
     super(fila,columna,clase);  
     this.lexema = lexema;
   }
  public String lexema() {return lexema;}
  public String toString() {
    return "[clase:"+clase()+",fila:"+fila()+",col:"+columna()+",lexema:"+lexema()+"]";  
  }
}
