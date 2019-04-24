package p3;

import java.util.ArrayList;

import model.Function;
import model.FunctionType;
import model.Pair;
import p2.CitiesChromosome;

public class FunctionAnt extends Function{
	@SuppressWarnings("rawtypes")
	public FunctionAnt(int n, FunctionType type) {
		super(type);
		arguments_intervals = new ArrayList<Pair>();
		arguments_intervals.add(new Pair<Integer, Integer>(0, 0));
	}
	
	public double evaluate(double... args) {
		return 0;
	};
}
