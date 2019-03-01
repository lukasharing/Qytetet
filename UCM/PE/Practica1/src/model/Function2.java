package model;

import java.util.ArrayList;

public class Function2 extends Function {
		
	@SuppressWarnings("rawtypes")
	public Function2(FunctionType type) {
		super(type);
		arguments_intervals = new ArrayList<Pair>();
		arguments_intervals.add(new Pair<Double, Double>(-512.0, +512.0));
		arguments_intervals.add(new Pair<Double, Double>(-512.0, +512.0));
	};
	
	public double evaluate(double... args) {
		return -(args[1] + 47) * Math.sin(Math.sqrt(Math.abs(args[1]+args[0]/2+47))) -
				args[0]*Math.sin(Math.sqrt(Math.abs(args[0]-(args[1]+47))));
	};
	
}
