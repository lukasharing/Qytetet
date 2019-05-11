package ast;

public class ASTTiny {
	public Program PROGRAM(Init init, Assign assign) { return new Program(init, assign); };

	public Init sInit(Type type, String id) { return new sInit(type, id); };
	public Init cInit(Init init, Type type, String id) { return new cInit(init, type, id); };

	public Assign sAssign(String id, Exp exp) { return new sAssign(id, exp); };
	public Assign cAssign(Assign assign, String id, Exp exp) { return new cAssign(assign, id, exp); };
	
	public Exp Add(Exp opLeft, Exp opRight) { return new Add(opLeft, opRight); }; 
	public Exp Sub(Exp opLeft, Exp opRight) { return new Sub(opLeft, opRight); }; 
	public Exp Mul(Exp opLeft, Exp opRight) { return new Mul(opLeft, opRight); }; 
	public Exp Div(Exp opLeft, Exp opRight) { return new Div(opLeft, opRight); };
	public Exp And(Exp opLeft, Exp opRight) { return new And(opLeft, opRight); };
	public Exp Or(Exp opLeft, Exp opRight) { return new Or(opLeft, opRight); };
	
	public Exp Less(Exp opLeft, Exp opRight) { return new Less(opLeft, opRight); }
	public Exp LessThan(Exp opLeft, Exp opRight) { return new LessThan(opLeft, opRight); };
	public Exp Greater(Exp opLeft, Exp opRight) { return new Greater(opLeft, opRight); };
	public Exp GreaterThan(Exp opLeft, Exp opRight) { return new GreaterThan(opLeft, opRight); };
	public Exp Eq(Exp opLeft, Exp opRight) { return new Eq(opLeft, opRight); };
	public Exp nEq(Exp opLeft, Exp opRight) { return new nEq(opLeft, opRight); }
	
	
	public Exp Not(Exp op) { return new Not(op); }
	public Exp UnaryMinus(Exp op) { return new UnaryMinus(op); }	
	
	public Type typeBoolean() { return new typeBoolean(); }
	public Type typeNumber() { return new typeNumber(); }
	public Exp ID(String id) { return new ID(id); }  
	public Exp Number(String st) { return new Number(st); } 
	public Exp Bool(String bool) { return new Boolean(bool); }
	
	
}
