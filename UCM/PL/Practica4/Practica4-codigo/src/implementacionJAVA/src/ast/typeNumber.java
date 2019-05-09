package ast;

public class typeNumber extends Type{
	@Override
	public TypeType type() { return TypeType.TYPE_NUMBER; };
	
	@Override
	public String toString() { return "num"; };
}
