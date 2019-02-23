package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import org.math.plot.*;

import javax.swing.*;

public class Panel extends JFrame {

	private static final long serialVersionUID = 2569879142816556337L;

	public Panel() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		setTitle("Práctica 1");
		this.setMinimumSize(new Dimension(1200, 700));

		double[] x = { 1, 2, 3, 4, 5, 6, 7 };
		double[] y = { 45, 89, 6, 32, 63, 12, 45};

		Plot2DPanel plot = new Plot2DPanel();
		plot.addLegend("SOUTH");
		plot.addLinePlot("Mejor de la generación", x, y);
		add(plot, BorderLayout.CENTER);

		// Components
		JTextField size_population = new JTextField("100", 12);
		JTextField num_generations = new JTextField("100", 12);
		JTextField crossover_perc = new JTextField("0.6", 12);
		JTextField mutation_perc = new JTextField("0.05", 12);
		JTextField prec = new JTextField("0.0001", 12);
		String[] function_sel_ops = { "Función 1", "Función 2", "Función 3", "Función 4" };
		JComboBox function_sel = new JComboBox(function_sel_ops);
		JButton start = new JButton("Iniciar");
		JButton restart = new JButton("Restablecer");
		JCheckBox elitism = new JCheckBox("Elitismo");
		Dimension size = new Dimension(200, 20);

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

		add(barraizq, BorderLayout.LINE_START);
		add(new JLabel("Realizado por Lukas Haring y Raúl Torrijos", SwingConstants.RIGHT), BorderLayout.PAGE_END);
		setVisible(true);

	}
}
