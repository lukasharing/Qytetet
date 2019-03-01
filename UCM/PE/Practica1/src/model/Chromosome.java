package model;

import java.util.*;

public abstract class Chromosome<T> {

	protected ArrayList<T> genes;

	protected Function func;
	protected double prec = 0.0;

	public Chromosome(Function f, double p) {
		prec = p;
		func = f;
		genes = new ArrayList<T>(f.getTotalArguments());
	};

	public double[] getFenotypes() {
		return null;
	};

	public void randomMutation(double prob) {
	};

	@Override
	public String toString() {
		String result = "\t\n Chromosome: \n";
		for (T gene : genes) {
			result += gene + ", \n";
		}
		result += "Evaluation: " + func.evaluate(this.getFenotypes()) + "\n";
		return result;
	}

	protected void cross(Chromosome<T> chr1, int n) {
	};

	public Chromosome<T> clone() {
		return null;
	};
}
