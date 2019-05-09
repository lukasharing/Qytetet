package ast;

public abstract class BinaryOp extends Exp{
	
	protected Exp opLeft;
	protected Exp opRight;
	
	public BinaryOp(Exp left, Exp right) {
		opLeft = left;
		opRight = right;
	};
	
	public Exp opLeft() { return opLeft; };
	public Exp opRight() { return opRight; };
	
	@Override
	public String toString() {return null;};
	
}
