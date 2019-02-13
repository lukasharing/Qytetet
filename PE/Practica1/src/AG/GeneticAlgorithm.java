package AG;

import java.util.List;

import model.Chromosome;
import model.Function;

public class GeneticAlgorithm<T> {
	private List<Chromosome<T>> chromosomes; //Population
	private int maxGenerations;
	private int maxPopulationSize;
	private Chromosome<T> bestChromosome;
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
	}
	
	public void run() {
		int currentGeneration = 0;
		
		if(!this.createInitialPopulation(this.maxPopulationSize, this.evaluationFunction)) { 
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
	}
	
	public boolean createInitialPopulation(int maxPopulationSize, Function evaluationFunction) {
		
		for(int i=0; i<maxPopulationSize; i++) {
			//chromosomes.add(Añadir cromosomas a la lista)); FALTA POR ACABAR
		}
		
		return true;
	}
	
	private boolean evaluation(Function func, List<Chromosome<T>> chromosomes) {
		
		//Locate the best chromosome and save it into bestChromosome
		return true;
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
