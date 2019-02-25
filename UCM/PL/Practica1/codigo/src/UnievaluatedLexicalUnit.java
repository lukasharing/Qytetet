public class UnievaluatedLexicalUnit extends LexicalUnit {
   public String lexema() {throw new UnsupportedOperationException();}   
   public UnievaluatedLexicalUnit(int fila, int columna, ClaseLexica clase) {
     super(fila,columna,clase);  
   }
  public String toString() {
    return "[clase:"+clase()+",fila:"+fila()+",col:"+columna()+"]";  
  }   
}
