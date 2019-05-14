package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.abego.treelayout.Configuration.Location;
import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.math.plot.Plot2DPanel;

import model.CrossType;
import model.Function;
import model.FunctionType;
import model.GeneticAlgorithm;
import model.MutationType;
import model.SelectionType;
import model.Pair;
import p3.Ant;
import p3.AntChromosome;
import p3.AntMovement;
import p3.AntTree;
import p3.FunctionAnt;
import panels.Text;
import panels.TextTreePane;
import panels.TreePanel;

public class Panel3 extends JFrame {

	private static final long serialVersionUID = 2569879142816556337L;

	Plot2DPanel plot;
	JTabbedPane tabbedPane;
	JPanel tp0;
	JPanel tp1, tp2/*, tp3, tp4, tp5*/;

	private JTextField size_population;
	private JTextField num_generations;
	private JTextField crossover_perc;
	private JTextField mutation_perc;

	// Algoritmos de selecci�n
	private List<SelectionType> selection_type = Arrays.asList(
		SelectionType.ROULETTE,
		SelectionType.DETE_TOURNAMENT,
		SelectionType.PRB_TOURNAMENT,
		SelectionType.RANKING
	);

	// Algoritmos de cruce
	private List<CrossType> cross_type = Arrays.asList(
		CrossType.SUBTREE
	);

	// Algoritmos de mutaci�n
	private List<MutationType> mutation_type = Arrays.asList(
		MutationType.SIMPLE_TERMINAL,
		MutationType.SIMPLE_FUNCTION,
		MutationType.SUBTREE/*,
		MutationType.PERMUTATION*/
	);

	private JComboBox<String> selection_sel;
	private JComboBox<String> mutation_sel;
	private JComboBox<String> cross_sel;
	private JButton start;
	private JButton restart;
	//private JCheckBox elitism;
	private JSpinner elitism_amount;
	private JCheckBox contractivity;
	private GeneticAlgorithm<?> ga = null;
	Function fun = new FunctionAnt(1, FunctionType.MAXIMIZE);

