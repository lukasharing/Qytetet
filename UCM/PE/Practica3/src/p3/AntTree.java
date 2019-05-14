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
	private AntMovement type;
	
	public AntTree(AntTree _parent, int _depth, AntMovement _type){
		this.setParent(_parent);
		this.setDepth(_depth);
		this.setType(_type);
		this.children = new ArrayList<>();
	};
	
	public ArrayList<AntTree> getChildren() { return children; };
	public void setChildren(ArrayList<AntTree> _children) { children = _children; };
	public void addChild(AntTree child){ children.add(child); };
	public void setChild(int i, AntTree child) { children.set(i, child); }
	public AntTree getChild(int i) { return children.get(i); };
	public int totalChildren() { return children.size(); };
	public void emptyChildren() { children.clear(); };
	public AntTree getParent() {return parent;}
	public void setParent(AntTree parent) {this.parent = parent;};
	
	
	public int getMaxHeight(AntTree node) {
		int max = node.depth;
		for(AntTree subnode : node.children) {
			max = Math.max(max, getMaxHeight(subnode));
		}
		return max;
	};
	
	public int getTotalNodes() {
		int count = 1;
		for(AntTree subnode : children) {
			count += subnode.getTotalNodes();
		}
		return count;
	};
	
	public AntTree clone(AntTree parent) {
		AntTree clone = new AntTree(parent, this.getDepth(), this.getType());
		for(AntTree subtree : this.children) {
			clone.addChild(subtree.clone(clone));
		}
		
		return clone;
	}
	
	
	public void updateChildrenDepth() {		
		for (AntTree child : children) {
			child.setDepth(depth+1);
			child.updateChildrenDepth();
		}
	}
	
	@Override
	public String toString() {
		String result = "";
		
		for(int i = 0; i < getDepth(); ++i) result += "-";
		result += ">" + this.getType() + "\n";
		
		for(AntTree subtree : children) {
			result += subtree.toString();
		}
		return result;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public AntMovement getType() {
		return type;
	}

	public void setType(AntMovement type) {
		this.type = type;
	}

	
}
