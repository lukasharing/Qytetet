package ast;

public class Eq extends BinaryOp{
	public Eq(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.EQ; };
	
	@Override
	public String toString() { return "eq(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
}
