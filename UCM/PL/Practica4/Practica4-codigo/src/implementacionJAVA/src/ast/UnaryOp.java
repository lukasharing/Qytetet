package ast;

public abstract class UnaryOp extends Exp{
	protected Exp op;
	
	public UnaryOp(Exp op1) {
		op = op1;
	};
	
	public Exp op() { return op; };

	@Override
	public String toString() {return null;};
}
