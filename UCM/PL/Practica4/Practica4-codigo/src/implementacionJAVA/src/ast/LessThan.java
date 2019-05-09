package ast;

public class LessThan extends BinaryOp{
	public LessThan(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.LESSTHAN; };
	
	@Override
	public String toString() { return "less_than(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
}
