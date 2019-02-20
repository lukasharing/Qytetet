package model;

import java.util.List;

public class GeneticAlgorithm<T> {
	private Class<T> typeArgumentClass;
	
	private List<T> chromosomes; //Population
	private int maxGenerations;
	private int maxPopulationSize;
	private T bestChromosome;
	private int posBestChromosome;
	private double crossoverProbability;
	private double mutationProbability; 
	private double precision;
	boolean GATerminated;
	private Function evaluationFunction;
	
	
	public GeneticAlgorithm(int maxGenerations, double crossoverProbability, double mutationProbability, double precision, Function evaluationFunction){
		this.maxGenerations = maxGenerations;
		this.crossoverProbability = crossoverProbability;
		this.mutationProbability = mutationProbability;
		this.precision = precision;
		this.evaluationFunction = evaluationFunction;
		GATerminated = false;
	};
	
	public void run() {
		int currentGeneration = 0;
		
		try{
			this.createInitialPopulation(this.maxPopulationSize, this.evaluationFunction);
		}catch(Exception ex) {
			System.exit(0); 
		}
		
		this.evaluation(evaluationFunction, chromosomes);
		
		while(!GATerminated && (currentGeneration < maxGenerations)) {
			currentGeneration++;
			this.selection();
			this.crossover(this.crossoverProbability);
			this.mutation(this.mutationProbability);
			this.evaluation(evaluationFunction, chromosomes);
		}
	};
	
	public void createInitialPopulation(int maxPopulationSize, Function evaluationFunction) throws Exception{
			
		Class[] cArg = new Class[1];
		cArg[0] = Function.class;
		
		for(int i = 0; i < maxPopulationSize; ++i) {
			chromosomes.add(
				typeArgumentClass.getDeclaredConstructor(cArg).newInstance(evaluationFunction)
			);
		}
	}
	
	private void evaluation(Function func, List<T> chromosomes) {
		for(T chromosome : chromosomes) {
			Double value = evaluationFunction.evaluate(
				((Chromosome)chromosome).getFenotypes()
			);
		}
	}
	
	private boolean selection() {
		return true;
	}
	
	private boolean crossover(double crossoverProbability) {
		return true;
	}
	
	private boolean mutation(double mutationProbability) {
		return true;
	}
	
}
