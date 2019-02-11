package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Chromosome<T> {
	
	protected List<T> genes;
	protected Function func;
	
	public Chromosome(Function f) {
		func = f;
		genes = new ArrayList<T>(f.getTotalArguments());
	};
	
}
