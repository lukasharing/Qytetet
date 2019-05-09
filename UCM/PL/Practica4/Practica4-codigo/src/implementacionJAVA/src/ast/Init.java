package ast;

public abstract class Init {
	
	protected Type type;
	protected String id;
	
	public Init(Type _type, String identificator) {
		type = _type;
		id = identificator;
	};
	
	public String id() { return id; }
	public Type typeExpression() { return type; }
	public Init init() { throw new UnsupportedOperationException("id"); }
	
	public abstract TypeInit type();
	@Override
	public abstract String toString();
	
}
