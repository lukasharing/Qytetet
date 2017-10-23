package modeloqytetet;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author lukasharing
 * @version 0.0
 * @since 27/09/17
 */
public class Qytetet {
    /* Const */
    private static int MAX_JUGADORES = 4;
    private static int MAX_CARTAS = 10;
    private static int MAX_CASILLAS = 20;
    private static int PRECIO_LIBERTAD = 200;
    private static int SALDO_SALIDA = 1000;
    
    
    private static final ArrayList<Sorpresa> MAZO = new ArrayList();
    private static final Tablero TABLERO = new Tablero();
    private ArrayList<Jugador> jugadores;
    private Jugador jugadorActual;
    public static void main(String[] args) {
        inicializarSorpresas();
        for(int i = 0; i < MAZO.size(); i++){
            System.out.printf(MAZO.get(i).toString() + "\n");
        }
        System.out.printf(TABLERO.toString());
    }
    
    public void siguienteJugador(){
        jugadorActual = jugadores.get((jugadorActual.getIndice() + 1) % jugadores.size());
    };
    
    public void salidaJugadores(){
        for(Jugador jugador : jugadores){
            jugador.setCasilla(0);
            jugador.setSaldoActual(7500);
        }
        
        for(Jugador jugador : jugadores){
            jugador.setTurno();
        }
    };
    
    ArrayList<Sorpresa> buscarValoresPositivo(){
        return MAZO.stream().filter(t -> t.getValor() > 0).collect(Collectors.toCollection(ArrayList::new));
    };
    
    ArrayList<Sorpresa> buscarIrACasillas(){
        return MAZO.stream().filter(t -> t.getTipoSorpresa() == TipoSorpresa.IRACASILLA).collect(Collectors.toCollection(ArrayList::new));
    };
    
    ArrayList<Sorpresa> buscarCasillas(TipoSorpresa tipo){
        return MAZO.stream().filter(t -> t.getTipoSorpresa() == tipo).collect(Collectors.toCollection(ArrayList::new));
    };
    
    private void inicializarSorpresas(){
        // Añadimos las cartas de PAGARCOBRAR (x2)
        MAZO.add(new Sorpresa("Has ganado la lotería!", +100, TipoSorpresa.PAGARCOBRAR));
        MAZO.add(new Sorpresa("Has perdido tu cartera en la calle...", -100, TipoSorpresa.PAGARCOBRAR));
        
        // Añadimos las cartas de IRACASILLA (x3)
        MAZO.add(new Sorpresa("Te vas a Paseo del prado", 18, TipoSorpresa.IRACASILLA));
        MAZO.add(new Sorpresa("Te trasladan a la salida.", 0, TipoSorpresa.IRACASILLA));
        MAZO.add(new Sorpresa("¡A la carcel!", TABLERO.getCarcel().getNumeroCasilla(), TipoSorpresa.IRACASILLA));
        
        // Añadimos las cartas de PORCASAHOTEL (x2)
        MAZO.add(new Sorpresa("Debes cobrar por tus propiedades", 10, TipoSorpresa.PORCASAHOTEL));
        MAZO.add(new Sorpresa("Debes pagar por tus propiedades", -10, TipoSorpresa.PORCASAHOTEL));
        
        // Añadimos las cartas de PORJUGADOR (x2)
        MAZO.add(new Sorpresa("Recibes de los demás jugadores", 20, TipoSorpresa.PORJUGADOR));
        MAZO.add(new Sorpresa("Debes darles a los demás jugadores", -5, TipoSorpresa.PORJUGADOR));
        
        // Añadimos las cartas de SALIRCARCEL (x1)
        MAZO.add(new Sorpresa("Hola Mundo", 10, TipoSorpresa.SALIRCARCEL));
    };
    
    private void inicializarJugadores(ArrayList<String> nombres){
        
    };
}
