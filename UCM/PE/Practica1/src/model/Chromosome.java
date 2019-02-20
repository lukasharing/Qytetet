package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Chromosome<T> {
	
	protected List<T> genes;
	protected Function func;
	
	public List<T> getGenes(){ return genes; };
	
	public Chromosome(Function f) {
		func = f;
		genes = new ArrayList<T>(f.getTotalArguments());
	};
	
	//public mutate(MutationType mutation, g)
	public Double[] getFenotypes(){ return null; };
}
