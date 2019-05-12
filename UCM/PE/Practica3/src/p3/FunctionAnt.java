package p3;

import java.util.ArrayList;

import model.Chromosome;
import model.Function;
import model.FunctionType;
import model.Pair;
import p2.CitiesChromosome;

public class FunctionAnt extends Function{

	private int steps = 400;
	public Ant last = null;
	
	@SuppressWarnings("rawtypes")
	public FunctionAnt(int n, FunctionType type) {
		super(type);
		arguments_intervals = new ArrayList<Pair>();
		arguments_intervals.add(new Pair<Integer, Integer>(0, 0));
	}
	
	
	@Override
	public double evaluate(Chromosome chromosome) {
		Ant ant = last = new Ant(0, 0);
		do {
			execute(ant, (AntTree)chromosome.genes.get(0));
		}while(steps > 0);
		steps = 400;
		
		return ant.eaten();
	};

	
	public void execute(Ant ant, AntTree tree) {
		if(steps <= 0) return;
		
		switch(tree.type) {
			case ISFOOD:
				if(ant.sensor()) { // is food
					execute(ant, tree.getChild(0));
				}else {
					execute(ant, tree.getChild(1));
				}
			break;
			case PROGN2:
				execute(ant, tree.getChild(0));
				execute(ant, tree.getChild(1));
			break;
			case PROGN3:
				execute(ant, tree.getChild(0));
				execute(ant, tree.getChild(1));
				execute(ant, tree.getChild(2));
			break;
			default:
				--steps;
				ant.step(tree.type);
			break;
		}
			
		
		
	}
}
