package model;

import AG.GeneticAlgorithm;

public class Main {

	public static void main(String[] args) {
		Function1 f1 = new Function1();
		//BinaryChromosome chromosome = new BinaryChromosome(f1);
		GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<Integer>(100, 0.3, 0.05, 0.0001, f1);
		
		ga.run();
		
	}

}
