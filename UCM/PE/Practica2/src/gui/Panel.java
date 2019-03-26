package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import org.math.plot.*;

import model.CrossType;
import model.Function;
import model.FunctionType;
import model.GeneticAlgorithm;
import model.MutationType;
import model.SelectionType;
import p1.BinaryChromosome;
import p1.Function1;
import p1.Function2;
import p1.Function3;
import p1.Function4;
import p1.RealChromosome;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Panel extends JFrame {

	private static final long serialVersionUID = 2569879142816556337L;

	Plot2DPanel plot;

	private JComboBox<String> chrtype_sel;
	private String[] chrtype_sel_ops = { "Binario", "Real" };
	private JTextField size_population;
	private JTextField num_generations;
	private JTextField crossover_perc;
	private JTextField mutation_perc;
	private JTextField prec;
	private String[] function_sel_ops = { "Función 1", "Función 2", "Función 3", "Función 4" };
	private String[] selection_type = { "Ruleta", "Torneo Determinista", "Torneo Probabilístico" };
	private String[] mutation_type = { "Aleatoria (Binario)", "Uniforme (Reales)", "No uniforme (Reales)" };
	private String[] cross_type = { "Monopunto", "Multipunto", "Uniforme (Binario)" };
	private JComboBox<String> function_sel;
	private JComboBox<String> selection_sel;
	private JComboBox<String> mutation_sel;
	private JComboBox<String> cross_sel;
	private JSpinner func4_params;
	private JButton start;
	private JButton restart;
	//private JCheckBox elitism;
	private JSpinner elitism_amount;
	private GeneticAlgorithm<?> ga;

	public Panel() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setResizable(false);

		setTitle("Práctica 1");
		this.setMinimumSize(new Dimension(1300, 700));

		// Components
		plot = new Plot2DPanel();
		plot.addLegend("SOUTH");
		plot.setBorder(BorderFactory.createLineBorder(new Color(141, 179, 214)));
		add(plot, BorderLayout.CENTER);

		JPanel barraizq = new JPanel();
		barraizq.setLayout(new BoxLayout(barraizq, BoxLayout.Y_AXIS));

		/* Titulo */
		JLabel titulo = new JLabel("PARÁMETROS:");
		JPanel ptitle = new JPanel(new GridLayout(1, 1));
		titulo.setFont(titulo.getFont().deriveFont(16.0f));
		ptitle.add(titulo);
		barraizq.add(ptitle);

		/* Chromosome Type Selection */
		this.chrtype_sel = new JComboBox<>(chrtype_sel_ops);
		chrtype_sel.setEnabled(false);
		JPanel p0 = new JPanel(new GridLayout(2, 1));
		p0.add(new JLabel("Tipo de cromosoma:"));
		p0.add(chrtype_sel);
		barraizq.add(p0);

		/* Size Population */
		this.size_population = new JTextField("100", 12);
		JPanel p1 = new JPanel(new GridLayout(2, 1));
		p1.add(new JLabel("Tamaño población:"));
		p1.add(size_population);
		barraizq.add(p1);

		/* Number Generations */
		this.num_generations = new JTextField("100", 12);
		JPanel p2 = new JPanel(new GridLayout(2, 1));
		p2.add(new JLabel("Número generaciones:"));
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
		p4.add(new JLabel("Probabilidad de mutación:"));
		p4.add(mutation_perc);
		barraizq.add(p4);

		/* Precition */
		this.prec = new JTextField("0.0001", 12);
		JPanel p5 = new JPanel(new GridLayout(2, 1));
		p5.add(new JLabel("Precisión:"));
		p5.add(prec);
		barraizq.add(p5);

		/* Function Selection */
		this.function_sel = new JComboBox<>(function_sel_ops);
		JPanel p6 = new JPanel(new GridLayout(2, 1));
		p6.add(new JLabel("Función:"));
		p6.add(function_sel);
		barraizq.add(p6);
		
		/* Number Parameters */
		this.func4_params = new JSpinner();
		func4_params.setValue(3);
		func4_params.setEnabled(false);
		JPanel p7 = new JPanel(new GridLayout(2, 1));
		p7.add(new JLabel("Número Argumentos:"));
		p7.add(func4_params);
		barraizq.add(p7);
		
		/* Selection Selection */
		this.selection_sel = new JComboBox<>(selection_type);
		JPanel p11 = new JPanel(new GridLayout(2, 1));
		p11.add(new JLabel("Selección:"));
		p11.add(selection_sel);
		barraizq.add(p11);
		
		/* Mutation Selection */
		this.mutation_sel = new JComboBox<>(mutation_type);
		JPanel p12 = new JPanel(new GridLayout(2, 1));
		p12.add(new JLabel("Mutación:"));
		p12.add(mutation_sel);
		barraizq.add(p12);
		
		/* Cross Selection */
		this.cross_sel = new JComboBox<>(cross_type);
		JPanel p13 = new JPanel(new GridLayout(2, 1));
		p13.add(new JLabel("Cruce:"));
		p13.add(cross_sel);
		barraizq.add(p13);


		/* Has Elitism */
		/*this.elitism = new JCheckBox("Elitismo");
		JPanel p8 = new JPanel(new GridLayout(1, 1));
		p8.add(elitism);
		barraizq.add(p8);*/

		/* Number Elitism */
		this.elitism_amount = new JSpinner();
		elitism_amount.setValue(5);
		JPanel p9 = new JPanel(new GridLayout(2, 1));
		p9.add(new JLabel("Número Elitismo:"));
		p9.add(elitism_amount);
		barraizq.add(p9);

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

		JLabel footer = new JLabel("Realizado por Lukas Haring y Raúl Torrijos", SwingConstants.CENTER);
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
		barradchaftr.add(new JLabel("Mejor evaluación:"), BorderLayout.PAGE_END);
		JLabel best_ev = new JLabel(" ");
		barradchaftr.add(best_ev);

		add(barradcha, BorderLayout.LINE_END);

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				restartPlot();
				restartResults(barradchactr, titulodcha);

				Function f = new Function1(null);
				String function_name = (String) function_sel.getSelectedItem();
				switch (function_name) {
				case "Función 1":
					f = new Function1(FunctionType.MAXIMIZE);
					break;
				case "Función 2":
					f = new Function2(FunctionType.MINIMIZE);
					break;
				case "Función 3":
					f = new Function3(FunctionType.MINIMIZE);
					break;
				case "Función 4":
					f = new Function4((Integer) func4_params.getValue(), FunctionType.MINIMIZE);
					break;
				}

				int elitism_am = 0;
				//if (elitism.isSelected()) {
					elitism_am = ((Integer) elitism_amount.getValue());
				//}

				int num_gen = Integer.parseInt(num_generations.getText());

				model.SelectionType type_sel = SelectionType.ROULETTE;
				String selection_name = (String) selection_sel.getSelectedItem();
				switch (selection_name) {
					case "Ruleta":
						type_sel = SelectionType.ROULETTE;
					break;
					case "Torneo Determinista":
						type_sel = SelectionType.DETE_TOURNAMENT;
					break;
					case "Torneo Probabilístico":
						type_sel = SelectionType.PRB_TOURNAMENT;
					break;
				}
				
				model.MutationType type_mut = MutationType.RANDOM;
				String mute_name = (String) mutation_sel.getSelectedItem();
				switch (mute_name) {
					case "Aleatoria (Binario)":
						type_mut = MutationType.RANDOM;
					break;
					case "Uniforme (Reales)":
						type_mut = MutationType.UNIFORM;
					break;
					case "No uniforme (Reales)":
						type_mut = MutationType.NONUNIFORM;
					break;
				}

				model.CrossType type_cross = CrossType.MONOPOINT;
				String cross_name = (String) cross_sel.getSelectedItem();
				switch(cross_name) {
					case "Monopunto":
						type_cross = CrossType.MONOPOINT;
					break;
					case "Multipunto":
						type_cross = CrossType.MULTIPOINT;
					break;
					case "Uniforme (Binario)":
						type_cross = CrossType.UNIFORM;
					break;
				}
				
				
				if (chrtype_sel.getSelectedItem().equals(chrtype_sel_ops[0])) {
					ga = new GeneticAlgorithm<BinaryChromosome>(BinaryChromosome.class,
							Integer.parseInt(size_population.getText()), num_gen,
							Double.parseDouble(crossover_perc.getText()), Double.parseDouble(mutation_perc.getText()),
							Double.parseDouble(prec.getText()), elitism_am, type_sel, type_cross, type_mut, f);
				} else {
					ga = new GeneticAlgorithm<RealChromosome>(RealChromosome.class,
							Integer.parseInt(size_population.getText()), num_gen,
							Double.parseDouble(crossover_perc.getText()), Double.parseDouble(mutation_perc.getText()),
							Double.parseDouble(prec.getText()), elitism_am, type_sel, type_cross, type_mut, f);
				}

				List<double[]> best_chromosomes = ga.run();

				double[] generations = new double[num_gen];
				for (int i = 0; i < num_gen; ++i)
					generations[i] = i;

				addPlotLines(generations, best_chromosomes);

				best_ev.setText(Double.toString(best_chromosomes.get(0)[best_chromosomes.get(0).length - 1]));

				for (int i = 0; i < ga.getBest_chr().getFenotypes().length; i++) {
					barradchactr.add(
							new JLabel("x" + (i + 1) + ": " + Double.toString(ga.getBest_chr().getFenotypes()[i])));
				}

			}
		});

		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartPlot();
				best_ev.setText(" ");
				restartResults(barradchactr, titulodcha);

			}
		});

		/*elitism.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				elitism_amount.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});*/

		function_sel.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				Object item = event.getItem();
				func4_params.setEnabled((event.getStateChange() == ItemEvent.SELECTED)
						&& (item.toString().equals(function_sel_ops[3])));

				if ((event.getStateChange() == ItemEvent.SELECTED) && (item.toString().equals(function_sel_ops[3]))) {
					func4_params.setEnabled(true);
					chrtype_sel.setEnabled(true);
				} else {
					func4_params.setEnabled(false);
					chrtype_sel.setSelectedItem(chrtype_sel_ops[0]);
					chrtype_sel.setEnabled(false);
				}
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
		remove(plot);
		plot = new Plot2DPanel();
		plot.addLegend("SOUTH");
		add(plot, BorderLayout.CENTER);
		repaint();
		validate();
	}

	void addPlotLines(double[] generations, List<double[]> best_chromosomes) {
		plot.addLinePlot("Mejor absoluto", generations, best_chromosomes.get(0));
		plot.addLinePlot("Mejor de la generación", generations, best_chromosomes.get(1));
		plot.addLinePlot("Media de la generación", generations, best_chromosomes.get(2));
	}
}
