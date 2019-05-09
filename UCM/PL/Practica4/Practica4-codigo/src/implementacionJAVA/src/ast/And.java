package ast;

public class And extends BinaryOp{
	public And(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.AND; };
	
	@Override
	public String toString() { return "and(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
}
