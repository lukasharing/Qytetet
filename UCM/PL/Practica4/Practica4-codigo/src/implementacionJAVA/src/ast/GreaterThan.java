package ast;

public class GreaterThan extends BinaryOp{
	public GreaterThan(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.GREATERTHAN; };
	
	@Override
	public String toString() { return "greater_than(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
}
