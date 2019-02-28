package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
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
import model.RealChromosome;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Panel extends JFrame {

	private static final long serialVersionUID = 2569879142816556337L;

	Plot2DPanel plot;
	
	private JComboBox<String> chrtype_sel;
	private String[] chrtype_sel_ops = { "Binario" , "Real" };
	private JTextField size_population;
	private JTextField num_generations;
	private JTextField crossover_perc;
	private JTextField mutation_perc;
	private JTextField prec;
	private String[] function_sel_ops = { "Función 1", "Función 2", "Función 3", "Función 4" };
	private JComboBox<String> function_sel;
	private JSpinner func4_params;
	private JButton start;
	private JButton restart;
	private JCheckBox elitism;
	private JTextField elitism_amount;
	private Dimension size;

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
		
		this.chrtype_sel = new JComboBox<>(chrtype_sel_ops);
		this.size_population = new JTextField("100", 12);
		this.num_generations = new JTextField("100", 12);
		this.crossover_perc = new JTextField("0.6", 12);
		this.mutation_perc = new JTextField("0.05", 12);
		this.prec = new JTextField("0.0001", 12);
		this.function_sel = new JComboBox<>(function_sel_ops);
		this.func4_params = new JSpinner();
		this.elitism = new JCheckBox("Elitismo");
		this.elitism_amount = new JTextField("5", 6);
		this.elitism_amount.setEnabled(false);
		start = new JButton("Iniciar");
		restart = new JButton("Restablecer");
		elitism = new JCheckBox("Elitismo");
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
		JPanel barradcha = new JPanel();

		barraizq.setLayout(new GridLayout(19, 2, 10, 10));

		barraizq.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JLabel titulo = new JLabel("PARÁMETROS:");
		titulo.setFont(titulo.getFont().deriveFont(16.0f));

		barraizq.add(titulo);
		barraizq.add(new JLabel("Tipo de cromosoma:"));
		barraizq.add(chrtype_sel);
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
		barraizq.add(elitism_amount);

		barraizq.add(function_sel);
		func4_params.setEnabled(false);
		barraizq.add(func4_params);
		func4_params.setValue(3);

		
		barraizq.add(start);
		barraizq.add(restart);

		barraizq.setBorder(BorderFactory.createEmptyBorder(8, 24, 0, 15));
		add(barraizq, BorderLayout.LINE_START);

		JLabel footer = new JLabel("Realizado por Lukas Haring y Raúl Torrijos", SwingConstants.CENTER);
		footer.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(footer, BorderLayout.PAGE_END);
		setVisible(true);

		// OUTPUT

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
				if (elitism.isSelected()) {
					elitism_am = Integer.parseInt(elitism_amount.getText());
				}
				
				

				int num_gen = Integer.parseInt(num_generations.getText());
				
				
				if(chrtype_sel.getSelectedItem().equals(chrtype_sel_ops[0])){
					ga = new GeneticAlgorithm<BinaryChromosome>(BinaryChromosome.class,
							Integer.parseInt(size_population.getText()), num_gen,
							Double.parseDouble(crossover_perc.getText()), Double.parseDouble(mutation_perc.getText()),
							Double.parseDouble(prec.getText()), elitism_am, f);
				} else {
					ga = new GeneticAlgorithm<RealChromosome>(RealChromosome.class,
							Integer.parseInt(size_population.getText()), num_gen,
							Double.parseDouble(crossover_perc.getText()), Double.parseDouble(mutation_perc.getText()),
							Double.parseDouble(prec.getText()), elitism_am, f);
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

		elitism.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				elitism_amount.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		function_sel.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				Object item = event.getItem();
				func4_params.setEnabled((event.getStateChange() == ItemEvent.SELECTED)
						&& (item.toString().equals(function_sel_ops[3])));

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
