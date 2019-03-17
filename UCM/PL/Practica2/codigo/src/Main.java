import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main {

	public static void main(String arg[]) throws IOException {
		Reader input = new InputStreamReader(new FileInputStream("./input/code_2_error.txt"));
		TinyErrorController err = new TinyErrorController();
		TinyLexicalAnalyzer al = new TinyLexicalAnalyzer(input);
		al.fijaGestionErrores(err);
		
		try{
			LexicalUnit lu = al.yylex();
			while(!lu.clase().equals(ClaseLexica.EOF)) {
				System.out.println(lu.toString());
				lu = al.yylex();
			}
		}catch(IOException e) {
			err.errorFatal(e);
		}
	};

}
