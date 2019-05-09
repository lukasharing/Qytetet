package ast;

public class Not extends UnaryOp{
	
	public Not(Exp op1) {
		super(op1);
	};

	@Override
	public TypeExpression type() { return TypeExpression.NOT; };
		
	@Override
	public String toString() {
		return "not(" + op.toString() + ")";
	};
}
