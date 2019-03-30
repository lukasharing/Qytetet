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
		int min = Math.min(number_arguments, genes.get(0));
		
		for (int i = 0; i < min; i++) { genes.add(i); }
		//Añadimos madrid al final del cromosoma
		for (int i = min + 1; i < number_arguments; i++) { genes.add(i); }
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
			this.genes.set(i, (int)rs[i]);
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
					

					final int perm = 3; //(int)(1 + Math.random() * 2);
					int[][] all = PERMUTATION[perm];
					
					int[] markers = new int [perm];
					double[] values = new double [perm];
					
					// Inicializamos los markadores y los valores de estos
					for(int i = 0; i < perm; ++i) {
						markers[i] = randomRange(1, 26);
						values[i] = this.getFenotype(markers[i]);
					}
					
					double[] cp = this.getFenotypes();
					double min_ev = ((FunctionCities)func).evaluate(cp);
					// Look at all permutations and find the best
					for(int i = 1; i < all.length; ++i) {
						double[] perm_gene = this.getFenotypes();
						
						
						for(int j = 0; j < perm; ++j) {
							perm_gene[markers[j]] = values[all[i][j]];
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
					
					double[] cp0_gene = this.getFenotypes();
					double ev_p = ((FunctionCities)func).evaluate(cp0_gene);
					
					// Swap center and left
					double rs = cp0_gene[p]; // Helper
					cp0_gene[p] = cp0_gene[p - 1];
					cp0_gene[p - 1] = rs;
					double ev_cp0 = ((FunctionCities)func).evaluate(cp0_gene);

					// Swap center and right
					double[] cp1_gene = this.getFenotypes();
					cp1_gene[p] = cp1_gene[p + 1];
					cp1_gene[p + 1] = rs;
					double ev_cp1 = ((FunctionCities)func).evaluate(cp1_gene);
	
					// SI el de la izquierda es menor distancia, cogemos
					if(ev_cp0 < ev_p) {
						ev_p = ev_cp0;
						this.setFenotypes(cp0_gene);
					}

					// SI el de la derecha es menor distancia, cogemos
					if(ev_cp1 < ev_p) {
						this.setFenotypes(cp1_gene);
					}
					
				break;
				
				case SELF_METHOD_2:
					// TODO
				break;
			}
		}
	};
	
	public void cross(Chromosome<Integer> chr1, CrossType type) {
		// Total elements
		int ttl = this.genes.size();
		
		switch(type) {
			case PARTIALLY_MAPPED: // PMX
				
				int p0 = randomRange(1, ttl - 2);
				int p1 = randomRange(1, ttl - 2);
				
				int min = Math.min(p0, p1);
				int max = Math.max(p0, p1);
				
				// Swap both subintervals
				List<Integer> sub0 = this.genes.subList(min, max);
				List<Integer> sub1 = chr1.genes.subList(min, max);
				
				int total = this.genes.size();
				ArrayList<Integer> child0 = new ArrayList<>(total);
				ArrayList<Integer> child1 = new ArrayList<>(total);
				
				// Vemos 0 - (min - 1)
				for(int i = 0; i < min; ++i) {
					// Vemos si está dentro del subconjunto.
					pmx_temp(child0, sub1, sub0, i, this);
					pmx_temp(child1, sub0, sub1, i, chr1);
				}
				child0.addAll(sub1); // Swap 0 -> 1
				child1.addAll(sub0); // Swap 1 -> 0
				for(int i = max; i < total; ++i) {
					pmx_temp(child0, sub1, sub0, i, this);
					pmx_temp(child1, sub0, sub1, i, chr1);
				}
				
				this.genes = child0;
				chr1.genes = child1;
				
			break;
			
			case CICLES:
				
				Boolean[] visited = new Boolean[ttl];
				Arrays.fill(visited, Boolean.FALSE);
				
				ArrayList<Integer> child2 = new ArrayList<>(ttl);
				ArrayList<Integer> child3 = new ArrayList<>(ttl);
				
				ArrayList<List<Integer>> cycles = new ArrayList<>();
				
				// Add all cycles into arraylist
				for(int i = 1; i < ttl - 1; ++i) {
					if(!visited[i].booleanValue()) {
						visited[i] = Boolean.TRUE;
						
						// Iterate over cycles
						int current = i;
						List<Integer> cycle = new ArrayList<Integer>();
						cycle.add(current);
						while(this.genes.indexOf(chr1.genes.get(current)) != i) {
							current = this.genes.indexOf(chr1.genes.get(current));
							visited[current] = Boolean.TRUE;
							cycle.add(current);
						}
						cycles.add(cycle);
					}
				}
				
				// Iterate Over Cycles and swap cycles in even numbers
				for(int i = 1; i < cycles.size(); i += 2) {
					List<Integer> cycle = cycles.get(i);
					// Swap if odd
					for(Integer c : cycle) {
						int tmp = this.genes.get(c);
						this.genes.set(c, ((CitiesChromosome) chr1).genes.get(c));
						chr1.genes.set(c, tmp);
					}
				}
				
			break;
			
			case ORDERED: 
				
				int cut1 = randomRange(2,26);
				int cut2 = randomRange(2,26);
				while(cut1 == cut2) {
					cut2 = randomRange(2,26);
				}
				if(cut1 > cut2) {
					int temp = cut1;
					cut1 = cut2;
					cut2 = temp;
				}
				ArrayList<Integer> sublist1 = new ArrayList<Integer>(this.genes.subList(cut1, cut2));
				ArrayList<Integer> sublist2 = new ArrayList<Integer>(chr1.genes.subList(cut1, cut2));
				
				List<Integer> result1 = new ArrayList<Integer>(func.getTotalArguments());
				List<Integer> result2 = new ArrayList<Integer>(func.getTotalArguments());
				
				for(int i = 1; i<cut1; i++){
					result1.add(-1);
					result2.add(-1);
				}
				result1.addAll(sublist2);
				result2.addAll(sublist1);
				for(int i = cut2; i<func.getTotalArguments()-1; i++){
					result1.add(-1);
					result2.add(-1);
				}				
				
				ArrayList<Integer> subparent1 = new ArrayList<Integer>(this.genes.subList(1, func.getTotalArguments()-1));
				ArrayList<Integer> subparent2 = new ArrayList<Integer>(chr1.genes.subList(1, func.getTotalArguments()-1));
				
				for(int i = 0; i<subparent1.size();i++){
					if(result1.get(i) == -1){
						boolean finished=false;
						int j = i;
						while (!finished){
							if(!result1.contains(subparent1.get(j))){
								result1.set(i, subparent1.get(j));
								finished = true;
							}
							j++;
							if(subparent1.size()==j){
								j=0;
							}
						}
					}
				}
				
				for(int i = 0; i<subparent2.size();i++){
					if(result2.get(i) == -1){
						boolean finished=false;
						int j = i;
						while (!finished){
							if(!result2.contains(subparent2.get(j))){
								result2.set(i, subparent2.get(j));
								finished = true;
							}
							j++;
							if(subparent1.size()==j){
								j=0;
							}
						}
					}
				}
				
				result1.add(0,25);
				result2.add(0,25);
				result1.add(25);
				result2.add(25);
				
				this.genes = (ArrayList<Integer>) result1;
				chr1.genes = (ArrayList<Integer>) result2;
				
			break;
		
			
			case ORDINAL_CODIFICATION:
				
				List<Integer> ord_0 = ord_get_temp(this);
				List<Integer> ord_1 = ord_get_temp(chr1);
				
				// Swap subintervals
				int swap_i = 4;//randomRange(1, );
				List<Integer> sub_00 = ord_0.subList(0, swap_i);
				List<Integer> sub_01 = ord_0.subList(swap_i, ttl - 2);
				
				List<Integer> sub_10 = ord_1.subList(0, swap_i);
				List<Integer> sub_11 = ord_1.subList(swap_i, ttl - 2);
				
				ArrayList<Integer> result_0 = new ArrayList<Integer>(ttl - 2);
				ArrayList<Integer> result_1 = new ArrayList<Integer>(ttl - 2);
				
				// Swap
				result_0.addAll(sub_00);
				result_0.addAll(sub_11);

				// Swap
				result_1.addAll(sub_10);
				result_1.addAll(sub_01);
				
				// Set to the genes
				ord_set_temp(result_0, this);
				ord_set_temp(result_1, chr1);
				
				
			break;
		}
		
	};
	
	void pmx_temp(ArrayList<Integer> child, List<Integer> sub0, List<Integer> sub1, int i, Chromosome chr) {
		// Vemos si está dentro del subconjunto.
		int gn = (int)chr.genes.get(i);
		int id = sub0.indexOf(gn);
		if(id >= 0) {
			
			// Escalamos "recursivamente" 
			// Ya que supongamos que i = 6 y las sublistas
			// 6 1 2 3
			// 1 2 3 0, el escalado es 6 -> 1 -> 2 -> 3 -> 0
			while(sub0.indexOf(sub1.get(id)) >= 0){
				id = sub0.indexOf(sub1.get(id));
			}
			
			child.add(i, sub1.get(id));
		}else {
			child.add(gn);
		}
	}
	
	List<Integer> ord_get_temp(Chromosome chr) {
		
		int ttl = chr.genes.size();
		//Añadimos el resto de ciudades al cromosoma
		List<Integer> order = new ArrayList<Integer>(ttl - 2);
		
		for (int i = 0; i < (int)chr.genes.get(0); i++) { order.add(i); }
		for (int i = (int)chr.genes.get(0) + 1; i < ttl - 1; i++) { order.add(i); }
		
		List<Integer> codification = new ArrayList<Integer>(ttl);
		
		for(int i = 1; i < ttl - 1; ++i) {
			int idx = order.indexOf(chr.genes.get(i));
			order.remove(idx);
			codification.add(idx);
		}
		
		return codification;
	};
	
	void ord_set_temp(ArrayList<Integer> rs, Chromosome chr) {
		int ttl = chr.genes.size();
		List<Integer> order = new ArrayList<Integer>(ttl);
		for (int i = 0; i < (int)chr.genes.get(0); i++) { order.add(i); }
		for (int i = (int)chr.genes.get(0) + 1; i < ttl - 1; i++) { order.add(i); }
		
		for(int i = 0; i < rs.size(); ++i) {
			Integer id = rs.get(i);
			chr.genes.set(i + 1, order.get(id));
			order.remove(id.intValue());
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
		result += "Total cities: " + this.genes.size() + "\n"; 
		result += genes.stream().map(g -> parseCity(g) + "("+ g +")").collect(Collectors.joining("->"));
		result += "\nEvaluation: " + func.evaluate(this.getFenotypes()) + "\n";
		return result;
	}
	
	public static String parseCity(int n){
		return (n >= Provinces.CITIES.length ? "Error" : Provinces.CITIES[n].toString());
	}
}

