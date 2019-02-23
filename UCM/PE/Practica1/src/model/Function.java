package model;

import java.util.List;

public abstract class Function {
	
	
	public class Pair{

		  public final Double first;
		  public final Double second;
		  
		  public Pair(Double left, Double right) {
		    this.first = left;
		    this.second = right;
		  }
	}
	
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
	

	public boolean compare(double v1, double... args) {
		if(maxmin.equals(FunctionType.MAXIMIZE)) {
			return (evaluate(args) > v1);
		}else {
			return (evaluate(args) < v1);
		}
	};
	
}
