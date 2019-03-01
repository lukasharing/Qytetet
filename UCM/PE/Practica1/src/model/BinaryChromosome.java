package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BinaryChromosome extends Chromosome<ArrayList<Integer>> {

	public BinaryChromosome(Function f, double p) {
		super(f, p);

		int number_arguments = f.getTotalArguments();

		// Random Values.

		for (int i = 0; i < number_arguments; ++i) {
			int interval = getGeneSize(f.getInterval(i));
			ArrayList<Integer> gen = new ArrayList<>(interval);
			for (int j = 0; j < interval; ++j) {
				gen.add(j, (int) (Math.random() * 2));
			}
			genes.add(i, gen);
		}

	};

	@SuppressWarnings("unchecked")
	public BinaryChromosome(Function f, double p, ArrayList<ArrayList<Integer>> cloning_genes) {
		super(f, p);
		for (ArrayList<Integer> gen : cloning_genes) {
			genes.add((ArrayList<Integer>) gen.clone());
		}
	};

	// Returns the gene in decimal
	private Integer getGene(int n) {
		ArrayList<Integer> gene = genes.get(n);
		int result = 0x0;
		for (int i = 0; i < gene.size(); i++) {
			result |= gene.get(i) << i;
		}
		return result;
	}

	// Returns number of bits needed for a given interval
	private Integer getGeneSize(Pair<Double, Double> interval) {
		return (int) Math.ceil(Math.log(1 + ((Double) interval.second - (Double) interval.first) / prec) / Math.log(2));
	};

	// Bijective Function that given the id of the interval and (binary chain =
	// number)
	// returns a point in the value.
	private Double getFenotype(Integer n) {
		@SuppressWarnings("rawtypes")
		Pair interval = func.getInterval(n);
		return (Double) interval.first + getGene(n) * ((Double) interval.second - (Double) interval.first)
				/ (double) ((1 << genes.get(n).size()) - 1);
	};

	public double[] getFenotypes() {
		double[] result = new double[genes.size()];
		for (int i = 0; i < genes.size(); ++i) {
			result[i] = getFenotype(i);
		}
		return result;
	};

	// -----------------------------------------------
	// - Mutations

	public void randomMutation(double prob) {
		for (int k = 0; k < genes.size(); ++k) {
			ArrayList<Integer> gen = genes.get(k);
			for (int t = 0; t < gen.size(); ++t) {
				if (Math.random() <= prob) {
					gen.set(t, gen.get(t) ^ 0x1);
				}
			}
			genes.set(k, gen);
		}
	};

	// -------------------------------------------------
	// Crossing
	protected void cross(BinaryChromosome chr1, int n) {
		// Put all genes in one line
		List<Integer> unroll0 = this.genes.stream().flatMap(List::stream).collect(Collectors.toList());

		List<Integer> unroll1 = chr1.genes.stream().flatMap(List::stream).collect(Collectors.toList());

		List<Integer> cross0 = new ArrayList<>();
		List<Integer> cross1 = new ArrayList<>();

		int d0 = 0, d1 = unroll0.size();
		for (int k = 0; k < n; ++k) {
			int division = (int) (1 + (Math.random() * (d1 / (n - k) - 1)));

			List<Integer> sub0 = unroll0.subList(d0, d0 + division);
			List<Integer> sub1 = unroll1.subList(d0, d0 + division);
			if ((k & 0x1) == 0x1) {
				cross0.addAll(sub1);
				cross1.addAll(sub0);
			} else {
				cross0.addAll(sub0);
				cross1.addAll(sub1);
			}

			d0 += division;
			d1 -= division;
		}

		if ((n & 0x1) == 0x1) {
			cross0.addAll(unroll1.subList(d0, unroll0.size()));
			cross1.addAll(unroll0.subList(d0, unroll0.size()));
		} else {
			cross0.addAll(unroll0.subList(d0, unroll0.size()));
			cross1.addAll(unroll1.subList(d0, unroll0.size()));
		}

		// Divide into genes
		ArrayList<ArrayList<Integer>> rs0 = new ArrayList<>();
		ArrayList<ArrayList<Integer>> rs1 = new ArrayList<>();

		for (int i = 0, r0 = 0; i < genes.size(); ++i) {
			int lgt = genes.get(i).size();
			rs0.add(new ArrayList<Integer>(cross0.subList(r0, r0 + lgt)));
			rs1.add(new ArrayList<Integer>(cross1.subList(r0, r0 + lgt)));
			r0 += lgt;
		}

		this.genes = rs0;
		chr1.genes = rs1;
	};

	public static BinaryChromosome newInstance(Function f, Double p) {
		return new BinaryChromosome(f, p);
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Chromosome clone() {
		return new BinaryChromosome(this.func, this.prec, this.genes);
	};
}
