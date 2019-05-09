package ast;

public class Boolean extends Exp{

	private String value;
	
	public Boolean(String bool) {
		value = bool;
	};
	
	public String value() { return this.value; };
	
	@Override
	public TypeExpression type() { return TypeExpression.BOOLEAN; };

	@Override
	public String toString() { return value; }
	
}
