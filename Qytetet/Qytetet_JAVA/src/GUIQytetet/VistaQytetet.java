package GUIQytetet;
import modeloqytetet.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;
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
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.SystemColor;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import modeloqytetet.Calle;
import modeloqytetet.Casilla;
import modeloqytetet.Jugador;
import modeloqytetet.MetodoSalirCarcel;
import modeloqytetet.OtraCasilla;
import modeloqytetet.Qytetet;
import javax.swing.SwingConstants;


public class VistaQytetet {
	private JFrame frame;
	private Qytetet juego;
	
	private final String input_player = "Introduce el nombre del jugador";
	
	// Jpannels for Names
	private final ArrayList<JTextField> jplayers = new ArrayList<JTextField>();
	private final JLabel arrow = new JLabel("");
	
	// Primary Pannels
	private final JPanel game = new JPanel();
	private final JPanel display = new JPanel();
	// Game pannels
	private final JPanel player_info = new JPanel();
	// Global
	private Jugador jugador;
	private Casilla casilla;
	
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
					panel.setBounds(13, 20 + (i++) * 100, 152, 84);
					player_info.add(panel);
					panel.setLayout(null);
					
					JLabel label = new JLabel(Integer.toString(jugador.getSaldo()));
					label.setHorizontalAlignment(SwingConstants.CENTER);
					label.setBounds(70, 11, 55, 14);
					panel.add(label);
					
					JLabel lblLukas = new JLabel(jugador.getNombre());
					lblLukas.setBounds(75, 50, 55, 14);
					panel.add(lblLukas);
					
					JLabel lblNewLabel = new JLabel("New label");
					lblNewLabel.setBounds(0, 0, 152, 84);
					panel.add(lblNewLabel);
					lblNewLabel.setIcon(new ImageIcon(VistaQytetet.class.getResource("/Images/player_info.png")));
				}
			}
		}
	};
	
	public void desarrolloJuego() {
		boolean puedeJugar = true, finalizado = false;
		
		/*if(jugador.getEncarcelado()) {
			if(jugador.getEncarcelado()){
				vista.mostrar("Desgraciadamente el jugador sigue en la carcel.");
				puedeJugar = false;
			}else{
				JOptionPane.showMessageDialog(frame, "¡Has conseguido salir de la carcel!");
			}
		}
		/*
		if(puedeJugar){
			  boolean noTienePropietario = juego.jugar();
			  Casilla desplazamiento = jugador.getCasillaActual();
			  String ra = "Tras lanzar el dado, el jugador se desplazo hacia: \n" + desplazamiento.toString();

			  if(jugador.estaBancarrota()) {
				  finalizado = true;
			  }else if(!jugador.getEncarcelado()) {
				  if(desplazamiento instanceof OtraCasilla) {
				  		noTienePropietario = juego.aplicarSorpresa();
				  }
				  if(!jugador.getEncarcelado()) {
					  if(!noTienePropietario){
						  boolean comprar = vista.elegirQuieroComprar();
						  if(comprar) {
							  boolean compra = juego.comprarTituloPropiedad();
							  if(compra) {
								  vista.mostrar("El usuario compró " + ((Calle)desplazamiento).getTitulo().getNombre());	  
							  }else {
								  vista.mostrar("No se pudo realizar la compra.");
							  }
						  }
					  }
					  vista.mostrar(jugador.toString());
	
					  if(jugador.estaBancarrota()) {
						  finalizado = true;
					  }else if(jugador.tengoPropiedades()) {
						  int opcion = vista.menuGestionInmobiliaria();
						  while(opcion != 0) {
							  Calle calle = this.elegirPropiedad(jugador.propiedades);
							  boolean posible = true;
							  switch(opcion) {
							  	case 1: posible = juego.edificarCasa(calle); break;
							  	case 2: posible = juego.edificarHotel(calle); break;
							  	case 3: posible = juego.venderPropiedad(calle); break;
							  	case 4: posible = juego.hipotecarPropiedad(calle); break;
							  	//case 5: posible = juego.cancelarHipoteca(calle); break;
							  	default:break;
							  }
							  if(!posible) {
								  vista.mostrar("No se pudo realizar la operacion");
							  }
							  opcion = vista.menuGestionInmobiliaria();
						  }
					  }
				  }else {
					  vista.mostrar(jugador.toString());
				  }
			  }
		}
		*/
		if(!finalizado) {
			juego.siguienteJugador();
			jugador = juego.getJugadorActual();
			casilla = jugador.getCasillaActual();
			if(jugador.estaBancarrota()) {
				finalizado = true;
			}
		}
		int player_number = juego.jugador_actual();
		arrow.setBounds(40, 74 + player_number * 100, 25, 34);
		
		/*
		if(finalizado){
			vista.mostrar("El juego ha acabado, un jugador esta en bancarrota.");
			ArrayList<Map.Entry<String, Integer>> ranking = juego.obtenerRanking();
			for(Map.Entry<String, Integer> person : ranking) {
				vista.mostrar(person.getKey() + " con un capital " + person.getValue());
			}
		}
		*/
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
		
		JPanel game_display = new JPanel();
		game_display.setBounds(179, 0, 514, 504);
		game.add(game_display);
		
		JPanel footer = new JPanel();
		footer.setBackground(Color.WHITE);
		footer.setBounds(0, 503, 693, 67);
		game.add(footer);
		footer.setLayout(null);
		
		JButton throw_dice = new JButton("Lanzar el dado");
		throw_dice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		name_selection.setBounds(199, 210, 300, 360);
		display.add(name_selection);
		name_selection.setVisible(false);
		name_selection.setBorder(new LineBorder(new Color(0, 0, 0), 2));
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
