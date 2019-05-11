package model;

import java.util.List;

public abstract class Function{

	public FunctionType maxmin;

	// Number of intervals == number of arguments
	@SuppressWarnings("rawtypes")
	protected List<Pair> arguments_intervals;

	public int getTotalArguments() {
		return arguments_intervals.size();
	};

	// Constructor
	public Function(FunctionType type) {
		maxmin = type;
	};

	// Returns nth Interval.
	@SuppressWarnings("unchecked")
	public Pair<Double, Double> getInterval(int i) {
		return arguments_intervals.get(i);
	};

	public double evaluate(Chromosome args) {
		return 0.0;
	};

	public int compare(double v1, Chromosome args) {
		if (evaluate(args) == v1)
			return 0;

		if (maxmin.equals(FunctionType.MAXIMIZE)) {
			return (evaluate(args) > v1) ? 1 : -1;
		} else {
			return (evaluate(args) < v1) ? 1 : -1;
		}
	};

}