	public Panel3() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException, IOException {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setResizable(false);

		setTitle("Practica 3");
		this.setMinimumSize(new Dimension(1300, 700));

		// Components
		tabbedPane = new JTabbedPane();


		plot = new Plot2DPanel();
		plot.addLegend("SOUTH");
		plot.setBorder(BorderFactory.createLineBorder(new Color(141, 179, 214)));
		plot.setMinimumSize(new Dimension(200, 200));

		tp0 = new JPanel();
		tp0.add(plot);
		tp0.setLayout(new GridLayout(1, 1));

		/*
		AntChromosome chr = new AntChromosome(fun, 0);

		AntTree root = new AntTree(null, 0, AntMovement.ISFOOD);
		AntTree cr1 = new AntTree(root, 1, AntMovement.MOVE);
		AntTree cr2 = new AntTree(root, 1, AntMovement.PROGN3);

		//cr2
		AntTree cr12 = new AntTree(cr2, 2, AntMovement.TURN_LEFT);
		AntTree cr22 = new AntTree(cr2, 2, AntMovement.ISFOOD);
		AntTree cr32 = new AntTree(cr2, 2, AntMovement.PROGN3);

		// cr22
		AntTree cr122 = new AntTree(cr22, 3, AntMovement.PROGN3);
		AntTree cr222 = new AntTree(cr22, 3, AntMovement.TURN_RIGHT);

		// cr122
		AntTree cr1122 = new AntTree(cr122, 4, AntMovement.MOVE);
		AntTree cr2122 = new AntTree(cr122, 4, AntMovement.MOVE);
		AntTree cr3122 = new AntTree(cr122, 4, AntMovement.MOVE);

		// cr32
		AntTree cr132 = new AntTree(cr32, 3, AntMovement.TURN_RIGHT);
		AntTree cr232 = new AntTree(cr32, 3, AntMovement.ISFOOD);
		AntTree cr332 = new AntTree(cr32, 3, AntMovement.MOVE);

		// cr232
		AntTree cr1232 = new AntTree(cr232, 4, AntMovement.MOVE);
		AntTree cr2232 = new AntTree(cr232, 4, AntMovement.TURN_LEFT);

		root.addChild(cr1);
		root.addChild(cr2);

		cr2.addChild(cr12);
		cr2.addChild(cr22);
		cr2.addChild(cr32);

		cr22.addChild(cr122);
		cr22.addChild(cr222);

		cr122.addChild(cr1122);
		cr122.addChild(cr2122);
		cr122.addChild(cr3122);

		cr32.addChild(cr132);
		cr32.addChild(cr232);
		cr32.addChild(cr332);

		cr232.addChild(cr1232);
		cr232.addChild(cr2232);

		chr.setTree(root);

		System.out.println(chr);
		//Pair<Integer, Integer> coords = ant.coords();

		*/

		tp1 = new JPanel() {
			private static final long serialVersionUID = 1L;
			private int time = 0;

			@Override
		    public void paintComponent(Graphics g){
				Graphics2D ctx = (Graphics2D)g;


				final int SIZE = 16;
				final int DSX = +200;
				final int DSY = +50;


				g.clearRect(DSX, DSY, SIZE * 32, SIZE * 32);

				for(int j = 0; j < 32; ++j) {
					for(int i = 0; i < 32; ++i) {
						int dx = i * SIZE + DSX;
						int dy = j * SIZE + DSY;
						if(Ant.MAP_ANT[j][i] == 1) {
							ctx.fillRect(dx, dy, SIZE, SIZE);
						}

					}
				}

				if(ga != null) {
					FunctionAnt fut = (FunctionAnt)fun;
					ArrayList<Pair<Integer, Integer>> res = fut.getPath(/*chr*/ga.getBestAbs_chr());


					for(int j = 0; j < res.size(); ++j) {
						int x = res.get(j).first;
                        int y = res.get(j).second;

						int dx = x * SIZE + DSX;
						int dy = y * SIZE + DSY;
						ctx.setColor(Ant.MAP_ANT[y][x] == 1 ? Color.ORANGE : Color.CYAN);
						ctx.fillRect(dx, dy, SIZE, SIZE);
					}


					fut.last.draw(ctx);
				}
				// Draw Grid
				for(int j = 0; j < 32; ++j) {
					for(int i = 0; i < 32; ++i) {
						int dx = i * SIZE + DSX;
						int dy = j * SIZE + DSY;
						ctx.setColor(Color.BLACK);
						ctx.drawRect(dx, dy, SIZE, SIZE);
					}
				}

		    };
		};
        tp1.setLayout(new GridLayout(1, 1));




        tp2 = new JPanel();
        /*
        tp3 = new JPanel();
        tp4 = new JPanel();
        tp5 = new JPanel();*/



		tabbedPane.addTab("Grafica", tp0);
        tabbedPane.addTab("Mapa", tp1);
        tabbedPane.addTab("Mejor Arbol", tp2);/*
        tabbedPane.addTab("Nodos Parent 2", tp3);
        tabbedPane.addTab("Nodos Child 1", tp4);
        tabbedPane.addTab("Nodos Child 2", tp5);*/

		add(tabbedPane, BorderLayout.CENTER);


		JPanel barraizq = new JPanel();
		barraizq.setLayout(new BoxLayout(barraizq, BoxLayout.Y_AXIS));

		/* Titulo */
		JLabel titulo = new JLabel("PARAMETROS:");
		JPanel ptitle = new JPanel(new GridLayout(1, 1));
		titulo.setFont(titulo.getFont().deriveFont(16.0f));
		ptitle.add(titulo);
		barraizq.add(ptitle);


		/* Size Population */
		this.size_population = new JTextField("100", 12);
		JPanel p1 = new JPanel(new GridLayout(2, 1));
		p1.add(new JLabel("Tamanio poblacion:"));
		p1.add(size_population);
		barraizq.add(p1);

		/* Number Generations */
		this.num_generations = new JTextField("100", 12);
		JPanel p2 = new JPanel(new GridLayout(2, 1));
		p2.add(new JLabel("Numero generaciones:"));
		p2.add(num_generations);
		barraizq.add(p2);

		/* Crossing Percentage */
		this.crossover_perc = new JTextField("0.6", 12);
		JPanel p3 = new JPanel(new GridLayout(2, 1));
		p3.add(new JLabel("Probabilidad de cruce:"));
		p3.add(crossover_perc);
		barraizq.add(p3);

		/* Mutation Percentage */
		this.mutation_perc = new JTextField("0.05", 12);
		JPanel p4 = new JPanel(new GridLayout(2, 1));
		p4.add(new JLabel("Probabilidad de mutacion:"));
		p4.add(mutation_perc);
		barraizq.add(p4);

		/* Selection Selection */
		this.selection_sel = new JComboBox<>(selection_type.stream().map(n -> n.toString()).toArray(String[]::new));
		JPanel p11 = new JPanel(new GridLayout(2, 1));
		p11.add(new JLabel("Seleccion:"));
		p11.add(selection_sel);
		barraizq.add(p11);

		/* Mutation Selection */
		this.mutation_sel = new JComboBox<>(mutation_type.stream().map(n -> n.toString()).toArray(String[]::new));
		JPanel p12 = new JPanel(new GridLayout(2, 1));
		p12.add(new JLabel("Mutacion:"));
		p12.add(mutation_sel);
		barraizq.add(p12);

		/* Cross Selection */
		this.cross_sel = new JComboBox<>(cross_type.stream().map(n -> n.toString()).toArray(String[]::new));
		JPanel p13 = new JPanel(new GridLayout(2, 1));
		p13.add(new JLabel("Cruce:"));
		p13.add(cross_sel);
		barraizq.add(p13);

		/* Number Elitism */
		this.elitism_amount = new JSpinner();
		elitism_amount.setValue(0);
		JPanel p9 = new JPanel(new GridLayout(2, 1));
		p9.add(new JLabel("Numero Elitismo:"));
		p9.add(elitism_amount);
		barraizq.add(p9);

		/* Contractivity */
		this.contractivity = new JCheckBox("Contractividad");
		JPanel p14 = new JPanel(new GridLayout(1, 1));
		p14.add(contractivity);
		barraizq.add(p14);


		/* Buttons */
		JPanel p10 = new JPanel(new GridLayout(2, 1));
		start = new JButton("Iniciar");
		restart = new JButton("Restablecer");
		p10.add(start);
		p10.add(restart);
		barraizq.add(p10);

		barraizq.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		/* Footer */
		barraizq.setBorder(BorderFactory.createEmptyBorder(8, 15, 0, 15));
		add(barraizq, BorderLayout.LINE_START);

		JLabel footer = new JLabel("Realizado por Lukas Haring y Raul Torrijos", SwingConstants.CENTER);
		footer.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(footer, BorderLayout.PAGE_END);
		setVisible(true);

		// OUTPUT
		JPanel barradcha = new JPanel();

		barradcha.setLayout(new BorderLayout());
		barradcha.setBorder(BorderFactory.createEmptyBorder(8, 24, 0, 15));

		JPanel barradchactr = new JPanel();
		barradcha.add(barradchactr);
		barradchactr.setLayout(new BoxLayout(barradchactr, BoxLayout.Y_AXIS));
		JLabel titulodcha = new JLabel("RESULTADOS:       ");
		titulodcha.setFont(titulodcha.getFont().deriveFont(16.0f));

		restartResults(barradchactr, titulodcha);

		JPanel barradchaftr = new JPanel();

		barradcha.add(barradchaftr, BorderLayout.PAGE_END);
		barradchaftr.setLayout(new BoxLayout(barradchaftr, BoxLayout.Y_AXIS));
		barradchaftr.add(new JLabel("Mejor evaluacion:"), BorderLayout.PAGE_END);
		JLabel best_ev = new JLabel(" ");
		barradchaftr.add(best_ev);

		add(barradcha, BorderLayout.LINE_END);

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				restartPlot();
				tp1.repaint();
				tp1.revalidate();
				tp2.repaint();
				tp2.revalidate();
				/* Paneles para visualizar mutaciones y cruces
				 * tp3.repaint();
				tp3.revalidate();
				tp4.repaint();
				tp4.revalidate();
				tp5.repaint();
				tp5.revalidate();*/
	
				restartResults(barradchactr, titulodcha);
	
	
				int elitism_am = ((Integer) elitism_amount.getValue());
	
				int num_gen = Integer.parseInt(num_generations.getText());
	
				model.SelectionType type_sel = selection_type.get(selection_sel.getSelectedIndex());
				model.CrossType type_cross = cross_type.get(cross_sel.getSelectedIndex());
				model.MutationType type_mut = mutation_type.get(mutation_sel.getSelectedIndex());
	
				/*
				AntChromosome c0 = new AntChromosome(fun, 0);
				AntChromosome c1 = new AntChromosome(fun, 0);
	

		        tp2.removeAll();
		        tp2.add(TreePanel.createTreePanel(c0.genes.get(0)));
				tp3.removeAll();
		        tp3.add(TreePanel.createTreePanel(c1.genes.get(0)));
		        

				c0.cross(c1, CrossType.SUBTREE);
				
		        tp4.removeAll();
		        tp4.add(TreePanel.createTreePanel(c0.genes.get(0)));
				tp5.removeAll();
		        tp5.add(TreePanel.createTreePanel(c1.genes.get(0)));
				*/
				
				//c0.cross(c1, CrossType.SUBTREE);
				//System.out.println("Crossing ---------------1");
				//System.out.println(c0.toString());
				//System.out.println("Crossing ---------------2");
				//System.out.println(c1.toString());
				
				
				ga = new GeneticAlgorithm<AntChromosome>(
					AntChromosome.class,
					Integer.parseInt(size_population.getText()),
					num_gen,
					Double.parseDouble(crossover_perc.getText()),
					Double.parseDouble(mutation_perc.getText()),
					0.0, elitism_am, type_sel, type_cross, type_mut,
					fun, contractivity.isSelected()
				);
	
				List<double[]> best_distances = ga.run();
	
	
	
				double[] generations = new double[num_gen];
				for (int i = 0; i < num_gen; ++i) {
					generations[i] = i;
				}
	
				addPlotLines(generations, best_distances);
				
				fun.evaluate(ga.getBestAbs_chr());
				FunctionAnt funt = (FunctionAnt) fun;
				best_ev.setText(funt.last.eaten() +" comidos");
	
	
				AntTree root = (AntTree) ga.getBestAbs_chr().genes.get(0);			
		        tp2.removeAll();
		        tp2.add(TreePanel.createTreePanel(root));
			}});

			restart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ga = null;
					restartPlot();
					tp1.repaint();
					tp1.revalidate();
					best_ev.setText(" ");
					restartResults(barradchactr, titulodcha);
	
					plot.repaint();
					plot.revalidate();
				}
			});

	}

	void restartResults(JPanel p, JLabel l) {
		p.removeAll();
		p.add(l);
		p.add(new JLabel(" "));
		p.revalidate();
		p.repaint();

	}

	void restartPlot() {
		tp0.remove(plot);
		plot = new Plot2DPanel();
		plot.addLegend("SOUTH");
		tp0.add(plot, BorderLayout.CENTER);
	}

	void addPlotLines(double[] generations, List<double[]> best_chromosomes) {
		plot.addLinePlot("Mejor absoluto", generations, best_chromosomes.get(0));
		plot.addLinePlot("Mejor de la generacion", generations, best_chromosomes.get(1));
		plot.addLinePlot("Media de la generacion", generations, best_chromosomes.get(2));
	}
}
