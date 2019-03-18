
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TinySintacticAnalyzer {
   private LexicalUnit anticipo;
   private TinyLexicalAnalyzer alex;
   private TinyErrorController errores;
   public TinySintacticAnalyzer(Reader input) {
      errores = new TinyErrorController();
      alex = new TinyLexicalAnalyzer(input);
      alex.fijaGestionErrores(errores);
      sigToken();
   }
   
   private void PROGRAM() {
      switch(anticipo.clase()) {
      case NUM: case BOOL:
    	  B();
    	  PO();
    	  A();
     break;
      default:
    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.NUM, ClaseLexica.BOOL);
      }
   };
   
   private void PO() {
      switch(anticipo.clase()) {
      case SEP: empareja(ClaseLexica.SEP); break;
      default:
    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.SEP);
      }
   };
   
   private void A() {
      switch(anticipo.clase()) {
      case ID:
    	  LIT();
    	  OA();
    	  E0();
    	  AP();
     break;
      default:
    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.ID);
      }
   };
   
   private void AP() {
      switch(anticipo.clase()) {
      case PYC:
    	  PYC();
    	  A();
     break;
      default:
    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.ID);
      }
   };
   
   private void OA() {
      switch(anticipo.clase()) {
      case EQ: empareja(ClaseLexica.EQ);
     break;
      default:
    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.EQ);
      }
   };
   
   
   
   private void B() {
      switch(anticipo.clase()) {
      case BOOL: case NUM:
    	OB();
    	LIT();
    	BP();
      break;
      default:
    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.BOOL, ClaseLexica.NUM);
      }
   };

   private void BP() {
      switch(anticipo.clase()) {
      case PYC:
    	PYC();
    	B();
      break;
      case SEP: break;
      case EOF: break;
      default:
    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.PYC);
      }
   };
   
   private void OB() {
      switch(anticipo.clase()) {
      case BOOL: empareja(ClaseLexica.BOOL); break;
      case NUM: empareja(ClaseLexica.NUM); break;
      default:
    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.BOOL, ClaseLexica.NUM);
      }
   };
   
   private void PYC() {
      switch(anticipo.clase()) {
      case PYC: empareja(ClaseLexica.PYC); break;
      default:
    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.PYC);
      }
   };
   
   private void LIT() {
      switch(anticipo.clase()) {
      case ID: empareja(ClaseLexica.ID); break;
      default:
    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.ID);
      }
   };
   
   
   
   private void E0() {
      switch(anticipo.clase()) {
        case SEP: case EOF: break;
	    case PAO: case MNS: case INT: case REAL: case EXE: case NOT:
	      E1();
	      E0P();
	    default:
	      errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.PAO, ClaseLexica.MNS, ClaseLexica.INT, ClaseLexica.REAL, ClaseLexica.EXE, ClaseLexica.ID, ClaseLexica.NOT);
      }
   };
   
   private void E0P() {

	   switch(anticipo.clase()) {
	      case PCE: case PYC: break;
	      case PLS: case MNS:
	    	  O0();
	    	  E1();
	    	  E0P();
    	  break;
	      case EOF: break;
	      default:
	    	  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.PLS, ClaseLexica.MNS);
	   }
	    
   };
   
   private void E1() {
	  switch (anticipo.clase()) {
	    case PAO: case MNS: case INT: case REAL: case EXE: case ID: case NOT:
	    	E2();
	    	E1P();
		break;
	  default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.PAO, ClaseLexica.MNS, ClaseLexica.INT, ClaseLexica.REAL, ClaseLexica.EXE, ClaseLexica.ID, ClaseLexica.NOT);
		break;
	  }
   };
   
   private void E1P() {
	  switch (anticipo.clase()) {
	    case PYC: break;
	    case PCE: break;
	    case PLS: break;
	    case MNS: break;
	    case AND:
	    	O11();
	    	E1();
	    break;
	    case OR:
	    	O12();
	    	E2();
    	break;
	    case EOF: break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.OR, ClaseLexica.AND);
		break;
	  }
   };
   
   private void E2() {
	  switch (anticipo.clase()) {
	    case PAO: case MNS: case INT: case EXE: case REAL: case ID: case NOT:
	    	E3();
	    	E2P();
	    break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.PAO, ClaseLexica.MNS, ClaseLexica.INT, ClaseLexica.REAL, ClaseLexica.EXE, ClaseLexica.ID, ClaseLexica.NOT);
		break;
	  }
   };
   
   private void E2P() {
	   switch (anticipo.clase()) {
	    case PCE: case PLS: case MNS: case AND: case OR: case PYC: break;
	    case BE: case NE: case GT: case GE: case LE: case LT:
	    	O2();
	    	E3();
	    break;
	    case EOF: break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.BE, ClaseLexica.NE, ClaseLexica.GT, ClaseLexica.GE, ClaseLexica.LE, ClaseLexica.LT);
		break;
	  }
   };
   
   private void E3() {
	   switch (anticipo.clase()) {
	    case PAO: case MNS: case INT: case REAL: case EXE: case ID: case NOT:
	    	E4();
	    	E3P();
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.PAO, ClaseLexica.MNS, ClaseLexica.INT, ClaseLexica.REAL, ClaseLexica.EXE, ClaseLexica.ID);
		break;
	  }
   };
   
   private void E3P() {
	   switch (anticipo.clase()) {
	    case PCE: case PLS: case MNS: case AND: case OR: case PYC: break;
	    case BE: case NE: case GT: case GE: case LE: case LT: break; // Operadores relacionales
	    case EOF: break; // $
	    case DIV: case MUL:
	    	O3();
	    	E4();
	    	E3P();
	    break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.DIV, ClaseLexica.MUL);
		break;
	  }
   };
   
   private void E4() {
	   switch (anticipo.clase()) {
		   case PAO: case INT: case REAL: case EXE: case ID: E5(); break;
		   case MNS: case NOT:
			   O42();
			   E5();
		   break;
		   default:
			errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.PAO, ClaseLexica.MNS, ClaseLexica.INT, ClaseLexica.REAL, ClaseLexica.EXE, ClaseLexica.NOT, ClaseLexica.ID);
		   break;
	  }
   };
   
   private void E5() {
	   switch (anticipo.clase()) {
	    case PAO:
	    	empareja(ClaseLexica.PAO);
            E0(); 
            empareja(ClaseLexica.PCE);
        break;
        case INT: empareja(ClaseLexica.INT); break;
        case REAL: empareja(ClaseLexica.REAL); break; 
        case EXE: empareja(ClaseLexica.EXE); break;
        case ID: empareja(ClaseLexica.ID); break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.PAO, ClaseLexica.INT, ClaseLexica.REAL, ClaseLexica.EXE, ClaseLexica.ID);
		break;
	  }
   };
   
   private void O0() {
	   switch (anticipo.clase()) {
	    case MNS: empareja(ClaseLexica.MNS); break;
        case PLS: empareja(ClaseLexica.PLS); break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.MNS, ClaseLexica.PLS);
		break;
	  }
   };
   
   private void O11() {
	   switch (anticipo.clase()) {
	    case AND: empareja(ClaseLexica.AND); break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.AND);
		break;
	  }
   };
   
   private void O12() {
	   switch (anticipo.clase()) {
	    case OR: empareja(ClaseLexica.OR); break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.OR);
		break;
	  }
   };
   
   private void O2() {
	   switch (anticipo.clase()) {
	   	case BE: empareja(ClaseLexica.BE); break;
	   	case NE: empareja(ClaseLexica.NE); break;
	   	case GT: empareja(ClaseLexica.GT); break;
	   	case GE: empareja(ClaseLexica.GE); break;
	   	case LE: empareja(ClaseLexica.LE); break;
	   	case LT: empareja(ClaseLexica.LT); break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.BE, ClaseLexica.NE, ClaseLexica.GT, ClaseLexica.GE, ClaseLexica.LE, ClaseLexica.LT);
		break;
	  }
   };
   
   private void O3() {
	   switch (anticipo.clase()) {
	    case MUL: empareja(ClaseLexica.MUL); break;
	    case DIV: empareja(ClaseLexica.DIV); break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.MUL, ClaseLexica.DIV);
		break;
	  }
   };
   
   private void O41() {
	   switch (anticipo.clase()) {
	    case MNS: empareja(ClaseLexica.MNS); break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.MNS);
		break;
	  }
   };
   
   private void O42() {
	   switch (anticipo.clase()) {
	    case NOT: empareja(ClaseLexica.NOT); break;
	    default:
		  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.NOT);
		break;
	  }
   };
   
   
   private void empareja(ClaseLexica claseEsperada) {
      if (anticipo.clase() == claseEsperada)
          sigToken();
      else errores.errorSintactico(anticipo.fila(),anticipo.clase(),claseEsperada);
   }
   
   private void sigToken() {
      try {
        anticipo = alex.yylex();
      }
      catch(IOException e) {
        errores.errorFatal(e);
      }
   }
   
   public void Sp() {
	   PROGRAM();
	   empareja(ClaseLexica.EOF);
   };
   
   
}