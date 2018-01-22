package GUIQytetet;
import modeloqytetet.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import java.awt.Panel;
import java.awt.Point;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import java.awt.SystemColor;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import javafx.scene.image.Image;
import modeloqytetet.Calle;
import modeloqytetet.Casilla;
import modeloqytetet.Jugador;
import modeloqytetet.MetodoSalirCarcel;
import modeloqytetet.OtraCasilla;
import modeloqytetet.Qytetet;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GradientPaint;
import javax.swing.border.EtchedBorder;
import java.awt.Canvas;
import java.awt.FlowLayout;
import javax.swing.JSpinner;
import java.awt.Choice;
import javax.swing.JRadioButton;


public class VistaQytetet {
	private JFrame frame;
	private Qytetet juego;
	
	private final String input_player = "Introduce el nombre del jugador";
	
	// Images
	private ArrayList<BufferedImage> players = new ArrayList<BufferedImage>();
	private BufferedImage luck;
	private BufferedImage casillas;
	private BufferedImage parking;
	private BufferedImage salida;
	private BufferedImage prision;
	private BufferedImage juez;
	private BufferedImage impuesto;
	
	// Jpannels for Names
	private final ArrayList<JTextField> jplayers = new ArrayList<JTextField>();
	private final ArrayList<JPanel> jplayerp = new ArrayList<JPanel>();
	private final JLabel arrow = new JLabel("");
	private final JLabel dice = new JLabel("");
	private final Choice choice = new Choice();
	
	// Primary Pannels
	private final JPanel game = new JPanel();
	private final JPanel display = new JPanel();
	// Game pannels
	private JPanel game_display;
	private final JPanel player_info = new JPanel();
	private final JButton throw_dice = new JButton("Lanzar el dado");
	// Global
	private Jugador jugador;
	private Casilla casilla;
	// Compra
	private final JPanel realizar_compra = new JPanel();
	private final JLabel lblPrecio = new JLabel("Precio: 300$");
	private final JLabel nombre_compra = new JLabel("Calle mar\u00EDa de los dolores");
	// Operation
	private final JPanel operation_pannel = new JPanel();
	
	
	private FocusListener clean_input = new FocusListener() {
		@Override
		public void focusGained(FocusEvent e) {
			((JTextField)e.getSource()).setText("");
		}

		@Override
		public void focusLost(FocusEvent e) {
			if("".equals(((JTextField)e.getSource()).getText())){
				((JTextField)e.getSource()).setText(input_player);
			}
		}
	};
	
