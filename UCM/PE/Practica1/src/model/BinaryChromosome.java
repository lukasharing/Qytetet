package model;

import java.util.ArrayList;

import model.Function.Pair;

public class BinaryChromosome extends Chromosome<Integer> {
	
	public BinaryChromosome(Function f, double p) {
		super(f, p);
		
		int number_arguments = f.getTotalArguments();
		
		// Random Values.
		
		for(int i = 0; i < number_arguments; ++i) {
			int interval = getGeneSize(f.getInterval(i));
			ArrayList<Integer> gen = new ArrayList<>(interval);
			for(int j = 0; j < interval; ++j) {
				gen.add(j, (int)(Math.random() * 2));
			}
			genes.add(i, gen);
		}
		
		
	};

	// Returns the gene in decimal
	private Integer getGene(int n){
		ArrayList<Integer> gene = genes.get(n);
		int result = 0x0;
		for(int i = 0; i < gene.size(); i++){
			result |= gene.get(i) << i;
		}
		return result;
	}

	// Returns number of bits needed for a given interval
	private Integer getGeneSize(Pair interval) {
		return (int)Math.ceil(
				Math.log(1 + (interval.second - interval.first) / prec)/Math.log(2)
			   );
	};
	
	// Bijective Function that given the id of the interval and (binary chain = number)
	// returns a point in the value.
	private Double getFenotype(Integer n) {
		Pair interval = func.getInterval(n);
		return interval.first + getGene(n) * (interval.second - interval.first) / (double)((1 << genes.get(n).size()) - 1);
	};
	
	public double[] getFenotypes(){
		double[] result = new double[genes.size()];
		for(int i = 0; i < genes.size(); ++i) {
			result[i] = getFenotype(i);
		}
		return result;
	};
	
	// -----------------------------------------------
	// - Mutations
	
	public void randomMutation(double prob) {
		boolean muted = false;
		for(int k = 0; k < genes.size(); ++k){
			ArrayList<Integer> gen = genes.get(k);
			for(int t = 0; t < gen.size(); ++t) {
				if(Math.random() <= prob) {
					gen.set(t, gen.get(t) ^ 0x1);
					muted = true;
				}
			}
			genes.set(k, gen);
		}
	};
	
	
	public static BinaryChromosome newInstance (Function f, Double p) {
		return new BinaryChromosome(f, p);
	};
}
