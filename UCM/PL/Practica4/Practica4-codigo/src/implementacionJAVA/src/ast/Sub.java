package ast;

public class Sub extends BinaryOp{

	public Sub(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.SUB; };
	
	@Override
	public String toString() { return "sub(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
	
}
