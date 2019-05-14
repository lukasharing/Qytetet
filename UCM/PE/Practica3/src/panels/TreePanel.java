package panels;

import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.Configuration.Location;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import p3.AntTree;

public class TreePanel {

	static NodeExtentProvider<Text> nodeExtentProvider = new NodeExtentProvider<Text>() {
        @Override
        public double getWidth(Text tn) {
            return 75;
        }

        @Override
        public double getHeight(Text tn) {
          return 20;
        }
    };
	    
	
	
	public static TextTreePane createTreePanel(AntTree tree) {
		
		Text rootText = new Text(tree.getType().toString(), 60, 30);
		DefaultTreeForTreeLayout<Text> treeLayaout = new DefaultTreeForTreeLayout<>(rootText);
		generateVisualizationTree(treeLayaout, rootText, tree);
	
	
	   
	    DefaultConfiguration<Text> configuration = new DefaultConfiguration<>(40, 30,Location.Top);
	    TreeLayout<Text> treeLayout = new TreeLayout<Text>(treeLayaout, nodeExtentProvider, configuration);
	    return new TextTreePane(treeLayout);
	}
	


	static void generateVisualizationTree(DefaultTreeForTreeLayout<Text> tree, Text parent, AntTree subtree) {

		for(AntTree subsubtree : subtree.getChildren()) {
			Text childText = new Text(subsubtree.getType().toString(), 60, 30);
			tree.addChild(parent, childText);
			generateVisualizationTree(tree, childText, subsubtree);
		}

	}
	
	
}
