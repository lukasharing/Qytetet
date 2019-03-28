package p2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.event.ListSelectionEvent;

import org.math.plot.utils.Array;

import model.Chromosome;
import model.CrossType;
import model.Function;
import model.MutationType;
import model.Pair;

public class CitiesChromosome extends Chromosome<Integer> {
	private int randomRange(int a, int b) { return a + (int)((b - a + 1) * Math.random()); };
	
	static int[][][] PERMUTATION = new int[][][]{
		new int[][] {},// 0 Permutation
		new int[][] { new int[] {0} }, // 1 Permutation
		new int[][] { // 2 Permutation
			new int[] {0, 1},
			new int[] {1, 0},
		},
		new int[][] { // 3 Permutation
			new int[] {0, 1, 2},
			new int[] {0, 2, 1},
			new int[] {1, 0, 2},
			new int[] {1, 2, 0},
			new int[] {2, 0, 1},
			new int[] {2, 1, 0}
		}
	};
	
	public CitiesChromosome(Function f) {
		super(f, 0.0);
		
		int number_arguments = f.getTotalArguments()-1;
		
		//Añadimos madrid al comienzo del cromosoma
		genes.add(25);
		//Añadimos el resto de ciudades al cromosoma
		for (int i = 0; i < 25; i++) { genes.add(i); }
		//Añadimos madrid al final del cromosoma
		genes.add(26);
		genes.add(25);
		
		//Añadimos madrid al final
		Collections.shuffle(genes.subList(1, genes.size() - 1));
	};

	@SuppressWarnings("unchecked")
	public CitiesChromosome(Function f, ArrayList<Integer> cloning_genes) {
		super(f, 0.0);
		this.genes = (ArrayList<Integer>) cloning_genes.clone();
	};
	
	// Bijective Function that given the id of the interval and (binary chain =
	// number)
	// returns a point in the value.
	private double getFenotype(Integer n) {
		return (double)genes.get(n);
	};

	public double[] getFenotypes() {
		double[] result = new double[genes.size()];
		for (int i = 0; i < genes.size(); ++i) {
			result[i] = getFenotype(i);
		}
		return result;
	};
	
	public void setFenotypes(double[] rs) {
		for (int i = 0; i < genes.size(); ++i) {
			this.genes.set(i, Integer.valueOf((int)rs[i]));
		}
	};

