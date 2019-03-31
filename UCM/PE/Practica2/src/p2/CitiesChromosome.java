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
			int ttl = this.genes.size();
			switch(mutation) {
				case INSERTION:
					
					// Algoritmos
					// 1. Elegimos posición aleatoria para elegirlo como desplazante  (1 - 26)
					int moving_pointer = randomRange(1, ttl - 2);
					// 2. Elegimos segunda posición aleatoria para desplazar.
					int move_pointer = randomRange(1, ttl - 2);
					
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
					int invert_1 = randomRange(1, ttl - 2);
					int invert_2 = randomRange(1, ttl - 2);
					
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
					
					ArrayList<Integer> markers = new ArrayList<Integer>(perm);
					double[] values = new double[perm];
					
					// Inicializamos los markadores y los valores de estos
					for(int i = 0; i < perm; ++i) {
						int rand = 0;
						do {
							rand = randomRange(1, ttl - 2);
						}while(markers.contains(rand)); // Generate Safe Number
						markers.add(rand);
						values[i] = this.getFenotype(markers.get(i));
					}
					
					double[] cp = this.getFenotypes();
					double min_ev = ((FunctionCities)func).evaluate(cp);
					// Look at all permutations and find the best
					for(int i = 1; i < all.length; ++i) {
						double[] perm_gene = this.getFenotypes();
						
						for(int j = 0; j < perm; ++j) {
							perm_gene[markers.get(j)] = values[all[i][j]];
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
		
			case ORDERED_VARIANT_OXPP:

				
				ArrayList<Integer> ov_subparent1 = new ArrayList<Integer>(this.genes.subList(1, func.getTotalArguments()-1));
				ArrayList<Integer> ov_subparent2 = new ArrayList<Integer>(chr1.genes.subList(1, func.getTotalArguments()-1));
				
				List<Integer> ov_result1 = new ArrayList<Integer>(func.getTotalArguments());
				List<Integer> ov_result2 = new ArrayList<Integer>(func.getTotalArguments());
				
				for(int i=0; i<ov_subparent1.size(); i++){
					ov_result1.add(-1);
					ov_result2.add(-1);
				}
				
				int ncross = randomRange(0 , ov_subparent1.size()-1);
				
				for(int i = 0; i<ncross; i++){
					int toswap = randomRange(0 , ov_subparent1.size()-1);
					if(ov_result1.get(toswap)==-1){
						ov_result1.set(toswap, ov_subparent2.get(toswap));
						ov_result2.set(toswap, ov_subparent1.get(toswap));
					}
				}
				
				for(int i = 0; i<ov_subparent1.size();i++){
					if(ov_result1.get(i) == -1){
						boolean finished=false;
						int j = i;
						while (!finished){
							if(!ov_result1.contains(ov_subparent1.get(j))){
								ov_result1.set(i, ov_subparent1.get(j));
								finished = true;
							}
							j++;
							if(ov_subparent1.size()==j){
								j=0;
							}
						}
					}
				}
				
				for(int i = 0; i<ov_subparent2.size();i++){
					if(ov_result2.get(i) == -1){
						boolean finished=false;
						int j = i;
						while (!finished){
							if(!ov_result2.contains(ov_subparent2.get(j))){
								ov_result2.set(i, ov_subparent2.get(j));
								finished = true;
							}
							j++;
							if(ov_subparent1.size()==j){
								j=0;
							}
						}
					}
				}
				
				ov_result1.add(0,25);
				ov_result2.add(0,25);
				ov_result1.add(25);
				ov_result2.add(25);
				
				this.genes = (ArrayList<Integer>) ov_result1;
				chr1.genes = (ArrayList<Integer>) ov_result2;
				
			break;
		
			case ORDERED_VARIANT_OXOP:
				
				ArrayList<Integer> ovo_subparent1 = new ArrayList<Integer>(this.genes.subList(1, func.getTotalArguments()-1));
				ArrayList<Integer> ovo_subparent2 = new ArrayList<Integer>(chr1.genes.subList(1, func.getTotalArguments()-1));
				
				List<Integer> ovo_result1 = new ArrayList<Integer>(func.getTotalArguments());
				List<Integer> ovo_result2 = new ArrayList<Integer>(func.getTotalArguments());
				
				for(int i=0; i<ovo_subparent1.size(); i++){
					ovo_result1.add(-1);
					ovo_result2.add(-1);
				}
				
				int nSelectedPositions = randomRange(1 , 10/*ovo_subparent1.size()-1*/);
				List<Integer> selectedPositions = new ArrayList<Integer>(nSelectedPositions);
				
				int z=0;
				while(z<nSelectedPositions){
					int pos = randomRange(0 , ovo_subparent1.size()-1);
					if(!selectedPositions.contains(pos)){
						selectedPositions.add(pos);
						z++;
					}
				}
				
				for(int i=0; i<selectedPositions.size(); i++){
					ovo_result1.set(ovo_subparent2.indexOf(ovo_subparent1.get(selectedPositions.get(i))), -2);
					ovo_result2.set(ovo_subparent1.indexOf(ovo_subparent2.get(selectedPositions.get(i))), -2);
				}
				
				int j=0;
				for(int i=0; i<ovo_subparent1.size(); i++){
					if(ovo_result1.get(i)==-1){
						ovo_result1.set(i, ovo_subparent2.get(i));
					}else if(ovo_result1.get(i)==-2){
						ovo_result1.set(i, ovo_subparent1.get(selectedPositions.get(j)));
						j++;
					}
				}
				
				j=0;
				for(int i=0; i<ovo_subparent2.size(); i++){
					if(ovo_result2.get(i)==-1){
						ovo_result2.set(i, ovo_subparent1.get(i));
					}else if(ovo_result2.get(i)==-2){
						ovo_result2.set(i, ovo_subparent2.get(selectedPositions.get(j)));
						j++;
					}
				}
				
				
				ovo_result1.add(0,25);
				ovo_result2.add(0,25);
				ovo_result1.add(25);
				ovo_result2.add(25);
				
				this.genes = (ArrayList<Integer>) ovo_result1;
				chr1.genes = (ArrayList<Integer>) ovo_result2;
				
				
			break;
		
			
			case CICLES:
				
				Boolean[] visited = new Boolean[ttl];
				Arrays.fill(visited, Boolean.FALSE);
				
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
			
			case EDGE_RECOMBINATION:
			
				ArrayList<Integer> erx_subparent1 = new ArrayList<Integer>(this.genes.subList(1, func.getTotalArguments()-1));
				ArrayList<Integer> erx_subparent2 = new ArrayList<Integer>(chr1.genes.subList(1, func.getTotalArguments()-1));
				
				List<Integer> erx_result1 = new ArrayList<Integer>(func.getTotalArguments());
				List<Integer> erx_result2 = new ArrayList<Integer>(func.getTotalArguments());
				
				
				List<List<Integer>> scoreTable = new ArrayList<List<Integer>>();
				
				for(int i=0; i<this.genes.size(); i++){
					List<Integer> temp = new ArrayList<Integer>();
					
					try {
						int n = erx_subparent1.indexOf(i);
						if(n!=-1 && !temp.contains(erx_subparent1.get(n-1)))
							temp.add(erx_subparent1.get(n-1));
					} catch (Exception e) {	}
					
					try {
						int n = erx_subparent1.indexOf(i);
						if(n!=-1 && !temp.contains(erx_subparent1.get(n+1)))
							temp.add(erx_subparent1.get(n+1));
					} catch (Exception e) {	}
					
					try {
						int n = erx_subparent2.indexOf(i);
						if(n!=-1 && !temp.contains(erx_subparent2.get(n-1)))
							temp.add(erx_subparent2.get(n-1));
					} catch (Exception e) {	}
					
					try {
						int n = erx_subparent2.indexOf(i);
						if(n!=-1 && !temp.contains(erx_subparent2.get(n+1)))
							temp.add(erx_subparent2.get(n+1));
					} catch (Exception e) {	}
					
					scoreTable.add(temp);
				}
				
				List<List<Integer>> scoreTable2 = new ArrayList<List<Integer>>(scoreTable);
				
				
				for (int i = 0; i<erx_subparent1.size(); i++){
					if(i==0){
						int r = erx_subparent1.get(i);
						erx_result1.add(r);
						for(int k=0; k<scoreTable.size(); k++){
							scoreTable.get(k).remove(new Integer(r));
							
						}
						
					} else {
						
						int minPath = -1;
						List<Integer> minsAt = new ArrayList<Integer>();
						
						for(int m=0; m < scoreTable.get(erx_result1.get(i-1)).size(); m++){
							if(minPath!=-1){
								int newMinPath = scoreTable.get(scoreTable.get(erx_result1.get(i-1)).get(m)).size();
								if(newMinPath<=minPath){
									//minsAt = new ArrayList<Integer>();
									minPath = newMinPath;
									minsAt.add(scoreTable.get(erx_result1.get(i-1)).get(m));
								}
							} else {
								minPath = scoreTable.get(scoreTable.get(erx_result1.get(i-1)).get(m)).size();
								if(!erx_result1.contains(scoreTable.get(erx_result1.get(i-1)).get(m)))
									minsAt.add(scoreTable.get(erx_result1.get(i-1)).get(m));
							}
						
						}
												
						//Si en la lista de mejores hay mas de uno, cojo uno al azar
						int shortest = randomRange(0,minsAt.size()-1);
						
						if(!minsAt.isEmpty()){
							for(int k=0; k<scoreTable.size(); k++){								
								scoreTable.get(k).remove(minsAt.get(shortest));
							}
							
							erx_result1.add(minsAt.get(shortest));
						} else {
							List<Integer> temp_list = new ArrayList<Integer>(func.getTotalArguments()-1);
							for (int it=0; it<func.getTotalArguments()-1; it++){
								if(it!=25)
									temp_list.add(it);
							}
							
							temp_list.removeAll(erx_result1);
							int num = randomRange(0,temp_list.size()-1);
							for(int k=0; k<scoreTable.size(); k++){
									scoreTable.get(k).remove(new Integer(num));
								
							}
							
							erx_result1.add(temp_list.get(num));
						}
						
					}
				}
				
				
				for (int i = 0; i<erx_subparent2.size(); i++){
					if(i==0){
						int r = erx_subparent2.get(i);
						erx_result2.add(r);
						for(int k=0; k<scoreTable2.size(); k++){
							scoreTable2.get(k).remove(new Integer(r));
							
						}
						
					} else {
						
						int minPath = -1;
						List<Integer> minsAt = new ArrayList<Integer>();
						
						for(int m=0; m < scoreTable2.get(erx_result2.get(i-1)).size(); m++){
							if(minPath!=-1){
								int newMinPath = scoreTable2.get(scoreTable2.get(erx_result2.get(i-1)).get(m)).size();
								if(newMinPath<=minPath){
									//minsAt = new ArrayList<Integer>();
									minPath = newMinPath;
									minsAt.add(scoreTable2.get(erx_result2.get(i-1)).get(m));
								}
							} else {
								minPath = scoreTable2.get(scoreTable2.get(erx_result2.get(i-1)).get(m)).size();
								if(!erx_result2.contains(scoreTable2.get(erx_result2.get(i-1)).get(m)))
									minsAt.add(scoreTable2.get(erx_result2.get(i-1)).get(m));
							}
						
						}
												
						//Si en la lista de mejores hay mas de uno, cojo uno al azar
						int shortest = randomRange(0,minsAt.size()-1);
						
						if(!minsAt.isEmpty()){
							for(int k=0; k<scoreTable2.size(); k++){								
								scoreTable2.get(k).remove(minsAt.get(shortest));
							}
							
							erx_result2.add(minsAt.get(shortest));
						} else {
							List<Integer> temp_list = new ArrayList<Integer>(func.getTotalArguments()-1);
							for (int it=0; it<func.getTotalArguments()-1; it++){
								if(it!=25)
									temp_list.add(it);
							}
							
							temp_list.removeAll(erx_result2);
							int num = randomRange(0,temp_list.size()-1);
							for(int k=0; k<scoreTable2.size(); k++){
									scoreTable2.get(k).remove(new Integer(num));
								
							}
							
							erx_result2.add(temp_list.get(num));
						}
						
					}
				}
				
				
				erx_result1.add(0,25);
				erx_result2.add(0,25);
				erx_result1.add(25);
				erx_result2.add(25);
				
				this.genes = (ArrayList<Integer>) erx_result1;
				chr1.genes = (ArrayList<Integer>) erx_result2;
				
				
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

