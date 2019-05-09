package ast;

public class Greater extends BinaryOp{
	public Greater(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.GREATER; };
	
	@Override
	public String toString() { return "greater(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
}
