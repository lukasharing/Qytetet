package ast;

public abstract class Assign {
	
	protected String id;
	protected Exp exp;
	
	public Assign(String identificator, Exp expression) {
		id = identificator;
		exp = expression;
	};
	
	public String id() { return id; }
	public Exp exp() { return exp; }
	public Init init() { throw new UnsupportedOperationException("id"); }
	
	public abstract TypeAssign type();
	@Override
	public abstract String toString();
	
}
