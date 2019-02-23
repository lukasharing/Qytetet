package model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

public class GeneticAlgorithm<T> {
	private ArrayList<T> chromosomes; //Population
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
	public GeneticAlgorithm(Class<T> class_type, int population, int generations, double crossoverProbability, double mutationProbability, double precision, Function evaluationFunction){
		this.class_type = class_type;
		this.initial_population = population;
		this.total_generations = generations;
		this.gene_precision = precision;
		
		this.crossing_prob = crossoverProbability;
		this.mutation_prob = mutationProbability;
		this.function = evaluationFunction;
		
		chromosomes = new ArrayList<>();
		
		try{
			this.createInitialPopulation();
		}catch(Exception ex) {
			System.err.println(ex.getMessage());
			System.exit(0); 
		}
	};
	
	public void test() {
		this.selection(SelectionType.ROULETTE, this.evaluation(false));
		/*for(T chromosme : chromosomes) {
			System.out.println(Arrays.toString(((Chromosome) chromosme).getFenotypes()));
			System.out.println("Mutate");
			((Chromosome)chromosme).randomMutation(this.mutation_prob);
			System.out.println(Arrays.toString(((Chromosome) chromosme).getFenotypes()));
			System.out.println("---------------");
		}*/
	};
	
	// Iterate
	public void run() {
		int currentGeneration = 0;
		
		
		double[] eval_result = this.evaluation(false);
		
		while(currentGeneration < total_generations) {
			currentGeneration++;
			this.selection(SelectionType.ROULETTE, eval_result);
			this.crossover();
			this.mutation();
			eval_result = this.evaluation(false);
		}
	};
	
	
	// Init population
	public void createInitialPopulation() throws Exception{
		// Creationf Binary Chromosomes
		if(class_type.getName().contains("BinaryChromosome")) {
		
			for(int i = 0; i < initial_population; ++i) {
				chromosomes.add((T) BinaryChromosome.newBinary(function, gene_precision));
			}
		
		// Creation of Real Chromosomes
		}else if(class_type.getName().contains("RealChromosome")) {
			for(int i = 0; i < initial_population; ++i) {
				//chromosomes.add((T) BinaryChromosome.newBinary(evaluation, gene_precision));
			}
		}
		
	}
	
	// Evaluate Population
	private double[] evaluation(boolean elitism) {
		int size = chromosomes.size();
		double[] eval_results = new double[size];
		for(int i = 0; i < size; ++i) {
			eval_results[i] = function.evaluate(((Chromosome)chromosomes.get(i)).getFenotypes());
		}
		
		
		DoubleStream map1 = Arrays.stream(eval_results);
		double max = map1.max().getAsDouble();
		DoubleStream map2 = Arrays.stream(eval_results).map((e) -> max - e);
		double total_sum = map2.sum();
		DoubleStream map3 = Arrays.stream(eval_results).map((e) -> (max - e) / total_sum);
		return map3.toArray();
	}
	
	private void selection(SelectionType type, double[] evaluations) {
		ArrayList<T> result = new ArrayList<>(initial_population);
		System.out.println(Arrays.toString(evaluations));
		switch(type){
			case ROULETTE:
				
				for(int i = 1; i < evaluations.length; ++i) {
					evaluations[i] += evaluations[i - 1];
				}
				
				ArrayList<T> generation = new ArrayList<>();
				for(int i = 0; i < initial_population; ++i) {
					double roulette = Math.random();
					int throul = 0;
					boolean end = false;
					
					// Buscamos el siguiente elemento en el que supera el valor de la ruleta
					while(!end) {
						end = roulette <= evaluations[throul++];
					}
					
					// Nos quedamos con el anterior.
					generation.add(chromosomes.get(throul - 1));
				}
				
			break;
		}
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
