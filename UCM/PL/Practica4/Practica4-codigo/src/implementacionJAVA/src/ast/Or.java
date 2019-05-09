package ast;

public class Or extends BinaryOp{

	public Or(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.OR; };
	
	@Override
	public String toString() { return "or(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
	
}
