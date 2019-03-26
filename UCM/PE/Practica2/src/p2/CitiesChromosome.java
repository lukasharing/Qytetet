package p2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.math.plot.utils.Array;

import model.Chromosome;
import model.CrossType;
import model.Function;
import model.MutationType;
import model.Pair;

public class CitiesChromosome extends Chromosome<Integer> {
	
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
		genes.add(27);
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

	// -----------------------------------------------
	// - Mutations
	public void mutate(MutationType mutation, double prob) {
		System.out.println("Mutation");
		switch(mutation) {
			case INSERTION:
				
				// Algoritmos
				// 1. Elegimos posición aleatoria para elegirlo como desplazante
				int moving_pointer = (int) (1 + Math.random() * 28);
				// 2. Elegimos segunda posición aleatoria para desplazar.
				int move_pointer = (int) (1 + Math.random() * moving_pointer);
				// 3. Desplazamos todo el subarray hacia la derecha (1 posición)
				List<Integer> left = genes.subList(0, move_pointer);
				List<Integer> shift = genes.subList(move_pointer, moving_pointer);
				Integer fx = genes.get(moving_pointer);
				List<Integer> right = genes.subList(moving_pointer + 1, genes.size());
				
				
				List<Integer> result = new ArrayList<>();
				result.addAll(left);
				// 4. Insertamos en ese lugar, el elemento que teniamos.
				result.add(fx);
				result.addAll(shift);
				result.addAll(right);
				
				this.genes = new ArrayList<Integer>(result);
			break;
			
			case SWAP:

				// Intercambiamos dos posiciones aleatorias
				int swap_1 = (int) (1 + Math.random() * 28);
				int swap_2 = (int) (1 + Math.random() * 28);
				Collections.swap(this.genes, swap_1, swap_2);
				
			break;
			
			case INVERSION:

				int invert_1 = (int) (1 + Math.random() * 28);
				int invert_2 = (int) (1 + Math.random() * 28);
				
				int min = Math.min(invert_1, invert_2);
				int max = Math.max(invert_1, invert_2);
				
				List<Integer> part0 = new ArrayList<>();
				part0.addAll(genes.subList(0, min));
				List<Integer> reverse = genes.subList(min, max);
				Collections.reverse(reverse);
				part0.addAll(reverse);
				part0.addAll(genes.subList(max, genes.size()));
				
				System.out.println(part0.toString());
				
				this.genes = new ArrayList<>(part0);
				
			break;
			
			case HEURISTIC:
				
				int[] markers = new int [] {
					(int) (1 + Math.random() * 28),
				    (int) (1 + Math.random() * 28),
				    (int) (1 + Math.random() * 28)
				};
				
				int perm = 3; //(int)(1 + Math.random() * 2);
				int[][] all = PERMUTATION[perm];
				
				ArrayList<Integer> cp = this.genes;
				int min_ev = ((FunctionCities)func).evaluate(cp.stream().mapToInt(Integer::intValue).toArray());
				// Look at all permutations and find the best
				for(int i = 1; i < all.length; ++i) {
					Integer[] perm_gene = this.genes.toArray(new Integer[0]);
					for(int j = 0; j < perm; ++j) {
						perm_gene[markers[j]] = this.genes.get(markers[all[i][j]]);
					}
					
					// See who has minimum distance
					List<Integer> ax = Arrays.asList(perm_gene);
					int ev_ax = ((FunctionCities)func).evaluate(ax.stream().mapToInt(Integer::intValue).toArray());
					if(ev_ax < min_ev) {
						min_ev = ev_ax;
						cp = new ArrayList<Integer>(ax);
					}
				}
				// Swap values
				this.genes = cp;
				
			break;
			
			case SELF_METHOD_1:
				
				// Miramos si haciendo swap a la derecha o a la izquierda, se mejora la evaluación, si es así, cambiamos.

				// 1. Cogemos 1 elemento aleatorio ( 2 - 26 ) para evitar coger madrid
				int p = (int) (2 + Math.random() * 25);
				int ev_p = ((FunctionCities)func).evaluate(this.genes.stream().mapToInt(Integer::intValue).toArray());
				
				List<Integer> cp0_gene = Arrays.asList(this.genes.toArray(new Integer[0]));
				Collections.swap(cp0_gene, p, p - 1);
				int ev_cp0 = ((FunctionCities)func).evaluate(cp0_gene.stream().mapToInt(Integer::intValue).toArray());
				
				List<Integer> cp1_gene = Arrays.asList(this.genes.toArray(new Integer[0]));
				Collections.swap(cp1_gene, p, p + 1);
				int ev_cp1 = ((FunctionCities)func).evaluate(cp1_gene.stream().mapToInt(Integer::intValue).toArray());

				// SI el de la izquierda es menor distancia, cogemos
				if(ev_cp0 < ev_p) {
					ev_p = ev_cp0;
					this.genes = new ArrayList<>(cp0_gene);
				}
				
				// SI el de la derecha es menor distancia, cogemos
				if(ev_cp1 < ev_p) {
					ev_p = ev_cp1;
					this.genes = new ArrayList<>(cp1_gene);
				}
				
			break;
		}
		
	};
	
	protected void cross(@SuppressWarnings("rawtypes") Chromosome chr1, CrossType type) {

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
		
		result = genes.stream().map(g -> parseCity(g)).collect(Collectors.joining("->"));
		
		result += "\nEvaluation: " + func.evaluate(this.getFenotypes()) + "\n";
		return result;
	}
	
	static String[] CITIES = {"Albacete", "Alicante", "Almería", "Ávila", "Badajoz", "Barcelona", "Bilbao", "Burgos", "Cáceres", "Cádiz", "Castellón", "Ciudad Real", "Córdoba", "A Coruña", "Cuenca", "Gerona", "Granada", "Guadalajara", "Huelva", "Huesca", "Jaén", "León", "Lérida", "Logroño", "Lugo", "Madrid", "Málaga"};
	public static String parseCity(int n){
		return (n >= CITIES.length ? "Error" : CITIES[n]);
	}
}

