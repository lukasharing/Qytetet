package ast;

public class sAssign extends Assign{


	public sAssign(String id, Exp exp) {
		super(id, exp);
	};

	@Override
	public TypeAssign type() { return TypeAssign.SIMPLE; }

	@Override
	public String toString() { return "SimpleAssign{ " + id + ", " + exp.toString() + "}; "; };
	
}
