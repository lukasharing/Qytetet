package p3;

import java.util.ArrayList;
import java.util.Stack;

public class AntTree {
	// Parent
	private ArrayList<AntTree> children;
	// Parent
	private AntTree parent;
	// Profundidad
	private int depth;
	// Node type tree
	AntMovement type;
	
	public AntTree(AntTree _parent, int _depth, AntMovement _type){
		this.parent = _parent;
		this.depth = _depth;
		this.type = _type;
		this.children = new ArrayList<>();
	};
	
	public void addChild(AntTree child){ children.add(child); };
	public AntTree getChild(int i) { return children.get(i); };
	
	
	public AntTree clone(AntTree parent) {
		AntTree clone = new AntTree(parent, this.depth, this.type);
		for(AntTree subtree : this.children) {
			clone.addChild(subtree.clone(clone));
		}
		
		return clone;
	}
	
	@Override
	public String toString() {
		String result = "";
		
		for(int i = 0; i < depth; ++i) result += "-";
		result += ">" + this.type + "\n";
		
		for(AntTree subtree : children) {
			result += subtree.toString();
		}
		return result;
	};
}
