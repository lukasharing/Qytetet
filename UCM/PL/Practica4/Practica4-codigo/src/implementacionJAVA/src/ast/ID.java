package ast;

public class ID extends Exp{
	
	private String id;
	
	public ID(String name) {
		id = name;
	};
	
	@Override
	public TypeExpression type() { return TypeExpression.ID; }

	@Override
	public String toString() { return id; };
}
