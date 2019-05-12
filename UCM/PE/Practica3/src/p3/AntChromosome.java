package p3;

import java.util.ArrayList;

import model.Chromosome;
import model.CrossType;
import model.Function;
import model.MutationType;

public class AntChromosome extends Chromosome<AntTree> {
	
	final int MAX_DEPTH = 2;
	
	public AntChromosome(Function f, double p) {
		super(f, 0.0);
		// Initialize
		AntTree new_tree = new AntTree(null, 0, AntMovement.random_node());
		this.genes.add(new_tree);
		create_tree(new_tree);
	};

	// Create Tree
	private void create_tree(AntTree parent) {
		for(int i = 0; i < parent.type.num_args; ++i) {
			// Create ant movement depending on the depth.
			AntMovement mov = parent.depth < (MAX_DEPTH - 1) ? AntMovement.random_movement() : AntMovement.random_final();
			
			// Create Subtree
			AntTree subtree = new AntTree(parent, parent.depth + 1, mov);
			
			// Add Subtree
			parent.addChild(subtree);
			
			// Generate sub-subtree
			create_tree(subtree);
		}
	};
	
	// -----------------------------------------------
	// - Cromosome
	@Override
	public void cross(Chromosome chr1, CrossType type) {
		switch(type) {
			case SUBTREE:
				
				AntTree node1 = this.genes.get(0);
				while(node1.type.num_args > 0) {
					node1 = node1.getChild((int)Math.floor(node1.totalChildren() * Math.random()));
				}

				AntTree node2 = (AntTree) chr1.genes.get(0);
				while(node2.type.num_args > 0) {
					node2 = node2.getChild((int)Math.floor(node2.totalChildren() * Math.random()));
				}
				
				node1 = node1.parent;
				node2 = node2.parent;
				
				AntMovement temp_type = node1.type;
				ArrayList<AntTree> temp_children = node1.getChildren();
				
				node1.type = node2.type;
				node1.setChildren(node2.getChildren());
				
				node2.type = temp_type;
				node2.setChildren(temp_children);
				
			break;
		}
	}
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
			case SIMPLE_FUNCTION:
				
				while(node.type.num_args > 0) {
					node = node.getChild((int)Math.floor(node.totalChildren() * Math.random()));
				}
				// Find Movement with the same number of arguments
				AntMovement mov = null;
				do { mov = AntMovement.random_node(); }while(node.parent.type.num_args != mov.num_args);
				node.parent.type = mov;
				
			break;
			case SUBTREE:

				while(node.type.num_args > 0) {
					node = node.getChild((int)Math.floor(node.totalChildren() * Math.random()));
				}
				
				node.parent.type = AntMovement.random_node();
				node.parent.emptyChildren();
				create_tree(node.parent);
				
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
