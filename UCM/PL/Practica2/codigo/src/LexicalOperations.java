import java.io.FileInputStream;
import java.io.Reader;

import java.io.InputStreamReader;
import java.io.IOException;

public class LexicalOperations {
	private TinyLexicalAnalyzer lex;
	public LexicalOperations(TinyLexicalAnalyzer lex) {
		this.lex = lex;
	};

	public LexicalUnit unitID() {
		switch(lex.lexema()) {
			// Name variable
			case "num":
				return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.NUM);
			case "bool":
				return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.BOOL);

			// Boolean operations
			case "and":
				return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.AND);
			case "or":
				return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.OR);
			case "not":
				return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.NOT);

			// Variable
			default:
				return new MultievaluatedLexicalUnit(lex.fila(), ClaseLexica.ID, lex.lexema());
		}
	};


	// Number Operations
	public LexicalUnit unitInteger() {
		return new MultievaluatedLexicalUnit(lex.fila(), ClaseLexica.INT, lex.lexema());
	};

	public LexicalUnit unitReal() {
		return new MultievaluatedLexicalUnit(lex.fila(), ClaseLexica.REAL, lex.lexema());
	};

	public LexicalUnit unitExponential() {
		return new MultievaluatedLexicalUnit(lex.fila(), ClaseLexica.REAL, lex.lexema());
	};

	// Arithmetic Operations
	public LexicalUnit unitPlus() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.PLS);
	};

	public LexicalUnit unitMinus() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.MNS);
	};

	public LexicalUnit unitMultiplication() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.MUL);
	};

	public LexicalUnit unitDivision() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.DIV);
	};

	public LexicalUnit unitOpenParenthesis() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.PAO);
	};

	public LexicalUnit unitClosedParenthesis() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.PCE);
	};

	public LexicalUnit unitAssignment() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.EQ);
	};

	// Mathematica Comparaison
	public LexicalUnit unitGreater() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.GT);
	};

	public LexicalUnit unitLess() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.LT);
	};

	public LexicalUnit unitGreaterEqual() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.GE);
	};

	public LexicalUnit unitLessEqual() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.LE);
	};

	public LexicalUnit unitEqual() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.BE);
	};

	public LexicalUnit unitInequal() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.NE);
	};

	// Separator
	public LexicalUnit unitBlockSeparator() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.SEP);
	};

	public LexicalUnit unitVariableSeparator() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.PYC);
	};

	// Unit End of file
	public LexicalUnit unitEOF() {
		return new UnievaluatedLexicalUnit(lex.fila(), ClaseLexica.EOF);
	};
};
