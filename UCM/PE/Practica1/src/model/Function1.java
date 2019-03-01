package model;

import java.util.ArrayList;

public class Function1 extends Function {

	@SuppressWarnings("rawtypes")
	public Function1(FunctionType type) {
		super(type);
		arguments_intervals = new ArrayList<Pair>();
		arguments_intervals.add(new Pair<Double, Double>(-3.0, +12.1));
		arguments_intervals.add(new Pair<Double, Double>(+4.1, +5.8));
	};

	public double evaluate(double... args) {
		return 21.5 + args[0] * Math.sin(4 * Math.PI * args[0]) + args[1] * Math.sin(20 * Math.PI * args[1]);
	};

}
