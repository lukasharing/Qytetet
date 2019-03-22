package model;

import java.util.ArrayList;
import java.util.Collections;

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
			
			switch(gene) {
				case 0:	
					result += "Albacete->";
					break;
				case 1:	
					result += "Alicante->";
					break;
				case 2:	
					result += "Almería->";
					break;
				case 3:	
					result += "Ávila->";
					break;
				case 4:	
					result += "Badajoz->";
					break;
				case 5:	
					result += "Barcelona->";
					break;
				case 6:	
					result += "Bilbao->";
					break;
				case 7:	
					result += "Burgos->";
					break;
				case 8:	
					result += "Cáceres->";
					break;
				case 9:	
					result += "Cádiz->";
					break;
				case 10:	
					result += "Castellón->";
					break;
				case 11:	
					result += "Ciudad Real->";
					break;
				case 12:	
					result += "Córdoba->";
					break;
				case 13:	
					result += "A Coruña->";
					break;
				case 14:	
					result += "Cuenca->";
					break;
				case 15:	
					result += "Gerona->";
					break;
				case 16:	
					result += "Granada->";
					break;
				case 17:	
					result += "Guadalajara->";
					break;
				case 18:	
					result += "Huelva->";
					break;
				case 19:	
					result += "Huesca->";
					break;
				case 20:	
					result += "Jaén->";
					break;
				case 21:	
					result += "León->";
					break;
				case 22:	
					result += "Lérida->";
					break;
				case 23:	
					result += "Logroño->";
					break;
				case 24:	
					result += "Lugo->";
					break;
				case 25:	
					result += "Madrid->";
					break;
				case 26:	
					result += "Málaga->";
					break;
				default:	
					result += "Unknown city (" + gene + ")";
					break;
					
			}
			
		}
		
		//Elimina la flechita de la ultima ciudad
		result = result.substring(0, result.length()-2);
		
		result += "\nEvaluation: " + func.evaluate(this.getFenotypes()) + "\n";
		return result;
	}
}
