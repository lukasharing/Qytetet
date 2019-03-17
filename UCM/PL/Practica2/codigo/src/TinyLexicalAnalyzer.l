%%
%line
%class TinyLexicalAnalyzer
%type  LexicalUnit
%unicode
%public

%{
  private LexicalOperations ops;
  private TinyErrorController error;
  public String lexema() {return yytext();}
  public int fila() {return yyline+1;}
  public void fijaGestionErrores(TinyErrorController error) {
   this.error = error;
  }
%}

%eofval{
  return ops.unitEOF();
%eofval}

%init{
  ops = new LexicalOperations(this);
%init}

letra  = ([A-Z]|[a-z])
digito = [0-9]
natural = {digito}*
saltables = [ \t\r\b\n]

not = not
and = and
or = or
num = num
bool = bool

identificador = {letra}({letra}|{digito}|\_)*
numeroEntero = [\+,\-]?{natural}
numeroDecimal = {numeroEntero}\.{natural}
numeroExponencial = ({numeroEntero}|{numeroDecimal})[e,E]?{numeroEntero}

suma = \+
resta = \-
multiplicacion = \*
division = /
aparentesis = \(
cparentesis = \)
igual = \=
mayor = \>
menor = \<
mayorIgual = \>\=
menorIgual = \<\=
comparacion = \=\=
desigualdad = \!\=

separador = &&
puntoycoma = \;

%%
{saltables}               {}

{not}                 {return ops.unitID();}
{bool}                {return ops.unitID();}
{and}                 {return ops.unitID();}
{or}                  {return ops.unitID();}
{identificador}       {return ops.unitID();}

{numeroEntero} {return ops.unitInteger();}
{numeroDecimal} {return ops.unitReal();}
{numeroExponencial} {return ops.unitExponential();}


{suma} {return ops.unitPlus();}
{resta} {return ops.unitMinus();}
{multiplicacion}       {return ops.unitMultiplication();}
{division}       {return ops.unitDivision();}
{aparentesis}       {return ops.unitOpenParenthesis();}
{cparentesis}       {return ops.unitClosedParenthesis();}
{igual}       {return ops.unitAssignment();}
{mayor}       {return ops.unitGreater();}
{mayorIgual}       {return ops.unitGreaterEqual();}
{menor}       {return ops.unitLess();}
{menorIgual}       {return ops.unitLessEqual();}
{comparacion}       {return ops.unitEqual();}
{desigualdad}       {return ops.unitInequal();}

{separador}       {return ops.unitBlockSeparator();}
{puntoycoma}       {return ops.unitVariableSeparator();}

[^]                       {error.errorLexico(fila(), lexema());}
