package ast;

public class Program {
	
	private Init initialization;
	private Assign assignment;
	public Program(Init init, Assign assign){
		initialization = init;
		assignment = assign;
	}
	
	
	public Init init() { return initialization; };
	public Assign assign() { return assignment; };
	
	@Override
	public String toString() {
		return "PROGRAM("+ initialization.toString() +" /*&&*/, "+ assignment.toString() +")";
	}
}
