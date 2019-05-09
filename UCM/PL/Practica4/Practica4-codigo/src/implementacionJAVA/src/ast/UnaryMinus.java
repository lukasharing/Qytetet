package ast;

public class UnaryMinus extends UnaryOp{
	public UnaryMinus(Exp op1) {
		super(op1);
	};

	@Override
	public TypeExpression type() { return TypeExpression.UNARYMINUS; };
		
	@Override
	public String toString() {
		return "not(" + op.toString() + ")";
	};
}
