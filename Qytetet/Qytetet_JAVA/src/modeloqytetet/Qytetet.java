package modeloqytetet;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;

public class Qytetet {
	// SINGLETON
	private static final Qytetet singleton = new Qytetet();
    public static Qytetet getInstance() {
        return singleton; 
    };
	
	/* Const */
    public static final int MAX_JUGADORES = 4;
    static final int MAX_CARTAS = 10;
    static final int MAX_CASILLAS = 20;
    static final int PRECIO_LIBERTAD = 200;
    static final int SALDO_SALIDA = 1000;
    
    private ArrayList<Sorpresa> mazo = new ArrayList<Sorpresa>();
    private Tablero tablero;
    private ArrayList<Jugador> jugadores;
    private Jugador jugadorActual;
    private Sorpresa cartaActual;
    
    public void inicializarJuego(ArrayList<String> nombres){
        inicializarJugadores(nombres);
        inicializarTablero();
        inicializarCartasSorpresas();
        salidaJugadores();
    };
    
    private void inicializarCartasSorpresas(){
        // Añadimos las cartas de PAGARCOBRAR (x2)
        mazo.add(new Sorpresa("Has ganado la lotería!", +100, TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa("Has perdido tu cartera en la calle...", -100, TipoSorpresa.PAGARCOBRAR));
        
        // Añadimos las cartas de IRACASILLA (x3)
        mazo.add(new Sorpresa("Te vas a Paseo del prado", 18, TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("Te trasladan a la salida.", 0, TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("¡A la carcel!", tablero.getCarcel().getNumeroCasilla(), TipoSorpresa.IRACASILLA));
        
        // Añadimos las cartas de PORCASAHOTEL (x2)
        mazo.add(new Sorpresa("Debes cobrar por tus propiedades", 10, TipoSorpresa.PORCASAHOTEL));
        mazo.add(new Sorpresa("Debes pagar por tus propiedades", -10, TipoSorpresa.PORCASAHOTEL));
        
        // Añadimos las cartas de PORJUGADOR (x2)
        mazo.add(new Sorpresa("Recibes de los demás jugadores", 20, TipoSorpresa.PORJUGADOR));
        mazo.add(new Sorpresa("Debes darles a los demás jugadores", -5, TipoSorpresa.PORJUGADOR));
        
        // Añadimos las cartas de SALIRCARCEL (x1)
        mazo.add(new Sorpresa("Hola Mundo", 10, TipoSorpresa.SALIRCARCEL));
        
        Collections.shuffle(mazo);
		cartaActual = mazo.get(0);
    };
    
    private void inicializarJugadores(ArrayList<String> nombres){
        jugadores = new ArrayList<Jugador>();
        for(int i = 0; i < nombres.size(); i++){
            jugadores.add(new Jugador(nombres.get(i)));
        }
    };
    
    private void inicializarTablero(){
        tablero = new Tablero();
    };
    
    /*
        GETTER / SETTER
    */
    public Sorpresa getCartaActual(){ return cartaActual; };
    public ArrayList<Jugador> getJugadores(){ return jugadores; };
    public Jugador getJugadorActual(){ return jugadorActual; };
    
    
    public void siguienteJugador(){
        int i = 0; boolean encontrado = false;
        for(; i < jugadores.size() && !encontrado; ++i){
            if(jugadores.get(i).equals(jugadorActual)){
                encontrado = true;
            }
        }
        jugadorActual = jugadores.get(i % jugadores.size());
    };
    
    public void salidaJugadores(){
        for(Jugador jugador : jugadores){
            jugador.setCasillaActual(tablero.obtenerCasillaNumero(0));
            jugador.setEncarcelado(false);
            jugador.modificarSaldo(Qytetet.SALDO_SALIDA);
        }
        Collections.shuffle(jugadores);
        jugadorActual = jugadores.get(0);
    };
    
    ArrayList<Sorpresa> buscarValoresPositivo(){
    	ArrayList<Sorpresa> result = new ArrayList<Sorpresa>();
    	for(Sorpresa sorpresa : mazo) {
    		if(sorpresa.getValor() > 0) {
    			result.add(sorpresa);
    		}
    	}
        return result;
    };
    
    ArrayList<Sorpresa> buscarIrACasillas(){
    	ArrayList<Sorpresa> result = new ArrayList<Sorpresa>();
    	for(Sorpresa sorpresa : mazo) {
    		if(sorpresa.getTipo() == TipoSorpresa.IRACASILLA) {
    			result.add(sorpresa);
    		}
    	}
        return result;
    };
    
    ArrayList<Sorpresa> buscarCasillas(TipoSorpresa tipo){
    	ArrayList<Sorpresa> result = new ArrayList<Sorpresa>();
    	for(Sorpresa sorpresa : mazo) {
    		if(sorpresa.getTipo() == tipo) {
    			result.add(sorpresa);
    		}
    	}
        return result;
    };
    
    public boolean jugar() {
    	int valorDado = Dado.tirar();
    	Casilla casillaPosicion = jugadorActual.getCasillaActual();
    	Casilla nuevaCasilla = tablero.obtenerNuevaCasilla(casillaPosicion, valorDado);
    	jugadorActual.actualizarPosicion(nuevaCasilla);
    	boolean tienePropietario = true;
    	if(nuevaCasilla.getTipo() == TipoCasilla.CALLE) {
	    	tienePropietario = nuevaCasilla.tengoPropietario();
	    	if(!nuevaCasilla.soyEdificable()) {
	    		if(nuevaCasilla.getTipo() == TipoCasilla.JUEZ) {
	    			encarcelarJugador();
	    		}else {
	    			cartaActual = mazo.get(0);
	    		}
	    	}
    	}
    	return tienePropietario;
    };
    
    public ArrayList<Map.Entry<String, Integer>> obtenerRanking(){
    	ArrayList<Map.Entry<String, Integer>> ranking = new ArrayList<Map.Entry<String, Integer>>();
    	for(Jugador jugador : jugadores) {
    		int capital = jugador.obtenerCapital();
    		Map.Entry<String,Integer> pair = new AbstractMap.SimpleEntry<String, Integer>(jugador.getNombre(), capital);
    		ranking.add(pair);
    	}
    	return ranking;
    };
    
    public boolean comprarTituloPropiedad() {
    	boolean puedoComprar = jugadorActual.comprarTitulo();
    	return puedoComprar;
    };
    
    void encarcelarJugador() {
    	if(!jugadorActual.tengoCartaLibertad()) {
    		Casilla casillaCarcel = tablero.getCarcel();
    		jugadorActual.irACarcel(casillaCarcel);
    	}else {
    		Sorpresa carta = jugadorActual.devolverCartaLibertad();
    		mazo.add(carta);
    	}
    };
    
    public boolean intentarSalirCarcel(MetodoSalirCarcel metodo) {
    	boolean libre = false;
    	switch (metodo) {
			case TIRANDODADO:
				int valorDado = Dado.tirar();
				libre = valorDado > 5;
			break;
			case PAGANDOLIBERTAD:
				boolean tengoSaldo = jugadorActual.pagarLibertad(-Qytetet.PRECIO_LIBERTAD);
				libre = tengoSaldo;
			break;
    	}
    	if(libre) {
    		jugadorActual.setEncarcelado(false);
    	}
    	return libre;
    };
    
    public boolean aplicarSorpresa() {
    	boolean tienePropietario = true;
    	switch(cartaActual.getTipo()) {
    		case PAGARCOBRAR:
    			jugadorActual.modificarSaldo(cartaActual.getValor());
    		break;
    		case IRACASILLA:
    			boolean esCarcel = tablero.esCasillaCarcel(cartaActual.getValor());
    			if(esCarcel) {
    				this.encarcelarJugador();
    			}else {
    				Casilla nuevaCasilla = tablero.obtenerCasillaNumero(cartaActual.getValor());
    				jugadorActual.actualizarPosicion(nuevaCasilla);
    				if(nuevaCasilla.getTipo() == TipoCasilla.CALLE) {
    					tienePropietario = nuevaCasilla.estaHipotecada();
    				}
    			}
			break;
    		case PORCASAHOTEL:
    			jugadorActual.pagarCobrarPorCasaYHotel(cartaActual.getValor());
			break;
    		case PORJUGADOR:
    			for(Jugador jugador : jugadores) {
    				if(!jugador.equals(jugadorActual)) {
    					jugadorActual.modificarSaldo(-cartaActual.getValor());
    					jugador.modificarSaldo(cartaActual.getValor());
    				}
    			}
			break;
			default:break;
    	}
    	if(cartaActual.getTipo() == TipoSorpresa.SALIRCARCEL) {
    		jugadorActual.setCartaLibertad(cartaActual);
    	}else {
			mazo.remove(0);
    		mazo.add(cartaActual);
    	}
    	return tienePropietario;
    };
    
    public boolean venderPropiedad(Casilla casilla) {
    	boolean puedoVender = false;
    	if(casilla.soyEdificable()) {
    		puedoVender = jugadorActual.puedoVenderPropiedad(casilla);
    		if(puedoVender) {
    			jugadorActual.venderPropiedad(casilla);
    			
    		}
    	}
    	return puedoVender;
    };
    
    public boolean cancelarHipoteca(Casilla casilla) {
    	return false;
    };
    
    public boolean hipotecarPropiedad(Casilla casilla) {
    	boolean puedoEdificar = false;
    	if(casilla.soyEdificable()) {
    		boolean sePuedeHipotecar = !casilla.estaHipotecada();
    		if(sePuedeHipotecar) {
    			puedoEdificar = jugadorActual.puedoHipotecar(casilla);
    			if(puedoEdificar) {
    				int cantidadRecibida = casilla.hipotecar();
    				jugadorActual.modificarSaldo(cantidadRecibida);
    			}
    		}	
    	}
    	return puedoEdificar;
    };
    
    public boolean edificarCasa(Casilla casilla) {
    	boolean puedoEdificar = false;
    	if(casilla.soyEdificable()) {
    		boolean sePuedeEdificar = casilla.sePuedeEdificarCasa();
    		if(sePuedeEdificar) {
    			puedoEdificar = jugadorActual.puedoEdificarCasa(casilla);
    			if(puedoEdificar) {
    				int costeEdificar = casilla.edificarCasa();
    				jugadorActual.modificarSaldo(-costeEdificar);
    			}
    		}
    	}
    	return puedoEdificar;
    };
    
    public boolean edificarHotel(Casilla casilla) {
    	boolean puedoEdificar = false;
    	if(casilla.soyEdificable()) {
    		boolean sePuedeEdificar = casilla.sePuedeEdificarHotel();
    		if(sePuedeEdificar) {
    			puedoEdificar = jugadorActual.puedoEdificarHotel(casilla);
    			if(puedoEdificar) {
    				int costeEdificar = casilla.edificarHotel();
    				jugadorActual.modificarSaldo(-costeEdificar);
    			}
    		}
    	}
    	return puedoEdificar;
    };
    
    @Override
    public String toString(){
        String info = "";
        
        info += "Cartas sorpresa: \n";
        for(Sorpresa carta : mazo){
            info += carta.toString() + "\n";
        }
        
        info += "Las casillas son las siguientess: \n" + tablero.toString();
        
        info += "Los participantes son: \n";
        for(Jugador jugador : jugadores){
            info += jugador.toString() + "\n";
        }
        
        return info;
    };
}
