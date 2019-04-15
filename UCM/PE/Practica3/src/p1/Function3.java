package p1;

import java.util.ArrayList;

import model.Function;
import model.FunctionType;
import model.Pair;

public class Function3 extends Function {

	@SuppressWarnings("rawtypes")
	public Function3(FunctionType type) {
		super(type);
		arguments_intervals = new ArrayList<Pair>();
		arguments_intervals.add(new Pair<Double, Double>(-10.0, +10.0));
		arguments_intervals.add(new Pair<Double, Double>(-10.0, +10.0));
	};

	public double evaluate(double... args) {
		double x1cos = 0.0, x2cos = 0.0;
		for (int i = 1; i <= 5; ++i) {
			x1cos += i * Math.cos((i + 1) * args[0] + i);
			x2cos += i * Math.cos((i + 1) * args[1] + i);
		}
		return x1cos * x2cos;
	};

}
