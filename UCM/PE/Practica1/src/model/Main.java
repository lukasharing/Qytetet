package model;

public class Main {

	public static void main(String[] args) {
		System.out.println("Algoritmo Genético.");
		Function1 f1 = new Function1(FunctionType.MAXIMIZE);
		GeneticAlgorithm<BinaryChromosome> ga = new GeneticAlgorithm<BinaryChromosome>(BinaryChromosome.class, 100, 100, 0.3, 0.05, 0.0001, f1);
		ga.test();
	}
}
