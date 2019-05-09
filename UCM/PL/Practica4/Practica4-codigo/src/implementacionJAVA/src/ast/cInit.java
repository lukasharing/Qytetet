package ast;

public class cInit extends Init{
	private Init initialization;
	
	public cInit(Init init, Type type, String id) {
		super(type, id);
		initialization = init; 
	};
	
	
	@Override
	public TypeInit type() { return TypeInit.COMPLEX; };
	
	@Override
	public String toString() { return "ComplexInit{ " + id + ", " + type.toString() + ", " + initialization.toString() + " }; "; }
}
