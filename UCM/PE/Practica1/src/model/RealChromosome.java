package model;

import java.util.ArrayList;
import java.util.List;

public class RealChromosome extends Chromosome<Double> {

	private MutationType mutation;

	public RealChromosome(Function f) {
		super(f, 0.0);

		mutation = MutationType.NONUNIFORM;
		int number_arguments = f.getTotalArguments();

		// Random Values.
		for (int i = 0; i < number_arguments; ++i) {
			genes.add(i, randomGen(i));
		}

	};

	@SuppressWarnings("unchecked")
	public RealChromosome(Function f, double p, ArrayList<Double> cloning_genes) {
		super(f, p);
		mutation = MutationType.UNIFORM;
		this.genes = (ArrayList<Double>) cloning_genes.clone();
	};

	// Returns number of bits needed for a given interval
	@SuppressWarnings("unused")
	private Integer getGeneSize(Pair<Double, Double> interval) {
		return (int) Math.ceil(Math.log(1 + ((Double) interval.second - (Double) interval.first) / prec) / Math.log(2));
	};

	// Bijective Function that given the id of the interval and (binary chain =
	// number)
	// returns a point in the value.
	private Double getFenotype(Integer n) {
		return genes.get(n);
	};

	public double[] getFenotypes() {
		double[] result = new double[genes.size()];
		for (int i = 0; i < genes.size(); ++i) {
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

		switch (mutation) {
		case UNIFORM:
			for (int k = 0; k < genes.size(); ++k) {
				if (Math.random() < prob) {
					genes.set(k, randomGen(k));
				}
			}
			break;
		case NONUNIFORM:
			for (int k = 0; k < genes.size(); ++k) {
				if (Math.random() < prob) {
					double gn = genes.get(k);
					double di = Math.abs(genes.get(k) - func.getInterval(k).first);
					double dj = Math.abs(genes.get(k) - func.getInterval(k).second);
					genes.set(k, gn + Math.min(di, dj) * (2 * Math.random() - 1));
				}
			}
			break;

		}

	};

	public static RealChromosome newInstance(Function f) {
		return new RealChromosome(f);
	};

	@SuppressWarnings("unchecked")
	protected void cross(@SuppressWarnings("rawtypes") Chromosome chr1, int n) {
		// Put all genes in one line
		List<Double> unroll0 = this.genes;
		List<Double> unroll1 = chr1.genes;

		ArrayList<Double> cross0 = new ArrayList<>();
		ArrayList<Double> cross1 = new ArrayList<>();

		int d0 = 0, d1 = unroll0.size();
		for (int k = 0; k < n; ++k) {
			int division = (int) (1 + (Math.random() * (d1 / (n - k) - 1)));

			List<Double> sub0 = unroll0.subList(d0, d0 + division);
			List<Double> sub1 = unroll1.subList(d0, d0 + division);
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

		this.genes = cross0;
		chr1.genes = cross1;
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Chromosome clone() {
		return new RealChromosome(this.func, this.prec, this.genes);
	};
}
