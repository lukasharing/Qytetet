package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Chromosome<T> {
	
	protected List<T> genes;
	protected Function func;
	protected double prec = 0.0;
	
	public List<T> getGenes(){ return genes; };
	
	public Chromosome(Function f, double p) {
		prec = p;
		func = f;
		genes = new ArrayList<T>(f.getTotalArguments());
	};
	
	public double[] getFenotypes(){ return null; };
	public void randomMutation(double prob) {};
	
	protected static BinaryChromosome newBinary (Function f, Double p) { return null; };
	//protected RealChromosome newReal (Function f, Double p) { return null; };

	@Override
	public String toString() {
		/*String result = "[";
		for(T gene : genes) {
			result += Integer.toBinaryString((int)gene) + ",";
		}
		result += "]";*/
		return genes.toString();
	}
}
