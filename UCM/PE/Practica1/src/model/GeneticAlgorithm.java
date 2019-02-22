package model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class GeneticAlgorithm<T> {
	private ArrayList<T> chromosomes; //Population
	private T best_chromosome;
	
	private Class<T> class_type;
	
	// Function
	private Function evaluation = null;
	
	// Child and parents
	private int initial_population = 0;
	private int total_generations = 0;
	private double gene_precision = 0.0;
	
	// Probabilities
	private double crossing_prob = 0.0;
	private double mutation_prob = 0.0;
	
	// Constructor
	public GeneticAlgorithm(Class<T> class_type, int population, int generations, double crossoverProbability, double mutationProbability, double precision, Function evaluationFunction){
		this.class_type = class_type;
		this.initial_population = population;
		this.total_generations = generations;
		this.gene_precision = precision;
		
		this.crossing_prob = crossoverProbability;
		this.mutation_prob = mutationProbability;
		this.evaluation = evaluationFunction;
		
		chromosomes = new ArrayList<>();
		
		try{
			this.createInitialPopulation();
		}catch(Exception ex) {
			System.err.println(ex.getMessage());
			System.exit(0); 
		}
	};
	
	public void test() {
		this.selection();
		for(T chromosme : chromosomes) {
			//System.out.println(Arrays.toString(((Chromosome) chromosme).getFenotypes()));
			//System.out.println("Mutate");
			//((Chromosome)chromosme).randomMutation(this.mutation_prob);
			//System.out.println(Arrays.toString(((Chromosome) chromosme).getFenotypes()));
			//System.out.println("---------------");
		}
	};
	
	// Iterate
	public void run() {
		int currentGeneration = 0;
		
		this.evaluation();
		
		while(currentGeneration < total_generations) {
			currentGeneration++;
			this.selection();
			this.crossover();
			this.mutation();
			this.evaluation();
		}
	};
	
	
	// Init population
	public void createInitialPopulation() throws Exception{
		// Creationf Binary Chromosomes
		if(class_type.getName().contains("BinaryChromosome")) {
		
			for(int i = 0; i < initial_population; ++i) {
				chromosomes.add((T) BinaryChromosome.newBinary(evaluation, gene_precision));
			}
		
		// Creation of Real Chromosomes
		}else if(class_type.getName().contains("RealChromosome")) {
			for(int i = 0; i < initial_population; ++i) {
				//chromosomes.add((T) BinaryChromosome.newBinary(evaluation, gene_precision));
			}
		}
		
	}
	
	// Evaluate Population
	private void evaluation() {
		int c1 = 0;
		double v1 = evaluation.evaluate(((Chromosome)chromosomes.get(0)).getFenotypes());
		for(int i = 1; i < chromosomes.size(); ++i) {

			double v2 = evaluation.evaluate(
				((Chromosome)chromosomes.get(i)).getFenotypes()
			);
			
			if(v1 < v2) {
				c1 = i;
				v1 = v2;
			}
		}
		
		best_chromosome = chromosomes.get(c1);
	}
	
	private void selection() {
		int size = chromosomes.size();

		double division = 1.0 / size;
		double[] prob = new double[size];
		prob[0] = Math.random() * division;
		for(int i = 1; i < size; ++i) {
			prob[i] = prob[i - 1] + Math.min(division, (1.0 - prob[i - 1]) * Math.random());
		}
		System.out.println(Arrays.toString(prob));
	}
	
	private boolean crossover() {
		return true;
	}
	
	private void mutation() {
		for(T chromosome : chromosomes) {
			((Chromosome) chromosome).randomMutation(mutation_prob);
		}
	};
	
}
