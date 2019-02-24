import java.io.FileInputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;

public class TinyLexicalAnalyzer {
	private Reader input;
	private StringBuffer lex;
	private int nextChar;
	private int filaInicio;
	private int columnaInicio;
	private int filaActual;
	private int columnaActual;
	private static String NL = System.getProperty("line.separator");
	   
	private enum state {
		INICIO, AND, N, 
		
		MUL, DIV, PAO, PCE, SEP, 
		
		GT, GE, LT, LE, BE, NE,
		
		EQ, 
		PLS,
		MNS,
		
		INT, PNT, EXP, DEC, SGN, EXE,
		PYC,
		
		ID, 
		
		EOF,
	
	
	};
	   
	private state state;
	
	public TinyLexicalAnalyzer(Reader input) throws IOException {
		this.input = input;
		this.lex = new StringBuffer();
		this.nextChar = input.read();
		this.filaActual = 1;
		this.columnaActual = 1;
	}

	public LexicalUnit nextToken() throws IOException {
		state = state.INICIO;
		filaInicio = filaActual;
		columnaInicio = columnaActual;
		lex.delete(0,lex.length());
		
		while(true) {
			switch(state) {
				case INICIO: 
					if (isMUL()) transita(state.MUL);
					else if (isDIV()) transita(state.DIV);
					else if (isPAO()) transita(state.PAO);
					else if (isPCE()) transita(state.PCE);
					else if (isAND()) transita(state.AND);
					else if (isGT()) transita(state.GT);
					else if (isLT()) transita(state.LT);
					else if (isEQ()) transita(state.EQ);
					else if (isN()) transita(state.N);
					else if (isCHAR()) transita(state.ID);
					else if (isPLS()) transita(state.PLS);
					else if (isINT()) transita(state.INT);
					else if (isMNS()) transita(state.MNS);
					else if (isPYC()) transita(state.PYC);
					else if (isSEPARATOR()) transitaIgnorando(state.INICIO);
					else if (isEOF()) transita(state.EOF);
					else error();
				break;
				
				// Arithmetic Operations
				case MUL: return unitMultiplication();
				case DIV: return unitDivision();
				case PAO: return unitOpenParenthesis();
				case PCE: return unitClosedParenthesis();
				
				// Separator
				case AND:
					if(isAND()) transita(state.SEP); 
					else error();
				break;
				
				case SEP:
					return unitBlockSeparator();
				
				// Comparison
				case GT:
					if(isGT()) transita(state.GE);
					else return unitGreater();
				break;
				case GE:
					return unitGreaterEqual();
				case LT:
					if(isEQ()) transita(state.LE);
					else return unitLess();
				break;
				case LE:
					return unitLessEqual();
				case EQ:
					if(isEQ()) transita(state.BE);
					else return unitAssignment();
				break;
				case BE:
					return unitEqual();
				case N:
					if(isEQ()) transita(state.NE);
					else error();
				break;
				case NE:
					return unitInequal();
				case ID: 
					if(isCHAR() || isINT() || isUNDERSCORE()) transita(state.ID);
					else return unitID();
				break;
				
				// Number
				case INT:
					if(isINT()) transita(state.INT);
					else if(isDOT()) transita(state.PNT);
					else if(isEXP()) transita(state.EXP);
					else return unitInteger();
				break;
				
				case PLS:
					if(isINT()) transita(state.INT); 
					else return unitPlus();
				break;
				
				case MNS:
					if(isINT()) transita(state.INT); 
					else return unitMinus();
				break;
				
				case PNT:
					if(isINT()) transita(state.DEC);
					else error();
				break;
				
				case DEC:
					if(isINT()) transita(state.DEC);
					else if(isEXP()) transita(state.EXP);
					else return unitReal();
				break;
				
				case EXP:
					if(isSIGN()) transita(state.SGN);
					else if(isINT()) transita(state.EXE);
					else error(); 
				break;
				
				case SGN:
					if(isINT()) transita(state.EXE); 
					else error();
				break;
				
				case EXE:
					if(isINT()) transita(state.EXE);
					else return unitExponential();
				break;
					
				case PYC:
					return unitVariableSeparator();
				case EOF:
					return unitEOF();
			}
		}   
	};
	
	private void transita(state next) throws IOException {
		lex.append((char)nextChar);
		nextChar();         
		state = next;
	};

	private void transitaIgnorando(state next) throws IOException {
		nextChar();         
		filaInicio = filaActual;
		columnaInicio = columnaActual;
		state = next;
	};
	
	private void nextChar() throws IOException {
		nextChar = input.read();
		if (nextChar == NL.charAt(0)) saltaFinDeLinea();
		if (nextChar == '\n') {
			filaActual++;
			columnaActual = 0;
		} else {
			columnaActual++;  
		}
	};

	private void saltaFinDeLinea() throws IOException {
		for (int i = 1; i < NL.length(); ++i) {
			nextChar = input.read();
			if (nextChar != NL.charAt(i)) error();
		}
		nextChar = '\n';
	};
   
	// Node Transition
	

