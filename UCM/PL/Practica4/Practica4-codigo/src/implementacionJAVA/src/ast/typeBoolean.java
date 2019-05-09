package ast;

public class typeBoolean extends Type{
	
	@Override
	public TypeType type() { return TypeType.TYPE_BOOLEAN; };
	
	@Override
	public String toString() { return "bool"; };
	
}
