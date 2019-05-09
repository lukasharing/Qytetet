package ast;

public class cAssign extends Assign{
	private Assign initialization;
	
	public cAssign(Assign assign, String id, Exp exp) {
		super(id, exp);
		initialization = assign; 
	};
	
	
	@Override
	public TypeAssign type() { return TypeAssign.COMPLEX; };
	
	@Override
	public String toString() { return "ComplexAssign{ " + id + ", " + exp.toString() + ", " + initialization.toString() + " }; "; }
}
