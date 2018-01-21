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
    static final int MAX_CARTAS = 12;
    static final int MAX_CASILLAS = 20;
    static final int PRECIO_LIBERTAD = 200;
    static final int SALDO_SALIDA = 1000;
    
    private ArrayList<Sorpresa> mazo = new ArrayList<Sorpresa>();
    private Tablero tablero;
    private ArrayList<Jugador> jugadores;
    private int jugadorActual;
    public int jugador_actual() { return jugadorActual; }
    private Sorpresa cartaActual;
    
    public void inicializarJuego(ArrayList<String> nombres){
        inicializarJugadores(nombres);
        inicializarTablero();
        inicializarCartasSorpresas();
        salidaJugadores();
    };
    
    private void inicializarCartasSorpresas(){
        // AÒadimos las cartas de PAGARCOBRAR (x2)
        mazo.add(new Sorpresa("Has ganado la loter√≠a!", +100, TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa("Has perdido tu cartera en la calle...", -100, TipoSorpresa.PAGARCOBRAR));
        
        // AÒadimos las cartas de IRACASILLA (x3)
        mazo.add(new Sorpresa("Te vas a Paseo del prado", 18, TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("Te trasladan a la salida.", 0, TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("¬°A la carcel!", tablero.getCarcel().getNumeroCasilla(), TipoSorpresa.IRACASILLA));
        
        // AÒadimos las cartas de PORCASAHOTEL (x2)
        mazo.add(new Sorpresa("Debes cobrar por tus propiedades", 10, TipoSorpresa.PORCASAHOTEL));
        mazo.add(new Sorpresa("Debes pagar por tus propiedades", -10, TipoSorpresa.PORCASAHOTEL));
        
        // AÒadimos las cartas de PORJUGADOR (x2)
        mazo.add(new Sorpresa("Recibes de los dem√°s jugadores", 20, TipoSorpresa.PORJUGADOR));
        mazo.add(new Sorpresa("Debes darles a los dem√°s jugadores", -5, TipoSorpresa.PORJUGADOR));
        
        // AÒadimos las cartas de SALIRCARCEL (x1)
        mazo.add(new Sorpresa("Hola Mundo", 10, TipoSorpresa.SALIRCARCEL));
        
        // AÒadimos las cartas de CONVERTIRME (x2)
        mazo.add(new Sorpresa("Te conviertes en un Especulador", 3000, TipoSorpresa.CONVERTIRME));
        mazo.add(new Sorpresa("Te conviertes en un Especulador", 5000, TipoSorpresa.CONVERTIRME));
        
        
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
    public Jugador getJugadorActual(){ return jugadores.get(jugadorActual); };
    
    
    public void siguienteJugador(){ jugadorActual = (jugadorActual + 1) % jugadores.size(); };
    
    public void salidaJugadores(){
        for(Jugador jugador : jugadores){
            jugador.setCasillaActual(tablero.obtenerCasillaNumero(0));
            jugador.setEncarcelado(false);
            jugador.modificarSaldo(Qytetet.SALDO_SALIDA);
        }
        Collections.shuffle(jugadores);
        jugadorActual = 0;
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
    	int valorDado = Dado.getInstance().tirar();
    	Casilla casillaPosicion = this.getJugadorActual().getCasillaActual();
    	Casilla nuevaCasilla = tablero.obtenerNuevaCasilla(casillaPosicion, valorDado);
    	boolean tienePropietario = this.getJugadorActual().actualizarPosicion(nuevaCasilla);
    	if(nuevaCasilla instanceof OtraCasilla) {
    		switch(((OtraCasilla)nuevaCasilla).getTipo()) {
	    		case JUEZ: encarcelarJugador(); break;
	    		case SORPRESA: cartaActual = mazo.get(0); break;
	    		default: break;
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
    	boolean puedoComprar = this.getJugadorActual().comprarTitulo();
    	return puedoComprar;
    };
    
    void encarcelarJugador() {
    	if(this.getJugadorActual().tengoCartaLibertad()) {
    		Sorpresa carta = this.getJugadorActual().devolverCartaLibertad();
    		mazo.add(carta);
    	}else {
    		Casilla casillaCarcel = tablero.getCarcel();
    		this.getJugadorActual().irACarcel(casillaCarcel);
    	}
    };
    
    public boolean intentarSalirCarcel(MetodoSalirCarcel metodo) {
    	boolean libre = false;
    	switch (metodo) {
			case TIRANDODADO:
				int valorDado = Dado.getInstance().tirar();
				libre = valorDado > 5;
			break;
			case PAGANDOLIBERTAD:
				boolean tengoSaldo = this.getJugadorActual().pagarLibertad(Qytetet.PRECIO_LIBERTAD);
				libre = tengoSaldo;
			break;
    	}
    	if(libre) {
    		this.getJugadorActual().setEncarcelado(false);
    	}
    	return libre;
    };
    
    public boolean aplicarSorpresa() {
    	boolean tienePropietario = true;
    	switch(cartaActual.getTipo()) {
    		case PAGARCOBRAR:
    			this.getJugadorActual().modificarSaldo(cartaActual.getValor());
    		break;
    		case IRACASILLA:
    			boolean esCarcel = tablero.esCasillaCarcel(cartaActual.getValor());
    			if(esCarcel) {
    				this.encarcelarJugador();
    			}else {
    				Casilla nuevaCasilla = tablero.obtenerCasillaNumero(cartaActual.getValor());
    				this.getJugadorActual().actualizarPosicion(nuevaCasilla);
    				if(nuevaCasilla instanceof Calle) {
    					tienePropietario = ((Calle)nuevaCasilla).estaHipotecada();
    				}
    			}
			break;
    		case PORCASAHOTEL:
    			this.getJugadorActual().pagarCobrarPorCasaYHotel(cartaActual.getValor());
			break;
    		case PORJUGADOR:
    			for(Jugador jugador : jugadores) {
    				if(!jugador.equals(this.getJugadorActual())) {
    					this.getJugadorActual().modificarSaldo(cartaActual.getValor());
    					jugador.modificarSaldo(-cartaActual.getValor());
    				}
    			}
			break;
    		case CONVERTIRME:
    			Especulador esp = this.getJugadorActual().convertirme(cartaActual.getValor());
			break;
			default:break;
    	}
    	if(cartaActual.getTipo() == TipoSorpresa.SALIRCARCEL) {
    		this.getJugadorActual().setCartaLibertad(cartaActual);
    	}else {
			mazo.remove(0);
    		mazo.add(cartaActual);
    	}
    	return tienePropietario;
    };
    
    public boolean venderPropiedad(Calle casilla) {
    	boolean puedoVender = false;
		puedoVender = this.getJugadorActual().puedoVenderPropiedad(casilla);
		if(puedoVender) {
			this.getJugadorActual().venderPropiedad(casilla);
		}
    	return puedoVender;
    };
    
    //public boolean cancelarHipoteca(Casilla casilla) { return false; };
    
    public boolean hipotecarPropiedad(Calle casilla) {
    	boolean puedoEdificar = false;
		boolean sePuedeHipotecar = !casilla.estaHipotecada();
		if(sePuedeHipotecar) {
			puedoEdificar = this.getJugadorActual().puedoHipotecar(casilla);
			if(puedoEdificar) {
				int cantidadRecibida = casilla.hipotecar();
				this.getJugadorActual().modificarSaldo(cantidadRecibida);
			}
		}
    	return puedoEdificar;
    };
    
    public boolean edificarCasa(Calle casilla) {
    	boolean puedoEdificar = false;
		boolean sePuedeEdificar = casilla.sePuedeEdificarCasa();
		if(sePuedeEdificar) {
			puedoEdificar = this.getJugadorActual().puedoEdificarCasa(casilla);
			if(puedoEdificar) {
				int costeEdificar = casilla.edificarCasa();
				this.getJugadorActual().modificarSaldo(-costeEdificar);
			}
		}
    	return puedoEdificar;
    };
    
    public boolean edificarHotel(Calle casilla) {
    	boolean puedoEdificar = false;
		boolean sePuedeEdificar = casilla.sePuedeEdificarHotel();
		if(sePuedeEdificar) {
			puedoEdificar = this.getJugadorActual().puedoEdificarHotel(casilla);
			if(puedoEdificar) {
				int costeEdificar = casilla.edificarHotel();
				this.getJugadorActual().modificarSaldo(-costeEdificar);
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
