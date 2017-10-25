package modeloqytetet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Qytetet {
    /* Const */
    public static final int MAX_JUGADORES = 4;
    static final int MAX_CARTAS = 10;
    static final int MAX_CASILLAS = 20;
    static final int PRECIO_LIBERTAD = 200;
    static final int SALDO_SALIDA = 1000;
    
    
    private ArrayList<Sorpresa> mazo = new ArrayList();
    private Tablero tablero;
    private ArrayList<Jugador> jugadores;
    private Jugador jugadorActual;
    private Sorpresa cartaActual;
    
    public Qytetet(){
        ArrayList<String> nombres = new ArrayList<String>();
        nombres.add("Lukas");
        nombres.add("Ana");
        nombres.add("Alberto");
        nombres.add("María");
        
        inicializarTablero();
        inicializarSorpresas();
        inicializarJugadores(nombres);
        salidaJugadores();
    };
    
    private void inicializarSorpresas(){
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
        for(; i < jugadores.size() && !encontrado; i++){
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
            jugador.modificarSaldo(7500);
        }
        Collections.shuffle(jugadores);
        jugadorActual = jugadores.get(0);
    };
    
    ArrayList<Sorpresa> buscarValoresPositivo(){
        return mazo.stream().filter(t -> t.getValor() > 0).collect(Collectors.toCollection(ArrayList::new));
    };
    
    ArrayList<Sorpresa> buscarIrACasillas(){
        return mazo.stream().filter(t -> t.getTipo() == TipoSorpresa.IRACASILLA).collect(Collectors.toCollection(ArrayList::new));
    };
    
    ArrayList<Sorpresa> buscarCasillas(TipoSorpresa tipo){
        return mazo.stream().filter(t -> t.getTipo() == tipo).collect(Collectors.toCollection(ArrayList::new));
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
