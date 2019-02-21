package model;

import java.util.ArrayList;

import model.Function.Pair;

public class BinaryChromosome extends Chromosome<Integer> {

	private ArrayList<Integer> max_gene_size;
	
	public BinaryChromosome(Function f, double p) {
		super(f, p);
		
		int number_arguments = f.getTotalArguments();
		max_gene_size = new ArrayList<>(number_arguments);
		
		for(int i = 0; i < number_arguments; ++i) {
			max_gene_size.add(getGeneSize(f.getInterval(i)));
		}
		
		// Random Values.
		for(int i = 0; i < number_arguments; ++i) {
			genes.add((int)(Math.random() * (1 << max_gene_size.get(i))));
		}
		
	};

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
		return interval.first + genes.get(n) * (interval.second - interval.first) / (double)((1 << max_gene_size.get(n)) - 1);
	};
	
	public Double[] getFenotypes(){
		Double[] result = new Double[genes.size()];
		for(int i = 0; i < genes.size(); ++i) {
			result[i] = getFenotype(i);
		}
		return result;
	};
	
	// -----------------------------------------------
	// - Mutations
	
	public void randomMutation(double prob) {
		for(int k = 0; k < genes.size(); ++k){
			int gen = genes.get(k), gsize = max_gene_size.get(k);
			for(int t = 0; t < gsize; ++t) {
				if(Math.random() <= prob) {
					genes.set(k, gen & ~(0x1 << t));
					genes.set(k, gen | (((gen >> t) & 0x1) ^ 0x1) << t);
				}
			}
		}
	};
	
	
	public static BinaryChromosome newBinary (Function f, Double p) {
		return new BinaryChromosome(f, p);
	};
}
