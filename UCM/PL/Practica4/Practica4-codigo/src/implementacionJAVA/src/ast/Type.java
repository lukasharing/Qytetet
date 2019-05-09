package ast;

public abstract class Type {
	public TypeType type() { throw new UnsupportedOperationException("type"); };
	
	public abstract String toString();
}
