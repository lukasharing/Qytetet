package model;

import java.util.ArrayList;

public class Function4 extends Function {
		
	public Function4(int n) {
		arguments_intervals = new ArrayList<Pair>(n);
		for(int i = 0; i < n; ++i) {
			arguments_intervals.set(i, new Pair(0.0, Math.PI));
		}
	};
	
	public double evaluate(Double... args) {
		double xisin = 0.0;
		for(int i = 0; i < args.length; ++i) {
			xisin += Math.sin(args[i]) *
					 Math.pow(Math.sin((i+1)*Math.pow(args[i], 2.0)/Math.PI), 20.0);
		}
		return -xisin;
	};
	
}