	final private ActionListener play_game = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			ArrayList<String> names_array = new ArrayList<String>();
			for(JTextField input : jplayers) {
				if(!input_player.equals(input.getText())) {
					names_array.add(input.getText());
				}
			}
			if(names_array.size() < 1) {
				JOptionPane.showMessageDialog(frame, "No hay ningún jugador");
			}else if(names_array.size() < 2){
				JOptionPane.showMessageDialog(frame, "Es necesario, como mínimo, dos jugadores.");
			}else {
				juego.inicializarJuego(names_array);
				game.setVisible(true);
				display.setVisible(false);
				jugador = juego.getJugadorActual();
				casilla = jugador.getCasillaActual();
				
				int i = 0;
				for(Jugador jugador : juego.getJugadores()){
					JPanel panel = new JPanel();
					jplayerp.add(panel);
					panel.setBounds(13, 20 + i * 100, 152, 84);
					player_info.add(panel);
					panel.setLayout(null);
					
					JLabel liberty = new JLabel("");
					liberty.setBounds(50, 0, 32, 33);
					panel.add(liberty);
					liberty.setVisible(false);
					//((JLabel)((jplayerp.get(0).getComponent(0)).getComponent(0))).setVisible(true);
					liberty.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/no-prison.png")));
					
					JLabel saldo = new JLabel(Integer.toString(jugador.getSaldo()));
					saldo.setHorizontalAlignment(SwingConstants.CENTER);
					saldo.setBounds(70, 11, 55, 14);
					panel.add(saldo);
					
					JLabel name = new JLabel(jugador.getNombre());
					name.setBounds(75, 50, 55, 14);
					panel.add(name);
					
					JLabel prision = new JLabel("");
					prision.setBounds(15, 7, 43, 68);
					panel.add(prision);
					prision.setVisible(false);
					prision.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/carcel.png")));
					
					JLabel especulador = new JLabel("");
					especulador.setBounds(35, 18, 20, 26);
					panel.add(especulador);
					especulador.setVisible(false);
					especulador.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/especulador.png")));
					
					JLabel lblNewLabel = new JLabel("");
					lblNewLabel.setBounds(0, 0, 152, 84);
					panel.add(lblNewLabel);
					++i;
					lblNewLabel.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/player_info-"+ i +".png")));
				}
			}
		}
	};
	
	private void draw_dice() {
		/*for(int i = 0; i < 4; ++i) {
			int random = (int)Math.floor(Math.random() * 6) + 1;
			dice.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/d"+ random +".png")));
			try {
				Thread.sleep(100);
			}catch(InterruptedException ex) {}
		}*/
		dice.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/d"+ Dado.getInstance().ultimo +".png")));
	}
	
	private void actualizar_saldo() {
		((JLabel)(jplayerp.get(juego.jugador_actual()).getComponent(1))).setText(Integer.toString(jugador.getSaldo()));
	}
	
	private boolean pasar_turno(boolean finalizado) {
		if(!finalizado) {
			actualizar_saldo();
			juego.siguienteJugador();
			jugador = juego.getJugadorActual();
			casilla = jugador.getCasillaActual();
			throw_dice.setEnabled(true);
			
			arrow.setBounds(40, 74 + juego.jugador_actual() * 100, 25, 34);
			return jugador.estaBancarrota();
		}
		
		if(finalizado){
			/*vista.mostrar("El juego ha acabado, un jugador esta en bancarrota.");
			ArrayList<Map.Entry<String, Integer>> ranking = juego.obtenerRanking();
			for(Map.Entry<String, Integer> person : ranking) {
				vista.mostrar(person.getKey() + " con un capital " + person.getValue());
			}*/
		}
		return true;
	}
	
	
	public void desarrolloJuego() {
		boolean puedeJugar = true, finalizado = false;
		int player_number = juego.jugador_actual();
		
		// Empezamos a jugar
		boolean encarcelado = false;
		if(jugador.getEncarcelado()) {
			encarcelado = true;
			juego.intentarSalirCarcel(MetodoSalirCarcel.TIRANDODADO);
			draw_dice();
			if(jugador.getEncarcelado()){
				JOptionPane.showMessageDialog(frame, "¡No pudo salir!");
				puedeJugar = false;
				pasar_turno(false);
			}else{
				JOptionPane.showMessageDialog(frame, "¡Has conseguido salir de la carcel!");
				((JLabel)(jplayerp.get(player_number).getComponent(3))).setVisible(false);
			}
		}
		boolean saldo_act = false;
		if(puedeJugar){
			  int dinero = jugador.getSaldo();
			  boolean noTienePropietario = juego.jugar();
			  if(Math.abs(dinero - jugador.getSaldo()) > 0) {
				  actualizar_saldo();
				  saldo_act = true;
			  }
			  game_display.revalidate();
			  game_display.repaint();
			  draw_dice();
			  Casilla desplazamiento = casilla = jugador.getCasillaActual();

			  if(jugador.estaBancarrota()) {
				  finalizado = true;
			  }else if(!jugador.getEncarcelado()) {
					if(desplazamiento instanceof OtraCasilla && ((OtraCasilla)desplazamiento).getTipo() == TipoCasilla.SORPRESA) {
						JOptionPane.showMessageDialog(frame, "Carta sorpresa\n" + juego.getCartaActual().toString());
						switch(juego.getCartaActual().getTipo()){
							case SALIRCARCEL:
								((JLabel)(jplayerp.get(player_number).getComponent(0))).setVisible(true);
							break;
							case IRACASILLA:
								game_display.revalidate();
								game_display.repaint();
							break;
							case CONVERTIRME:
								((JLabel)(jplayerp.get(player_number).getComponent(4))).setVisible(true);
							break;
							default: break;
						}
						noTienePropietario = juego.aplicarSorpresa();
						pasar_turno(false);
					}
				  if(!jugador.getEncarcelado()) {
					  boolean compra = false;
					  if(!noTienePropietario && desplazamiento instanceof Calle){
						compra = true;
						lblPrecio.setText("Precio: " + (desplazamiento.getCoste()) + "$");
						nombre_compra.setText(((Calle)desplazamiento).getTitulo().getNombre());
						realizar_compra.setVisible(true);
					  }
					  
					  if(jugador.estaBancarrota()) {
						  finalizado = true;
					  }else if(jugador.tengoPropiedades()){
						  choice.removeAll();
						  System.out.println(3);
						  for(TituloPropiedad titulo : jugador.propiedades) {
							  choice.add(titulo.getNombre());
						  }
						  if(!compra) {
							  operation_pannel.setVisible(true);
						  }
					  }else if(saldo_act) {
						  pasar_turno(finalizado);
					  }
				  }
			  }
		}
		
		// MOVIMIENTO FINAL DEL JUGADOR
		if(jugador.getEncarcelado() && !encarcelado) {
			game_display.revalidate();
			game_display.repaint();
			JOptionPane.showMessageDialog(frame, "¡Ha entrado en la carcel!");
			((JLabel)(jplayerp.get(player_number).getComponent(3))).setVisible(true);
			pasar_turno(false);
		}
		
		if(finalizado) { pasar_turno(true); }
	};
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VistaQytetet window = new VistaQytetet();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VistaQytetet() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		try {
			luck = ImageIO.read(VistaQytetet.class.getResourceAsStream("/Images/suerte.png"));
		}catch(IOException err) {}
		try {
			casillas = ImageIO.read(VistaQytetet.class.getResourceAsStream("/Images/casillas.png"));
		}catch(IOException err) {}
		try {
			parking = ImageIO.read(VistaQytetet.class.getResourceAsStream("/Images/parking.png"));
		}catch(IOException err) {}
		try {
			salida = ImageIO.read(VistaQytetet.class.getResourceAsStream("/Images/salida.png"));
		}catch(IOException err) {}
		try {
			prision = ImageIO.read(VistaQytetet.class.getResourceAsStream("/Images/prision.png"));
		}catch(IOException err) {}
		try {
			juez = ImageIO.read(VistaQytetet.class.getResourceAsStream("/Images/juez.png"));
		}catch(IOException err) {}
		try {
			impuesto = ImageIO.read(VistaQytetet.class.getResourceAsStream("/Images/impuestos.png"));
		}catch(IOException err) {}

		for(int i = 1; i <= 4; ++i) {
			try {
					players.add(ImageIO.read(VistaQytetet.class.getResourceAsStream("/Images/player" + i + ".png")));
			}catch(IOException err) {}
		}
		
		juego = Qytetet.getInstance();
		// FRAME
		frame = new JFrame();
		frame.setTitle("Juego Qytetet - Lukas Häring García");
		frame.setBounds(100, 100, 700, 600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		// GAME PANNEL
		game.setVisible(false);
		game.setBackground(Color.WHITE);
		game.setBounds(0, 0, 694, 570);
		frame.getContentPane().add(game);
		game.setLayout(null);
		
		player_info.setBackground(Color.WHITE);
		player_info.setBounds(0, 0, 180, 504);
		game.add(player_info);
		player_info.setLayout(null);
		
		arrow.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/curren_player.png")));
		arrow.setBounds(40, 74, 25, 34);
		player_info.add(arrow);
		
		game_display = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int ci = 514 / 2 - 150;
				int cj = 504 / 2 + 150;
				
				int radius = 10;
				String name;
				Casilla casilla;
				BufferedImage draw;
				for(int j = 0; j < 4; ++j){
					int dx = (j % 2) * (j - 2);
					int dy = ((j + 1) % 2) * (j - 1);
					casilla = juego.getTablero().getCasilla(j * 5);
					switch (((OtraCasilla)casilla).getTipo()) {
						case SALIDA: draw = salida; break;
						case CARCEL: draw = prision; break;
						case JUEZ: draw = juez; break;
						case IMPUESTO: draw = impuesto; break;
						default: draw = null; break;
					}
					g.drawImage(draw, ci - 50, cj - 50, null);
					draw_player(g, j * 5, ci, cj);
					
					ci -= 75 * dx;
					cj += 75 * dy;
					
					for(int i = 1; i < 5; ++i) {
						casilla = juego.getTablero().getCasilla(i + j * 5);
						int wd = 100 - Math.abs(dx) * 50;
						int hd = 100 - Math.abs(dy) * 50;
						name = casilla.toString();
						Graphics2D g2 = (Graphics2D) g.create();
						// Draw Background
						g2.translate(ci, cj);
						g2.rotate(-Math.PI / 2 * dy);
						g2.translate(-25, -50);
						draw = casillas;
						if(casilla instanceof OtraCasilla){
							switch(((OtraCasilla)casilla).getTipo()) {
								case SORPRESA: draw = luck; break;
								case PARKING: draw = parking; break;
								default:break;
							}
						}
						g2.drawImage(draw, 0, 0, null);
						g2.dispose();
						// Draw Text
						if(casilla instanceof Calle) {
							g2 = (Graphics2D) g.create();
							g2.setColor(Color.BLACK);
							g2.translate(ci, cj);
							g2.rotate(-Math.PI / 2 * dx);
							String p = "";
							if((((Calle) casilla).getTitulo()).tengoPropietario()) {
								p = (((Calle) casilla).getTitulo()).propietario.getNombre();
							}else {
								p = "Precio: " + casilla.getCoste() + "$";
							}
							g2.drawString(p, -(p.length() * 6) / 2, 13);
							g2.translate(-name.length() * (casilla instanceof Calle ? 5 : 7) / 2, -2);
							g2.drawString(name, 0, 0);
							g2.dispose();
						}
						
						draw_player(g, j * 5 + i, ci, cj);
						
						ci -= wd * dx;
						cj += hd * dy;
					}
					ci -= 25 * dx;
					cj += 25 * dy;
				}
			}

			private void draw_player(Graphics g, int j, int ci, int cj) {
				int p = 0;
				for(Jugador player : juego.getJugadores()) {
					if(player.getCasillaActual().getNumeroCasilla() == j) {
						g.drawImage(players.get(p), ci - 50 + 10 + (p % 2) * 30, cj - 50 + 30 + 16 * (int)Math.floor(p/2), null);
					}
					++p;
				}
			}
		};
		game_display.setBounds(179, 0, 514, 504);
		game.add(game_display);
		game_display.setLayout(null);
		
		realizar_compra.setVisible(false);
		
		operation_pannel.setVisible(false);
		operation_pannel.setBorder(new LineBorder(new Color(0, 0, 0)));
		operation_pannel.setBackground(Color.WHITE);
		operation_pannel.setBounds(62, 123, 392, 244);
		game_display.add(operation_pannel);
		operation_pannel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOperacionesARealizar = new JLabel("Operaciones a realizar");
		lblOperacionesARealizar.setHorizontalAlignment(SwingConstants.CENTER);
		lblOperacionesARealizar.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		operation_pannel.add(lblOperacionesARealizar, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		operation_pannel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(1, 1));
		
		JButton cancel_action = new JButton("Pasar el turno");
		cancel_action.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				operation_pannel.setVisible(false);
				pasar_turno(false);
			}
		});
		panel_1.add(cancel_action, BorderLayout.WEST);
		ButtonGroup group = new ButtonGroup();
		
		JButton btnNewButton_2 = new JButton("Aceptar y pasar el turno");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Calle calle = jugador.propiedades.get(choice.getSelectedIndex()).casilla;
				int opcion = group.getSelection().getMnemonic() - 'A';
				System.out.println(opcion);
				System.out.println((char)opcion);
				boolean posible = true;
				switch(opcion) {
					case 1: posible = juego.edificarCasa(calle); break;
					case 2: posible = juego.edificarHotel(calle); break;
					case 3: posible = juego.venderPropiedad(calle); break;
					case 4: posible = juego.hipotecarPropiedad(calle); break;
					case 5: posible = juego.cancelarHipoteca(calle); break;
				}
				if(posible) {
					operation_pannel.setVisible(false);
					pasar_turno(false);
				}else {
					JOptionPane.showMessageDialog(frame, "No se pudo realizar la compra.");
				}
			}
		});
		panel_1.add(btnNewButton_2, BorderLayout.EAST);
		
		JPanel panel_3 = new JPanel();
		operation_pannel.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(null);
		
		JRadioButton rdbtnEdificarCasa = new JRadioButton("Edificar Casa");
		group.add(rdbtnEdificarCasa);
		rdbtnEdificarCasa.setSelected(true);
		rdbtnEdificarCasa.setMnemonic('a');
		rdbtnEdificarCasa.setBounds(47, 76, 143, 30);
		panel_3.add(rdbtnEdificarCasa);
		
		JRadioButton rdbtnEdificarHotel = new JRadioButton("Edificar Hotel");
		group.add(rdbtnEdificarHotel);
		rdbtnEdificarHotel.setMnemonic('b');
		rdbtnEdificarHotel.setBounds(192, 76, 158, 30);
		panel_3.add(rdbtnEdificarHotel);
		
		JRadioButton rdbtnVenderPropiedad = new JRadioButton("Vender Propiedad");
		group.add(rdbtnVenderPropiedad);
		rdbtnVenderPropiedad.setMnemonic('e');
		rdbtnVenderPropiedad.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnVenderPropiedad.setBounds(47, 144, 303, 30);
		panel_3.add(rdbtnVenderPropiedad);
		
		JRadioButton rdbtnHipotecarPropiedad = new JRadioButton("Hipotecar Propiedad");
		group.add(rdbtnHipotecarPropiedad);
		rdbtnHipotecarPropiedad.setMnemonic('d');
		rdbtnHipotecarPropiedad.setBounds(192, 109, 158, 30);
		panel_3.add(rdbtnHipotecarPropiedad);
		
		JRadioButton rdbtnCancelarHipoteca = new JRadioButton("Cancelar Hipoteca");
		group.add(rdbtnCancelarHipoteca);
		rdbtnCancelarHipoteca.setMnemonic('c');
		rdbtnCancelarHipoteca.setBounds(47, 109, 143, 30);
		panel_3.add(rdbtnCancelarHipoteca);
		
		choice.setBounds(47, 38, 303, 20);
		
		panel_3.add(choice);
		realizar_compra.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		realizar_compra.setBackground(Color.WHITE);
		realizar_compra.setBounds(115, 192, 281, 100);
		game_display.add(realizar_compra);
		realizar_compra.setLayout(new BorderLayout(2, 2));
		
		JLabel lblNewLabel_1 = new JLabel("\u00BFQuiere realizar la compra?");
		lblNewLabel_1.setFont(new Font("Yu Gothic Light", Font.PLAIN, 22));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		realizar_compra.add(lblNewLabel_1, BorderLayout.NORTH);
		
		JButton btnNewButton_1 = new JButton("No") {
			protected void paintComponent(Graphics g){
		        Graphics2D g2 = (Graphics2D)g.create();
		        g2.setPaint(new GradientPaint(
		                new Point(0, 0), 
		                new Color(180, 30, 30), 
		                new Point(0, getHeight()/3), 
		                Color.WHITE));
		        g2.fillRect(0, 0, getWidth(), getHeight()/3);
		        g2.setPaint(new GradientPaint(
		                new Point(0, getHeight()/3), 
		                Color.WHITE, 
		                new Point(0, getHeight()), 
		                new Color(180, 30, 30)));
		        g2.fillRect(0, getHeight()/3, getWidth(), getHeight());
		        g2.dispose();

		        super.paintComponent(g);
		    }
		};
		
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realizar_compra.setVisible(false);
				if(jugador.tengoPropiedades()) {
					operation_pannel.setVisible(true);
				}else {
					pasar_turno(false);
				}
			}
		});
		realizar_compra.add(btnNewButton_1, BorderLayout.WEST);
		
		JButton btnS = new JButton("Sí") {
			protected void paintComponent(Graphics g){
		        Graphics2D g2 = (Graphics2D)g.create();
		        g2.setPaint(new GradientPaint(
		                new Point(0, 0), 
		                new Color(30, 180, 30), 
		                new Point(0, getHeight()/3), 
		                Color.WHITE));
		        g2.fillRect(0, 0, getWidth(), getHeight()/3);
		        g2.setPaint(new GradientPaint(
		                new Point(0, getHeight()/3), 
		                Color.WHITE, 
		                new Point(0, getHeight()), 
		                new Color(30, 180, 30)));
		        g2.fillRect(0, getHeight()/3, getWidth(), getHeight());
		        g2.dispose();

		        super.paintComponent(g);
		    }
		};
		btnS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean compra = juego.comprarTituloPropiedad();
				if(compra) {
					String calle = ((Calle)juego.getJugadorActual().getCasillaActual()).getTitulo().getNombre();
					choice.add(calle);
					JOptionPane.showMessageDialog(frame, "¡El usuario compró " + calle + "!");
					game_display.revalidate();
					game_display.repaint();
				}else{
					JOptionPane.showMessageDialog(frame, "No se pudo realizar la compra.");
				}
				realizar_compra.setVisible(false);
				if(jugador.tengoPropiedades()) {
					operation_pannel.setVisible(true);
				}else {
					pasar_turno(jugador.estaBancarrota());
				}
			}
		});
		btnS.setContentAreaFilled(false);
		realizar_compra.add(btnS, BorderLayout.EAST);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(135, 206, 250));
		realizar_compra.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		nombre_compra.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(nombre_compra, BorderLayout.CENTER);
		
		lblPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_2.add(lblPrecio, BorderLayout.SOUTH);
		
		JPanel dado = new JPanel();
		dado.setForeground(new Color(0, 0, 0));
		dado.setBorder(new LineBorder(new Color(0, 0, 51), 3));
		dado.setBounds(204, 200, 101, 100);
		game_display.add(dado);
		dado.setBackground(Color.WHITE);
		dado.setLayout(null);
		
		dice.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/d1.png")));
		dice.setBounds(10, 11, 80, 80);
		dado.add(dice);
		
		JPanel footer = new JPanel();
		footer.setBackground(Color.WHITE);
		footer.setBounds(0, 503, 693, 67);
		game.add(footer);
		footer.setLayout(null);
		
		throw_dice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				throw_dice.setEnabled(false);
				desarrolloJuego();
			}
		});
		throw_dice.setBounds(559, 11, 124, 45);
		footer.add(throw_dice);
		//
		display.setBounds(0, 0, 694, 571);
		frame.getContentPane().add(display);
		display.setLayout(null);
		
		
		// NEXT PANNEL
		JPanel name_selection = new JPanel();
		for(int i = 0; i < 4; ++i){
			int padding = 54 * i;
			JLabel input = new JLabel("Jugador " + (i + 1));
			input.setBounds(49, 30 + padding, 150, 14);
			name_selection.add(input);
			
			JTextField input_name = new JTextField();
			input_name.setBounds(49, 45 + padding, 228, 30);
			input_name.addFocusListener(clean_input);
			input_name.setText(input_player);
			name_selection.add(input_name);
			jplayers.add(input_name);
		}
		name_selection.setBounds(198, 209, 302, 362);
		display.add(name_selection);
		name_selection.setVisible(false);
		name_selection.setBorder(null);
		name_selection.setBackground(Color.WHITE);
		name_selection.setLayout(null);
		
		// NEXT STEP
		JButton btnNewButton = new JButton("Seguir");
		btnNewButton.addActionListener(play_game);
		
		btnNewButton.setBounds(49, 254, 228, 47);
		name_selection.add(btnNewButton);
		
		JLabel logo_title = new JLabel("");
		logo_title.setBounds(135, 0, 430, 209);
		display.add(logo_title);
		logo_title.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/logo.png")));
		logo_title.setBorder(new LineBorder(new Color(0, 0, 0), 4));
		
		JButton play_button = new JButton("");
		play_button.setBounds(206, 415, 288, 145);
		display.add(play_button);
		play_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				name_selection.setVisible(true);
				play_button.setVisible(false);
			}
		});
		play_button.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/play_button.png")));
		play_button.setBorderPainted(false);
		play_button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		JLabel background = new JLabel(new ImageIcon(VistaQytetet.class.getResource("/Images/background.png")));
		background.setBounds(0, 0, 700, 600);
		display.add(background);
	}
}
