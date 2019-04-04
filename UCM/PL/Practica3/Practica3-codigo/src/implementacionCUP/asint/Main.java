package implementacionCUP.asint;


import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import implementacionCUP.alex.TinyLexicalAnalyzer;

public class Main {
   public static void main(String[] args) throws Exception {
     Reader input = new InputStreamReader(new FileInputStream("input.txt"));
     TinyLexicalAnalyzer alex = new TinyLexicalAnalyzer(input);
     TinySintacticAnalyzer asint = new TinySintacticAnalyzer(alex);
	 //asint.setScanner(alex);
	 asint.parse();
 }
}
   