	// -----------------------------------------------
	// - Mutations
	public void mutate(MutationType mutation, double prob) {
		if(Math.random() < prob) {
			switch(mutation) {
				case INSERTION:
					
					// Algoritmos
					// 1. Elegimos posición aleatoria para elegirlo como desplazante  (1 - 26)
					int moving_pointer = randomRange(1, 26);
					// 2. Elegimos segunda posición aleatoria para desplazar.
					int move_pointer = randomRange(1, 26);
					
					int min_p = Math.min(moving_pointer, move_pointer);
					int max_p = Math.max(move_pointer, move_pointer);
					
					// 3. Desplazamos todo el subarray hacia la derecha (1 posición)
					List<Integer> left = genes.subList(0, min_p);
					List<Integer> shift = genes.subList(min_p, max_p);
					Integer fx = genes.get(max_p);
					List<Integer> right = genes.subList(max_p + 1, genes.size());
					
					
					List<Integer> result = new ArrayList<>();
					result.addAll(left);
					// 4. Insertamos en ese lugar, el elemento que teniamos.
					result.add(fx);
					result.addAll(shift);
					result.addAll(right);
					
					this.genes = new ArrayList<Integer>(result);
				break;
				
				case SWAP:
	
					// Intercambiamos dos posiciones aleatorias  (1 - 27)
					int swap_1 = randomRange(1, 26);
					int swap_2 = randomRange(1, 26);
					Collections.swap(this.genes, swap_1, swap_2);
					
				break;
				
				case INVERSION:
					
					// (1 - 26)
					// Invierte un subarray del vector
					int invert_1 = randomRange(1, 26);
					int invert_2 = randomRange(1, 26);
					
					int min = Math.min(invert_1, invert_2);
					int max = Math.max(invert_1, invert_2);
					
					List<Integer> part0 = new ArrayList<>();
					part0.addAll(genes.subList(0, min));
					List<Integer> reverse = genes.subList(min, max);
					Collections.reverse(reverse);
					part0.addAll(reverse);
					part0.addAll(genes.subList(max, genes.size()));
					
					this.genes = new ArrayList<>(part0);
					
				break;
				
				case HEURISTIC:
					
					int[] markers = new int [3];
					for(int i=0; i<3; i++){
						int num = randomRange(1, 26);
						if(!Arrays.asList(markers).contains(num)){
							markers[i] = num;
						}
					}
					
					int perm = 3; //(int)(1 + Math.random() * 2);
					int[][] all = PERMUTATION[perm];
					
					double[] cp = this.getFenotypes();
					double min_ev = ((FunctionCities)func).evaluate(cp);
					// Look at all permutations and find the best
					for(int i = 1; i < all.length; ++i) {
						double[] perm_gene = this.getFenotypes();
						for(int j = 0; j < perm; ++j) {
							perm_gene[markers[j]] = this.getFenotype(markers[all[i][j]]);
						}
						
						// See who has minimum distance
						double ev_ax = ((FunctionCities)func).evaluate(perm_gene);
						if(ev_ax < min_ev) {
							min_ev = ev_ax;
							cp = perm_gene;
						}
					}
					// Swap values
					this.setFenotypes(cp);
					
				break;
				
				case SELF_METHOD_1:
					
					// Miramos si haciendo swap a la derecha o a la izquierda, se mejora la evaluación, si es así, cambiamos.
	
					// 1. Cogemos 1 elemento aleatorio ( 2 - 26 ) para evitar coger madrid
					int p = randomRange(2, 25);
					double ev_p = ((FunctionCities)func).evaluate(this.getFenotypes());
					
					double[] cp0_gene = this.getFenotypes();
					double rs = cp0_gene[p]; // Helper
					cp0_gene[p] = cp0_gene[p - 1];
					cp0_gene[p - 1] = rs;
					double ev_cp0 = ((FunctionCities)func).evaluate(cp0_gene);
					
					double[] cp1_gene = this.getFenotypes();
					cp0_gene[p] = cp0_gene[p + 1];
					cp0_gene[p + 1] = rs;
					double ev_cp1 = ((FunctionCities)func).evaluate(cp1_gene);
	
					// SI el de la izquierda es menor distancia, cogemos
					if(ev_cp0 < ev_p) {
						ev_p = ev_cp0;
						this.setFenotypes(cp0_gene);
					}
					
					// SI el de la derecha es menor distancia, cogemos
					if(ev_cp1 < ev_p) {
						ev_p = ev_cp1;
						this.setFenotypes(cp1_gene);
					}
					
				break;
			}
		}
	};
	
	protected void cross(@SuppressWarnings("rawtypes") Chromosome chr1, CrossType type) {

		switch(type) {
			case PARTIALLY_MAPPED: // PMX
				
			break;
		}
		
	};
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Chromosome clone() {
		return new CitiesChromosome(this.func, this.genes);
	};
	
	
	public static CitiesChromosome newInstance(Function f) {
		return new CitiesChromosome(f);
	};
	
	public String toString() {
		String result = "\t\n Chromosome: \n";
		result += genes.stream().map(g -> parseCity(g) + "("+ g +")").collect(Collectors.joining("->"));
		result += "\nEvaluation: " + func.evaluate(this.getFenotypes()) + "\n";
		return result;
	}
	
	public static String parseCity(int n){
		return (n >= Provinces.CITIES.length ? "Error" : Provinces.CITIES[n].toString());
	}
}