	// Is Char (uppercase or lowercase)
	private boolean isCHAR() {
		return (Character.toLowerCase(nextChar) >= 'a' && Character.toLowerCase(nextChar) <= 'z');
	};
	
	// Is Integer
	private boolean isINT() {
		return Character.isDigit(nextChar);
	};
	
	// Is Exponential
	private boolean isEXP() {
		return Character.toLowerCase(nextChar) == 'e';
	};
	
	// Multiplication Transition
	private boolean isMUL() {
		return nextChar == '*';
	};
	
	// Division Transition
	private boolean isDIV() {
		return nextChar == '/';
	};
	
	// Open Parenthesis
	private boolean isPAO() {
		return nextChar == '(';
	};
	
	// Closed Parenthesis
	private boolean isPCE() {
		return nextChar == ')';
	};
	
	// Is & Character
	private boolean isAND() {
		return nextChar == '&';
	};
	
	// Is greater than
	private boolean isGT() {
		return nextChar == '>';
	};
	
	// Is less than
	private boolean isLT() {
		return nextChar == '<';
	};
	
	// Is Equal
	private boolean isEQ() {
		return nextChar == '=';
	};
	
	// Is Negation Simbol (!)
	private boolean isN() {
		return nextChar == '!';
	};
	
	// Is plus sign
	private boolean isPLS() {
		return nextChar == '+';
	};
	
	// Is minus sign
	private boolean isMNS() {
		return nextChar == '-';
	};
	
	// Is minus or plus 
	private boolean isSIGN(){
		return (isMNS() || isPLS());
	};
	
	// Is minus sign
	private boolean isPYC() {
		return nextChar == ';';
	};
	
	// Is dot char
	private boolean isDOT() {
		return nextChar == '.';
	};
	
	// Is Underscore char
	private boolean isUNDERSCORE() {
		return nextChar == '_';
	};
	
	// Is End Of File
	private boolean isEOF() {
		return nextChar == -1;
	};
	
	// Is empty/end of line char
	private boolean isSEPARATOR() {
		return (nextChar == ' ' || nextChar == '\n');
	};

	// Jump 
	private boolean hayNEWLINE() {
		return (nextChar == '\r' || nextChar == '\b' || nextChar == '\n');
	};


	private LexicalUnit unitID() {
		switch(lex.toString()) {
			// Name variable
			case "num":  
				return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.NUM);
			case "bool":    
				return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.BOOL);
			
			// Boolean operations
			case "and":
				return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.AND);
			case "or":
				return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.OR);
			case "not":
				return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.NOT);
			
			// Variable
			default:
				return new MultievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.ID, lex.toString());     
		}
	};

	
	// Number Operations
	private LexicalUnit unitInteger() {
		return new MultievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.INT, lex.toString());     
	};
	
	private LexicalUnit unitReal() {
		return new MultievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.REAL, lex.toString());     
	};
	
	private LexicalUnit unitExponential() {
		return new MultievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.REAL, lex.toString());     
	};
	
	// Arithmetic Operations
	private LexicalUnit unitPlus() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.PLS);     
	};

	private LexicalUnit unitMinus() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.MNS);     
	};

	private LexicalUnit unitMultiplication() {
		return new UnievaluatedLexicalUnit(filaInicio,columnaInicio, ClaseLexica.MUL);     
	};

	private LexicalUnit unitDivision() {
		return new UnievaluatedLexicalUnit(filaInicio,columnaInicio, ClaseLexica.DIV);     
	};

	private LexicalUnit unitOpenParenthesis() {
		return new UnievaluatedLexicalUnit(filaInicio,columnaInicio, ClaseLexica.PAO);     
	};
	
	private LexicalUnit unitClosedParenthesis() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.PCE);     
	};
	
	private LexicalUnit unitAssignment() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.EQ);     
	};
	
	// Mathematica Comparaison
	private LexicalUnit unitGreater() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.GT);     
	};
	
	private LexicalUnit unitLess() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.LT);     
	};
	
	private LexicalUnit unitGreaterEqual() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.GE);     
	};
	
	private LexicalUnit unitLessEqual() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.LE);     
	};
	
	private LexicalUnit unitEqual() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.BE);     
	};
	
	private LexicalUnit unitInequal() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.NE);     
	};
	
	// Separator
	private LexicalUnit unitBlockSeparator() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.SEP);     
	};
	
	private LexicalUnit unitVariableSeparator() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.PYC);     
	};
	
	// Unit End of file 
	private LexicalUnit unitEOF() {
		return new UnievaluatedLexicalUnit(filaInicio, columnaInicio, ClaseLexica.EOF);     
	};

	// Returns Error
	private void error() {
		System.err.println("("+filaActual+','+columnaActual+"):Caracter inexperado");  
		System.exit(1);
	};

	public static void main(String arg[]) throws IOException {
		Reader input = new InputStreamReader(new FileInputStream("./input/code_2_error.txt"));
		TinyLexicalAnalyzer al = new TinyLexicalAnalyzer(input);
		LexicalUnit unit;
		do {
			unit = al.nextToken();
			System.out.println(unit);
		}while (unit.clase() != ClaseLexica.EOF);
	};
};