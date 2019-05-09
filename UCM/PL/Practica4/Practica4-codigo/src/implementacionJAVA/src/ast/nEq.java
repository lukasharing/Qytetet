package ast;

public class nEq extends BinaryOp{

	public nEq(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	}
	
	@Override
	public TypeExpression type() { return TypeExpression.NEQ; };
	
	@Override
	public String toString() { return "nEq(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
}
