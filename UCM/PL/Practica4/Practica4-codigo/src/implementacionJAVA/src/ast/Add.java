package ast;

public class Add extends BinaryOp{
	public Add(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.ADD; };
	
	@Override
	public String toString() { return "add(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
}
