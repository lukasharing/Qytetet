package implementacionCUP.alex;



public class UnievaluatedLexicalUnit extends LexicalUnit {
   public String lexema() {throw new UnsupportedOperationException();}   
   public UnievaluatedLexicalUnit(int fila, ClaseLexica clase) {
     super(fila,clase);  
   }
  public String toString() {
    return "[clase:"+clase()+",fila:"+fila()+"]";  
  }   
}
