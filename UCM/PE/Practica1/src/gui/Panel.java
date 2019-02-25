package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.math.plot.*;

import model.BinaryChromosome;
import model.Function;
import model.Function1;
import model.Function2;
import model.Function3;
import model.Function4;
import model.FunctionType;
import model.GeneticAlgorithm;

import javax.swing.*;

public class Panel extends JFrame {

	private static final long serialVersionUID = 2569879142816556337L;

	Plot2DPanel plot;

	private JTextField size_population;
	private JTextField num_generations;
	private JTextField crossover_perc;
	private JTextField mutation_perc;
	private JTextField prec;
	private String[] function_sel_ops = { "Función 1", "Función 2", "Función 3", "Función 4" };
	private JComboBox<String> function_sel;
	private JButton start;
	private JButton restart;
	private JCheckBox elitism = new JCheckBox("Elitismo");
	private Dimension size;
	private GeneticAlgorithm<BinaryChromosome> ga;

	/*
	 * double[] x; double[] y;
	 */
	public Panel() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setTitle("Práctica 1");
		this.setMinimumSize(new Dimension(1200, 700));

		double[] x = { 1, 2, 3, 4, 5, 6, 7 };
		double[] y = { 45, 89, 6, 32, 63, 12, 45 };

		this.plot = new Plot2DPanel();
		plot.addLegend("SOUTH");
		plot.addLinePlot("Mejor de la generación", x, y);
		add(plot, BorderLayout.CENTER);

		// Components
		this.size_population = new JTextField("100", 12);
		this.num_generations = new JTextField("100", 12);
		this.crossover_perc = new JTextField("0.6", 12);
		this.mutation_perc = new JTextField("0.05", 12);
		this.prec = new JTextField("0.0001", 12);
		this.function_sel = new JComboBox<>(function_sel_ops);
		start = new JButton("Iniciar");
		restart = new JButton("Restablecer");
		JCheckBox elitism = new JCheckBox("Elitismo");
		this.size = new Dimension(200, 20);

		function_sel.setPreferredSize(new Dimension(200, 25));

		function_sel.setMaximumSize(function_sel.getPreferredSize());

		size_population.setPreferredSize(size);
		size_population.setMaximumSize(size_population.getPreferredSize());

		num_generations.setPreferredSize(size);
		num_generations.setMaximumSize(size_population.getPreferredSize());

		crossover_perc.setPreferredSize(size);
		crossover_perc.setMaximumSize(size_population.getPreferredSize());

		mutation_perc.setPreferredSize(size);
		mutation_perc.setMaximumSize(size_population.getPreferredSize());

		prec.setPreferredSize(size);
		prec.setMaximumSize(size_population.getPreferredSize());

		JPanel barraizq = new JPanel();

		barraizq.setLayout(new GridLayout(20, 2, 10, 10));

		barraizq.add(new JLabel("Tamaño población:"));
		barraizq.add(size_population);
		barraizq.add(new JLabel("Número generaciones:"));
		barraizq.add(num_generations);
		barraizq.add(new JLabel("Porcentaje de cruces:"));
		barraizq.add(crossover_perc);
		barraizq.add(new JLabel("Porcentaje de mutación:"));
		barraizq.add(mutation_perc);
		barraizq.add(new JLabel("Precisión:"));
		barraizq.add(prec);
		barraizq.add(elitism);
		barraizq.add(function_sel);

		barraizq.add(start);
		barraizq.add(restart);

		barraizq.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
		add(barraizq, BorderLayout.LINE_START);
		add(new JLabel("Realizado por Lukas Haring y Raúl Torrijos", SwingConstants.RIGHT), BorderLayout.PAGE_END);
		setVisible(true);

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * x = new double[Integer.parseInt(num_generations.getText())];
				 * y = new double[Integer.parseInt(num_generations.getText())];
				 */
				Function f = new Function1(null);

				switch ((String) function_sel.getSelectedItem()) {
				case "Función 1":
					System.out.println("f1");
					f = new Function1(FunctionType.MAXIMIZE);
					break;
				case "Función 2":
					System.out.println("f2");
					f = new Function2(FunctionType.MINIMIZE);
					break;

				case "Función 3":
					System.out.println("f3");
					f = new Function3(FunctionType.MINIMIZE);
					break;

				case "Función 4":
					System.out.println("f4");
					f = new Function4(3, FunctionType.MINIMIZE);
					break;
				}

				ga = new GeneticAlgorithm<BinaryChromosome>(BinaryChromosome.class,
						Integer.parseInt(size_population.getText()), Integer.parseInt(num_generations.getText()),
						Double.parseDouble(crossover_perc.getText()), Double.parseDouble(mutation_perc.getText()),
						Double.parseDouble(prec.getText()), f);

				System.out.println(ga.run());

			}
		});

		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plot = new Plot2DPanel();

			}
		});
	}

	public void plotUpdate() {
		plot.update(getGraphics());
	}
}
