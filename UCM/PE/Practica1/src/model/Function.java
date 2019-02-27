package model;

import java.util.List;

public abstract class Function {
	
	public FunctionType maxmin;
	
	// Number of intervals == number of arguments
	protected List<Pair> arguments_intervals;
	
	int getTotalArguments() { return arguments_intervals.size(); };
	
	// Constructor
	public Function(FunctionType type) {
		maxmin = type;
	};
	
	// Returns nth Interval.
	public Pair getInterval(int i) { return arguments_intervals.get(i); };
	

	public double evaluate(double... args) {
		return 0.0;
	};
	
	public int compare(double v1, double... args) {
		if(evaluate(args) == v1) return 0;
		
		if(maxmin.equals(FunctionType.MAXIMIZE)) {
			return (evaluate(args) > v1) ? 1 : -1;
		}else {
			return (evaluate(args) < v1) ? 1 : -1;
		}
	};
	
}
