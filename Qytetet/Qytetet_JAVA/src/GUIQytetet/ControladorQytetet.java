package GUIQytetet;
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
		String rs = "Es el turno de " + jugador.getNombre() + ", actualmente se encuentra en: \n" + casilla.toString();
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
	
	private Calle elegirPropiedad(ArrayList<TituloPropiedad> propiedades) {
	  ArrayList<String> nombres = new ArrayList<String>();
	  for(TituloPropiedad titulo : propiedades) {
		  nombres.add(titulo.getNombre());
	  }
	  int opcion = vista.menuElegirPropiedad(nombres);
	  return propiedades.get(opcion).casilla;
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
