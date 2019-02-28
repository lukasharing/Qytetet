package model;

import java.util.*;
import java.util.stream.Collectors;

import sun.nio.cs.ext.GB18030;

public abstract class Chromosome<T, N> {

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
	
	protected void cross(Chromosome<T, N> chr1, int n) {
		// Put all genes in one line
		List<N> unroll0 = this.plain();
		List<N> unroll1 = chr1.plain();
		
		List<N> cross0 = new ArrayList<>();
		List<N> cross1 = new ArrayList<>();

		int d0 = 0, d1 = unroll0.size();
		for(int k = 0; k < n; ++k) {
			int division = (int)(1 + (Math.random() * (d1 / (n - k) - 1)));
			
			List<N> sub0 = unroll0.subList(d0, d0 + division);
			List<N> sub1 = unroll1.subList(d0, d0 + division);
			if((k & 0x1) == 0x1) {
				cross0.addAll(sub1);
				cross1.addAll(sub0);
			}else {
				cross0.addAll(sub0);
				cross1.addAll(sub1);
			}
			
			d0 += division;
			d1 -= division;
		}
		
		if((n & 0x1) == 0x1) {
			cross0.addAll(unroll1.subList(d0, unroll0.size()));
			cross1.addAll(unroll0.subList(d0, unroll0.size()));
		}else {
			cross0.addAll(unroll0.subList(d0, unroll0.size()));
			cross1.addAll(unroll1.subList(d0, unroll0.size()));
		}
		
		// Divide into genes
		ArrayList<T> rs0 = new ArrayList<>();
		ArrayList<T> rs1 = new ArrayList<>();
		
		for(int i = 0, r0 = 0; i < genes.size(); ++i) {
			int lgt = ((ArrayList<T>) genes.get(i)).size();
			rs0.add((T)new ArrayList<N>(cross0.subList(r0, r0 + lgt)));
			rs1.add((T)new ArrayList<N>(cross1.subList(r0, r0 + lgt)));
			r0 += lgt;
		}
		
		this.genes = rs0;
		chr1.genes = rs1;
		
	};
	
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
	
	
	public List<N> plain(){ return null; };
	
	public Chromosome clone() { return null; };
}
