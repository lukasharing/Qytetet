package model;

import java.util.*;
import java.util.stream.Collectors;

import sun.nio.cs.ext.GB18030;

public abstract class Chromosome<T> {

	protected ArrayList<ArrayList<T>> genes;

	protected Function func;
	protected double prec = 0.0;
	
	public Chromosome(Function f, double p) {
		prec = p;
		func = f;
		genes = new ArrayList<ArrayList<T>>(f.getTotalArguments());
	};
	
	public double[] getFenotypes(){ return null; };
	public void randomMutation(double prob) {};
	
	protected void cross(Chromosome<T> chr1, int n) {
		
		// Put all genes in one line
		List<T> unroll0 = this.genes.stream().flatMap(List::stream).collect(Collectors.toList());
		List<T> unroll1 = chr1.genes.stream().flatMap(List::stream).collect(Collectors.toList());
		
		List<T> cross0 = new ArrayList<>();
		List<T> cross1 = new ArrayList<>();

		int d0 = 0, d1 = unroll0.size();
		for(int k = 0; k < n; ++k) {
			int division = (int)(1 + (Math.random() * (d1 / (n - k) - 1)));
			
			List<T> sub0 = unroll0.subList(d0, d0 + division);
			List<T> sub1 = unroll1.subList(d0, d0 + division);
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
		ArrayList<ArrayList<T>> rs0 = new ArrayList<>();
		ArrayList<ArrayList<T>> rs1 = new ArrayList<>();
		
		for(int i = 0, r0 = 0; i < genes.size(); ++i) {
			int lgt = genes.get(i).size();
			rs0.add(new ArrayList<T>(cross0.subList(r0, r0 + lgt)));
			rs1.add(new ArrayList<T>(cross1.subList(r0, r0 + lgt)));
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
		for(ArrayList<T> gene : genes) {
			result += gene + ", \n";
		}
		return result;
	}
}
