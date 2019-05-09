package ast;

public class sInit extends Init{

	public sInit(Type type, String id) {
		super(type, id);
	};

	@Override
	public TypeInit type() { return TypeInit.SIMPLE; }

	@Override
	public String toString() { return "SimpleInit{ " + type.toString() + " = " + id + "}; "; };
}
