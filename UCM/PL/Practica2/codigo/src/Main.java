import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main {
	
	public static void main(String arg[]) throws IOException {
		Reader input = new InputStreamReader(new FileInputStream("./input/code_3_error.txt"));
		
		// COMENTARIO:
		// Se nos ha olvidado la inicialización, asignación y el programa, las expresiones funcionan.
		//mainSintactic();
		if(false) {
			TinyErrorController err = new TinyErrorController();
			TinyLexicalAnalyzer al = new TinyLexicalAnalyzer(input);
			al.fijaGestionErrores(err);
			
			try{
				LexicalUnit lu = al.yylex();
				System.out.println(lu.toString());
				while(!lu.clase().equals(ClaseLexica.EOF)) {
					lu = al.yylex();
					System.out.println(lu.toString());
				}
			}catch(IOException e) {
				err.errorFatal(e);
			}
		}else {
			TinySintacticAnalyzer sa = new TinySintacticAnalyzer(input);
			sa.Sp();
		}
		
		
	};

}
