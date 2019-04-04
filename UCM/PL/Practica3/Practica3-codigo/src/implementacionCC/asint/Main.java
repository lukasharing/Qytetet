package implementacionCC.asint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, ParseException {

		Reader input = new InputStreamReader(new FileInputStream("./input/code_1.txt"));
		SimpleCharStream stream = new SimpleCharStream(input);
		JavaCCLexicalAnalyzerTokenManager token_manager = new JavaCCLexicalAnalyzerTokenManager(stream);
		
		JavaCCLexicalAnalyzer analyzer = new JavaCCLexicalAnalyzer(token_manager);
		analyzer.PROGRAM();
	}
	
}
