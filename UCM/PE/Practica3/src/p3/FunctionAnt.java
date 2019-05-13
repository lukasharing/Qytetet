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
	private static int MAXTIMENOEAT = 20;
	
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
		do {
			execute(ant, tree, null);
		}while(steps > 0 && ant.timeNoEat < MAXTIMENOEAT);
		steps = 400;
		
		System.out.println(ant.timeNoEat >= MAXTIMENOEAT);
		return ant.timeNoEat >= MAXTIMENOEAT ? 0 : (ant.eaten() - tree.getMaxHeight(tree));
	};
	
	public ArrayList<Pair<Integer, Integer>> getPath(Chromosome chr){
		
		ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
		Ant ant = last = new Ant(0, 0);
		
		result.add(ant.coords());
		do {
			execute(ant, (AntTree)chr.genes.get(0), result);
		}while(steps > 0);
		steps = 400;
		
		return result;
	}
	
	public void execute(Ant ant, AntTree tree, ArrayList<Pair<Integer, Integer>> res) {
		if(steps <= 0 || ant.timeNoEat >= MAXTIMENOEAT) return;
		
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
