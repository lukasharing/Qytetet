package p3;

import java.util.ArrayList;

import model.Chromosome;
import model.Function;
import model.FunctionType;
import model.Pair;
import p2.CitiesChromosome;

public class FunctionAnt extends Function{

	public static int MAX_STEPS = 500;
	private int steps = MAX_STEPS;
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
		AntTree tree = (AntTree)chromosome.genes.get(0);
		steps = MAX_STEPS;
		do {
			execute(ant, tree, null);
		}while(steps > 0);
		
		//control de bloating
		return ant.eaten() - tree.getTotalNodes() * 0.5;
	};
	
	public ArrayList<Pair<Integer, Integer>> getPath(Chromosome chr){
		
		ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
		Ant ant = last = new Ant(0, 0);
		
		result.add(ant.coords());
		
		steps = MAX_STEPS;
		do {
			execute(ant, (AntTree)chr.genes.get(0), result);
		}while(steps > 0);
		
		return result;
	}
	
	public void execute(Ant ant, AntTree tree, ArrayList<Pair<Integer, Integer>> res) {
		if(steps <= 0) return;
		
		switch(tree.getType()) {
			case ISFOOD:
				if(ant.sensor()) { // is food
					execute(ant, tree.getChild(0), res);
				}else {
					execute(ant, tree.getChild(1), res);
				}
			break;
			case PROGN2:
				execute(ant, tree.getChild(0), res);
				execute(ant, tree.getChild(1), res);
			break;
			case PROGN3:
				execute(ant, tree.getChild(0), res);
				execute(ant, tree.getChild(1), res);
				execute(ant, tree.getChild(2), res);
			break;
			default:
				--steps;
				ant.step(tree.getType());
				if(res != null && tree.getType().equals(AntMovement.MOVE)) {
					res.add(ant.coords());
				}
			break;
		}
			
		
		
	}
}
