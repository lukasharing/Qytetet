package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

public class GeneticAlgorithm<T> {

	private final int TOURNAMENT_SET = 3;
	private final double TOURNAMENT_PROB = 0.60;

	private ArrayList<T> chromosomes; // Population
	private T best_chromosome;

	private Class<T> class_type;

	// Function
	private Function function = null;

	// Child and parents
	private int initial_population = 0;
	private int total_generations = 0;
	private double gene_precision = 0.0;

	// Probabilities
	private double crossing_prob = 0.0;
	private double mutation_prob = 0.0;

	// Constructor
	public GeneticAlgorithm(Class<T> class_type, int population, int generations, double crossoverProbability,
			double mutationProbability, double precision, Function evaluationFunction) {
		this.class_type = class_type;
		this.initial_population = population;
		this.total_generations = generations;
		this.gene_precision = precision;

		this.crossing_prob = crossoverProbability;
		this.mutation_prob = mutationProbability;
		this.function = evaluationFunction;

		chromosomes = new ArrayList<>();

		try {
			this.createInitialPopulation();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			System.exit(0);
		}
	};

	public void test() {
		this.selection(SelectionType.ROULETTE, this.evaluation(false));
		/*
		 * for(T chromosme : chromosomes) { System.out.println(((Chromosome)
		 * chromosme).toString()); System.out.println("Mutate");
		 * ((Chromosome)chromosme).randomMutation(this.mutation_prob);
		 * System.out.println(((Chromosome) chromosme).toString());
		 * System.out.println("---------------"); }
		 */
	};

	// Iterate
	public List<Double> run() {
		int currentGeneration = 0;
		List<Double> report = new ArrayList<Double>();

		double[] eval_result = this.evaluation(false);
		report.add((Double) Arrays.stream(eval_result).max().getAsDouble());
		System.out.println(eval_result);
		while (currentGeneration < total_generations) {
			currentGeneration++;
			this.selection(SelectionType.ROULETTE, eval_result);
			this.crossover(1);
			this.mutation();
			eval_result = this.evaluation(false);
			report.add((Double) Arrays.stream(eval_result).max().getAsDouble());
			
			System.out.println(Arrays.toString(eval_result));
		}
		System.out.println("END");
		return report;
	};

	// Init population
	public void createInitialPopulation() throws Exception {
		// Creationf Binary Chromosomes
		if (class_type.getName().contains("BinaryChromosome")) {

			for (int i = 0; i < initial_population; ++i) {
				chromosomes.add((T) BinaryChromosome.newInstance(function, gene_precision));
			}

			// Creation of Real Chromosomes
		} else if (class_type.getName().contains("RealChromosome")) {
			for (int i = 0; i < initial_population; ++i) {
				// chromosomes.add((T) BinaryChromosome.newInstance(evaluation,
				// gene_precision));
			}
		}

	}

	// Evaluate Population
	private double[] evaluation(boolean elitism) {
		int size = chromosomes.size();
		double[] eval_results = new double[size];
		for (int i = 0; i < size; ++i) {
			eval_results[i] = function.evaluate(((Chromosome) chromosomes.get(i)).getFenotypes());
		}

		DoubleStream map1 = Arrays.stream(eval_results);
		double max = map1.max().getAsDouble();
		DoubleStream map2 = Arrays.stream(eval_results).map((e) -> max - e);
		double total_sum = map2.sum();
		DoubleStream map3 = Arrays.stream(eval_results).map((e) -> (max - e) / total_sum);
		return map3.toArray();
	};

	private void selection(SelectionType type, double[] evaluations) {
		ArrayList<T> generation = new ArrayList<>();
		switch (type) {
		case ROULETTE:

			for (int i = 1; i < evaluations.length; ++i) {
				evaluations[i] += evaluations[i - 1];
			}
			for (int i = 0; i < initial_population; ++i) {
				// Lanzamos y buscamos el siguiente elemento el cual supere el
				// valor de la ruleta
				double roulette = Math.random();
				int throul = 0;
				while (evaluations[throul++] < roulette)
					;

				// Nos quedamos con el anterior.
				generation.add(chromosomes.get(--throul));
			}

			break;
		case DETE_TOURNAMENT:
		case PRB_TOURNAMENT:

			for (int i = 0; i < initial_population; ++i) {
				// Select random chromosome (Suppose it is the best).
				int first = (int) (Math.random() * initial_population);
				Chromosome best_cr = ((Chromosome) chromosomes.get(first));
				Chromosome worst_cr = best_cr;
				double best = function.evaluate(best_cr.getFenotypes());
				double worst = best;

				// Generate n more and compare.
				for (int j = 0; j < TOURNAMENT_SET; ++j) {
					int random = (int) (Math.random() * initial_population);
					Chromosome chromosme = (Chromosome) chromosomes.get(random);
					// Check if better than the best.
					if (function.compare(best, chromosme.getFenotypes())) {
						best = function.evaluate(chromosme.getFenotypes());
						best_cr = chromosme;
					} else {
						// Check if it is worse than the worst.
						if (function.compare(worst, chromosme.getFenotypes())) {
							best = function.evaluate(chromosme.getFenotypes());
							worst_cr = chromosme;
						}
					}
				}

				if (type == SelectionType.DETE_TOURNAMENT) {
					generation.add((T) best_cr);
				} else {
					if (Math.random() <= TOURNAMENT_PROB) {
						generation.add((T) best_cr);
					} else {
						generation.add((T) worst_cr);
					}
				}
			}

			this.chromosomes = generation;
			break;
		}
		this.chromosomes = generation;
	}

	private void crossover(int n) {

		ArrayList<Integer> quieren_cruzarse = new ArrayList<>();
		for (int i = 0; i < initial_population; ++i) {
			if (Math.random() <= this.crossing_prob) {
				quieren_cruzarse.add(i);
			}
		}

		// Convertir en par
		int size = quieren_cruzarse.size() & ~0x1;

		for (int i = 0; i < size; i += 2) {
			Chromosome chr1 = (Chromosome) chromosomes.get(quieren_cruzarse.get(i + 0));
			Chromosome chr2 = (Chromosome) chromosomes.get(quieren_cruzarse.get(i + 1));

			chr1.cross(chr2, n); // Symmetric
		}

	};

	private void mutation() {
		for (T chromosome : chromosomes) {
			((Chromosome) chromosome).randomMutation(mutation_prob);
		}
	};

}
