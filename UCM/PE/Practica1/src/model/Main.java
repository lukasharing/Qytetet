package model;

import javax.swing.UnsupportedLookAndFeelException;

import gui.Panel;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		System.out.println("Algoritmo Genético.");
		//Function1 f1 = new Function1(FunctionType.MAXIMIZE);
		new Panel();
		//GeneticAlgorithm<BinaryChromosome> ga = new GeneticAlgorithm<BinaryChromosome>(BinaryChromosome.class, 100, 100, 0.3, 0.05, 0.0001, f1);
		//ga.test();
	}
}
