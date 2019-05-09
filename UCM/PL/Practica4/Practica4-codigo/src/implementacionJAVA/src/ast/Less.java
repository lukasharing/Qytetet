package ast;

public class Less extends BinaryOp{
	public Less(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.LESS; };
	
	@Override
	public String toString() { return "less(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
}
