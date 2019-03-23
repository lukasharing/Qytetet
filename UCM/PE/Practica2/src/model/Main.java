package model;

import javax.swing.UnsupportedLookAndFeelException;

import gui.Panel;
import gui.Panel2;
import model.FunctionType;
public class Main {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		new Panel2();
		/*
		Function f;
		f = (Function) new FunctionCities(27, FunctionType.MINIMIZE);
		Chromosome c = new CitiesChromosome(f);
		System.out.println(c);
		*/
	}
}
