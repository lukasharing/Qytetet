package ast;

public class Div extends BinaryOp{
	public Div(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.DIV; };
	
	@Override
	public String toString() { return "div(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
}
