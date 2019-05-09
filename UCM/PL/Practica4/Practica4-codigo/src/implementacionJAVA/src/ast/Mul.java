package ast;

public class Mul extends BinaryOp{
	public Mul(Exp opnd1, Exp opnd2) {
		super(opnd1, opnd2);
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.MUL; };
	
	@Override
	public String toString() { return "mul(" + opLeft.toString() + ", " + opRight.toString() + ")"; };
}
