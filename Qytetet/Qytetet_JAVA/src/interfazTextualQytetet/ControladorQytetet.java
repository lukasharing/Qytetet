package interfazTextualQytetet;
import modeloqytetet.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
public class ControladorQytetet {
	private Qytetet juego;
	private Jugador jugador;
	private Casilla casilla;
	private VistaTextualQytetet vista;
	
	public ControladorQytetet() {
		jugador = null;
		casilla = null;
		vista = new VistaTextualQytetet();
	};
	
	public void desarrolloJuego() {
		boolean puedeJugar = true, finalizado = false;
		vista.mostrar("------------------------------------");
		String rs = "Es el turno de " + jugador.getNombre() + ", actualmente se encuentra en la casilla " + casilla.getNumeroCasilla();
		if(casilla.getTipo() == TipoCasilla.CALLE) {
			rs += " con nombre " + casilla.getTitulo().getNombre();
		}else {
			rs += " de tipo " + casilla.getTipo();
		}
		vista.mostrar(rs);
		
		if(jugador.getEncarcelado()) {
			int valor = vista.menuSalirCarcel();
			juego.intentarSalirCarcel(valor == 0 ? MetodoSalirCarcel.PAGANDOLIBERTAD : MetodoSalirCarcel.TIRANDODADO);
			if(jugador.getEncarcelado()) {
				vista.mostrar("Desgraciadamente el jugador sigue en la carcel.");
				puedeJugar = false;
			}else{
				vista.mostrar("Afortunadamente consiguio salir sin muchos problemas.");
			}
		}
		
		if(puedeJugar){
			  boolean noTienePropietario = juego.jugar();
			  Casilla desplazamiento = jugador.getCasillaActual();
			  String ra = "Tras lanzar el dado, el jugador se desplazo hacia ";
			  if(desplazamiento.getTipo() == TipoCasilla.CALLE) {
				  ra += desplazamiento.getTitulo().getNombre();
			  }else {
				  ra += " la casilla de tipo " + desplazamiento.getTipo();
			  }
			  vista.mostrar(ra + " (" + desplazamiento.getNumeroCasilla() + ").");

			  if(jugador.estaBancarrota()) {
				  finalizado = true;
			  }else if(!jugador.getEncarcelado()) {
				  switch(desplazamiento.getTipo()) {
				  	case SORPRESA:
				  		vista.mostrar("Se trata de una carta sorpresa " + desplazamiento.getTipo() + " -> " + juego.getCartaActual().getDescripcion());
				  		noTienePropietario = juego.aplicarSorpresa();
				  	break;
				  	case CALLE:
				  		vista.mostrar(desplazamiento.toString());
			  		break;
				  	default:break;
				  }
				  if(!jugador.getEncarcelado()) {
					  if(!noTienePropietario){
						  boolean comprar = vista.elegirQuieroComprar();
						  if(comprar) {
							  boolean compra = juego.comprarTituloPropiedad();
							  if(compra) {
								  vista.mostrar("El usuario compró " + desplazamiento.getTitulo().getNombre());	  
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
							  ArrayList<Casilla> casillas = new ArrayList<Casilla>();
							  for(TituloPropiedad titulo : jugador.propiedades) {
								  casillas.add(titulo.casilla);
							  }
							  casilla = this.elegirPropiedad(casillas);
							  boolean posible = true;
							  switch(opcion) {
							  	case 1: posible = juego.edificarCasa(casilla); break;
							  	case 2: posible = juego.edificarHotel(casilla); break;
							  	case 3: posible = juego.venderPropiedad(casilla); break;
							  	case 4: posible = juego.hipotecarPropiedad(casilla); break;
							  	case 5: posible = juego.cancelarHipoteca(casilla); break;
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
		
		if(!finalizado) {
			juego.siguienteJugador();
			jugador = juego.getJugadorActual();
			casilla = jugador.getCasillaActual();
			if(jugador.estaBancarrota()) {
				finalizado = true;
			}else {
				this.desarrolloJuego();
			}
		}
		
		if(finalizado){
			vista.mostrar("El juego ha acabado, un jugador esta en bancarrota.");
			ArrayList<Map.Entry<String, Integer>> ranking = juego.obtenerRanking();
			for(Map.Entry<String, Integer> person : ranking) {
				vista.mostrar(person.getKey() + " con un capital " + person.getValue());
			}
		}
	};
	
	private Casilla elegirPropiedad(ArrayList<Casilla> propiedades) {
	  ArrayList<String> nombres = new ArrayList<String>();
	  for(Casilla casilla : propiedades) {
		  nombres.add(casilla.getTitulo().getNombre());
	  }
	  int opcion = vista.menuElegirPropiedad(nombres);
	  return propiedades.get(opcion);
	};
	
	public void inicializacionJuego() {
		juego = Qytetet.getInstance();
		ArrayList<String> nombres = vista.obtenerNombreJugadores();
		juego.inicializarJuego(nombres);
		jugador = juego.getJugadorActual();
		casilla = jugador.getCasillaActual();
		vista.mostrar(juego.toString());
		this.desarrolloJuego();
	};
}
