/*
 * [The "BSD license"]
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the abego Software GmbH nor the names of its 
 *    contributors may be used to endorse or promote products derived from this 
 *    software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package panels;

import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.abego.treelayout.Configuration.Location;
import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

public class SwingDemo {

	public static void main(String[] args) {
		// get the sample tree
		
		Text root=new Text("root", 40, 30);
	      
        Text t1=new Text("t1", 30, 30);
        Text t2=new Text("t2", 30, 30);
        Text t3=new Text("t3", 30, 30);
        Text t4=new Text("t4", 30, 30);

        DefaultTreeForTreeLayout<Text> tree = new DefaultTreeForTreeLayout<>(root);
        tree.addChild(root, t1);
        tree.addChild(root, t2);
        tree.addChild(t2, t3);
        tree.addChild(t3, t4);
		
        
        NodeExtentProvider<Text> nodeExtentProvider = new NodeExtentProvider<Text>() {
            @Override
            public double getWidth(Text tn) {
                return tn.width;
            }

            @Override
            public double getHeight(Text tn) {
              return tn.height;
            }
        };
        DefaultConfiguration<Text> configuration = new DefaultConfiguration<>(40, 300,Location.Top);
        TreeLayout<Text> treeLayout = new TreeLayout<Text>(tree, nodeExtentProvider, configuration);
        TextTreePane panel = new TextTreePane(treeLayout);
        
        
        JFrame frame = new JFrame("test");
        
        frame.add(panel);
        frame.setVisible(true);
        
        
//        JDialog dialog = new JDialog();
//        Container contentPane = dialog.getContentPane();
//        ((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(
//                10, 10, 10, 10));
//        contentPane.add(panel);
//        dialog.pack();
//        dialog.setLocationRelativeTo(null);
//        dialog.setVisible(true);
//        
	}
	
	
}