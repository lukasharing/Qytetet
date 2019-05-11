package p3;

import model.Chromosome;
import model.Function;

public class AntChromosome extends Chromosome<AntTree> {
	
	final int MAX_DEPTH = 3;
	
	public AntChromosome(Function f, double p) {
		super(f, 0.0);
		// Initialize
		AntTree new_tree = new AntTree(null, 0, AntMovement.random_node());
		this.genes.add(new_tree);
		create_tree(new_tree, 0);
	};

	// Create Tree
	private void create_tree(AntTree parent, int current_depth) {
		for(int i = 0; i < parent.type.num_args; ++i) {
			// Create ant movement depending on the depth.
			AntMovement mov = current_depth < MAX_DEPTH ? AntMovement.random_movement() : AntMovement.random_final();
			
			// Create Subtree
			AntTree subtree = new AntTree(parent, current_depth + 1, mov);
			
			// Add Subtree
			parent.addChild(subtree);
			
			// Generate sub-subtree
			create_tree(subtree, current_depth + 1);
		}
	};
	
	// Clone AntTree
	public AntChromosome(Function f, AntTree tree) {
		super(f, 0.0);
		// Clone
		this.genes.add(tree.clone(null));
	};
	
	@SuppressWarnings("unchecked")
	public Chromosome clone() {
		return new AntChromosome(this.func, this.genes.get(0));
	};
	
	public static AntChromosome newInstance(Function f, int branching) {
		return new AntChromosome(f, branching);
	};
	
	@Override
	public String toString() {
		return this.genes.get(0).toString();
	}

	public void setTree(AntTree root) { this.genes.set(0, root); }
	
}
