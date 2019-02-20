package model;

import java.util.ArrayList;

import model.Function.Pair;

public class BinaryChromosome extends Chromosome<Integer> {
	final double PRECISION = 0.0001;

	private ArrayList<Integer> max_gene_size;
	private ArrayList<Integer> genes_value;
	
	public BinaryChromosome(Function f) {
		super(f);
		
		int number_arguments = f.getTotalArguments();
		max_gene_size = new ArrayList<>(number_arguments);
		
		for(int i = 0; i < number_arguments; ++i) {
			max_gene_size.add(getGeneSize(f.getInterval(i)));
		}
		
		// Random Values.
		for(int i = 0; i < number_arguments; ++i) {
			genes_value.add((int)(Math.random() * (1 << max_gene_size.get(i))));
		}
		
	};

	// Returns number of bits needed for a given interval
	private Integer getGeneSize(Pair interval) {
		return (int)Math.ceil(
				Math.log(1 + (interval.second - interval.first) / PRECISION)/Math.log(2)
			   );
	};
	
	// Bijective Function that given the id of the interval and (binary chain = number)
	// returns a point in the value.
	private Double getFenotype(Integer n) {
		Pair interval = func.getInterval(n);
		return interval.first + genes_value.get(n) * (interval.second - interval.first) / (double)((1 << max_gene_size.get(n)) - 1);
	};
	
	public Double[] getFenotypes(){
		Double[] result = new Double[genes_value.size()];
		for(int i = 0; i < genes_value.size(); ++i) {
			result[i] = getFenotype(i);
		}
		return result;
	};
}
