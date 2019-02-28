package model;

import java.util.*;
import java.util.stream.Collectors;

import sun.nio.cs.ext.GB18030;

public abstract class Chromosome<T> {

	protected ArrayList<T> genes;

	protected Function func;
	protected double prec = 0.0;
	
	public Chromosome(Function f, double p) {
		prec = p;
		func = f;
		genes = new ArrayList<T>(f.getTotalArguments());
	};
	
	public double[] getFenotypes(){ return null; };
	public void randomMutation(double prob) {};
	
	//protected static BinaryChromosome newBinary (Function f, Double p) { return null; };
	//protected RealChromosome newReal (Function f, Double p) { return null; };

	@Override
	public String toString() {
		String result = "\t\n Chromosome: \n";
		for(T gene : genes) {
			result += gene + ", \n";
		}
		result += "Evaluation: " + func.evaluate(this.getFenotypes()) + "\n";
		return result;
	}
	
	protected void cross(Chromosome chr1, int n) {};
	
	public Chromosome clone() { return null; };
}
