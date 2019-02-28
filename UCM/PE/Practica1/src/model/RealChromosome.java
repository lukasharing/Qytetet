package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RealChromosome extends Chromosome<Double, Double> {
	
	private MutationType mutation;
	
	public RealChromosome(Function f) {
		super(f, 0.0);
		
		int number_arguments = f.getTotalArguments();
		
		// Random Values.
		for(int i = 0; i < number_arguments; ++i) {
			genes.add(i, randomGen(i));
		}
		
	};
	
	public RealChromosome(Function f, double p, ArrayList<Double> cloning_genes) {
		super(f, p);
		this.genes = (ArrayList<Double>) cloning_genes.clone();
	};

	// Returns number of bits needed for a given interval
	private Integer getGeneSize(Pair<Double, Double> interval) {
		return (int)Math.ceil(
				Math.log(1 + ((Double)interval.second - (Double)interval.first) / prec)/Math.log(2)
			   );
	};
	
	// Bijective Function that given the id of the interval and (binary chain = number)
	// returns a point in the value.
	private Double getFenotype(Integer n) {
		return genes.get(n);
	};
	
	public double[] getFenotypes(){
		double[] result = new double[genes.size()];
		for(int i = 0; i < genes.size(); ++i) {
			result[i] = getFenotype(i);
		}
		return result;
	};
	
	private double randomGen(int i) {
		return func.getInterval(i).first + Math.random() * (func.getInterval(i).second - func.getInterval(i).first);
	};
	
	// -----------------------------------------------
	// - Mutations
	public void randomMutation(double prob) {
		
		switch(mutation) {
			case UNIFORM:
				for(int k = 0; k < genes.size(); ++k){
					if(Math.random() < prob) {
						genes.set(k, randomGen(k));
					}
				}
			break;
			case NONUNIFORM:
				for(int k = 0; k < genes.size(); ++k) {
					if(Math.random() < prob) {
						double gn = genes.get(k);
						double di = Math.abs(genes.get(k) - func.getInterval(k).first);
						double dj = Math.abs(genes.get(k) - func.getInterval(k).second);
						genes.set(k, gn + Math.min(di, dj) * (2 * Math.random() - 1));
					}
				}
			break;
		
		}
		boolean muted = false;
		
		
	};
	
	
	public static RealChromosome newInstance (Function f) {
		return new RealChromosome(f);
	};
	
	public List<Double> plain(){
		return (List)genes;
	};
	
	
	public Chromosome clone() { return new RealChromosome(this.func, this.prec, this.genes); };
}
