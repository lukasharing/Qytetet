package p2;

import java.util.ArrayList;
import java.util.Collections;

import model.Chromosome;
import model.CrossType;
import model.Function;
import model.MutationType;
import model.Pair;

public class CitiesChromosome extends Chromosome<Integer> {
	
	public CitiesChromosome(Function f) {
		super(f, 0.0);
		
		int number_arguments = f.getTotalArguments()-1;

		// Random Values.
		
		genes.add(0, 25); //Añadimos madrid al comienzo del cromosoma
		
		for (int i=0; i < number_arguments ; i++) {
			if( i != 25 ){
				genes.add(i); //Añadimos el resto de ciudades al cromosoma
			}
		}
		genes.add(25); //Añadimos madrid al final
		Collections.shuffle(genes.subList(1, genes.size()-1));
	};

	@SuppressWarnings("unchecked")
	public CitiesChromosome(Function f, ArrayList<Integer> cloning_genes) {
		super(f, 0.0);
		this.genes = (ArrayList<Integer>) cloning_genes.clone();
	};

	// Returns number of bits needed for a given interval
	@SuppressWarnings("unused")
	private Integer getGeneSize(Pair<Double, Double> interval) {
		return (int) Math.ceil(Math.log(1 + ((Double) interval.second - (Double) interval.first) / prec) / Math.log(2));
	};

	// Bijective Function that given the id of the interval and (binary chain =
	// number)
	// returns a point in the value.
	private Integer getFenotype(Integer n) {
		return genes.get(n);
	};

	public double[] getFenotypes() {
		double[] result = new double[genes.size()];
		for (int i = 0; i < genes.size(); ++i) {
			result[i] = getFenotype(i);
		}
		return result;
	};

	private int randomGen() {
		return (int) (Math.random() * 27);
	};

	// -----------------------------------------------
	// - Mutations
	public void mutate(MutationType mutation, double prob) {

	};

	public static CitiesChromosome newInstance(Function f) {
		return new CitiesChromosome(f);
	};

	@SuppressWarnings("unchecked")
	protected void cross(@SuppressWarnings("rawtypes") Chromosome chr1, CrossType type) {

	};
	
	@SuppressWarnings("unchecked")
	protected void crossTemp(@SuppressWarnings("rawtypes") Chromosome chr1, int n) {

	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Chromosome clone() {
		return new CitiesChromosome(this.func, this.genes);
	};
	
	
	public String toString() {
		String result = "\t\n Chromosome: \n";
		for (Integer gene : genes) {
			result += parseCity(gene) + "->";			
		}
		
		//Elimina la flechita de la ultima ciudad
		result = result.substring(0, result.length()-2);
		
		result += "\nEvaluation: " + func.evaluate(this.getFenotypes()) + "\n";
		return result;
	}
	
	public static String parseCity(int n){
		switch(n) {
		case 0:	
			return "Albacete";
		case 1:	
			return "Alicante";
		case 2:	
			return "Almería";
		case 3:	
			return "Ávila";
		case 4:	
			return "Badajoz";
		case 5:	
			return "Barcelona";
		case 6:	
			return "Bilbao";
		case 7:	
			return "Burgos";
		case 8:	
			return "Cáceres";
		case 9:	
			return "Cádiz";
		case 10:	
			return "Castellón";
		case 11:	
			return "Ciudad Real";
		case 12:	
			return "Córdoba";
		case 13:	
			return "A Coruña";
		case 14:	
			return "Cuenca";
		case 15:	
			return "Gerona";
		case 16:	
			return "Granada";
		case 17:	
			return "Guadalajara";
		case 18:	
			return "Huelva";
		case 19:	
			return "Huesca";
		case 20:	
			return "Jaén";
		case 21:	
			return "León";
		case 22:	
			return "Lérida";
		case 23:	
			return "Logroño";
		case 24:	
			return "Lugo";
		case 25:	
			return "Madrid";
		case 26:	
			return "Málaga";
		default:	
			return "Unknown city (" + n + ")";
			
		}
	}
}

