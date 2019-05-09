package ast;

public class Number extends Exp{

	private String value;
	
	public Number(String number) {
		value = number;
	};
	
	public String value() { return this.value; };
	
	@Override
	public TypeExpression type() { return TypeExpression.NUMBER; };

	@Override
	public String toString() { return value; }
	
}
