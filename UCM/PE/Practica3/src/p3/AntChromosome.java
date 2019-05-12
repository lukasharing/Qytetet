package p3;

import model.Chromosome;
import model.Function;
import model.MutationType;

public class AntChromosome extends Chromosome<AntTree> {
	
	final int MAX_DEPTH = 4;
	
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

	// -----------------------------------------------
	// - Mutations
	public void mutate(MutationType mutation, double prob) {
		if(Math.random() > prob) return;

		AntTree node = this.genes.get(0);
		switch(mutation) {
			case SIMPLE_TERMINAL:
				
				while(node.type.num_args > 0) {
					node = node.getChild((int)Math.floor(node.totalChildren() * Math.random()));
				}
				
				// Random Final Node
				node.type = AntMovement.random_final();
				
			break;
			case SUBTREE:

				while(node.type.num_args > 0) {
					node = node.getChild((int)Math.floor(node.totalChildren() * Math.random()));
				}
				
				node.parent.type = AntMovement.random_node();
				node.parent.emptyChildren();
				create_tree(node.parent, node.parent.depth);
				
			break;
			case PERMUTATION:
				
				int idx = 0;
				while(node.type.num_args > 0) {
					idx = (int)Math.floor(node.totalChildren() * Math.random());
					node = node.getChild(idx);
				}
				
				// Choose random node
				int rnd = -1;
				do { rnd = (int)(node.parent.totalChildren() * Math.random()); }while(idx == rnd);
				
				// Swap
				AntTree temp = node.parent.getChild(idx);
				node.parent.setChild(idx, node.parent.getChild(rnd));
				node.parent.setChild(rnd, temp);
				
			break;
		}
	}
	
	
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
	
	public static AntChromosome newInstance(Function f) {
		return new AntChromosome(f, 0.0);
	};
	
	@Override
	public String toString() {
		return this.genes.get(0).toString();
	}

	public void setTree(AntTree root) { this.genes.set(0, root); }
	
}
