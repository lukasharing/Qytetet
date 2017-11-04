package modeloqytetet;

import java.util.ArrayList;

public class Jugador {
    private ArrayList <TituloPropiedad> propiedades;
    private Casilla casillaActual;
    private Sorpresa cartaLibertad;
    
    private boolean encarcelado = false;
    private String nombre;
    private int saldo = 0;
    
    public Jugador(String nombre){
        this.nombre = nombre;
    };
    
    public Casilla getCasillaActual(){ return casillaActual; };
    public boolean getEncarcelado(){ return encarcelado; };
    public boolean tengoPropiedades(){ return (propiedades.size() > 0); };
    boolean actualizarPosicion(Casilla casilla){ 
        if(casilla.getNumeroCasilla() != casillaActual.getNumeroCasilla()){
            casillaActual = casilla; 
            return true;
        }else{
            return false;
        } 
    };
    boolean comprarTitulo(){ return !casillaActual.tengoPropietario(); };
    Sorpresa devolverCartaLibertad(){
        Sorpresa carta = cartaLibertad;
        cartaLibertad = null;
        return carta;
    };
    void irACarcel(Casilla casilla){
        casillaActual = casilla;
        encarcelado = true;
    };
    void modificarSaldo(int cantidad){ saldo += cantidad; };
    int obtenerCapital(){
        int valorpropiedades = 0;
        for(TituloPropiedad propiedad : propiedades){
            int edificios = propiedad.casilla.getNumCasas() + propiedad.casilla.getNumHoteles();
            valorpropiedades += propiedad.casilla.getCoste() + edificios * propiedad.getPrecioEdificar();
            if(propiedad.getHipotecada()){
                valorpropiedades -= propiedad.getHipotecaBase();
            }
        }
        return saldo + valorpropiedades;
    };
    ArrayList<TituloPropiedad> obtenerPropiedadesHipotecadas(boolean hipotecada){
        ArrayList<TituloPropiedad> hipotecadas = new ArrayList<TituloPropiedad>();
        for(TituloPropiedad propiedad : propiedades){
            if(propiedad.getHipotecada() == hipotecada){
                hipotecadas.add(propiedad);
            }
        }
        return hipotecadas;
    };
    //void pagarCobrarPorCasaYHotel(int cantidad){};
    //boolean pagarLibertad(int cantidad){};
    //boolean puedoEdificarCasa(Casilla casilla){};
    //boolean puedoEdificarHotel(Casilla casilla){};
    //boolean puedoHipotecar(Casilla casilla){};
    //boolean puedoPagarHipoteca(Casilla casilla){};
    boolean puedoVenderPropiedad(Casilla casilla){
        return esDeMipropiedad(casilla);
    };
    void setCartaLibertad(Sorpresa carta){ cartaLibertad = carta; };
    void setCasillaActual(Casilla casilla){ casillaActual = casilla; };
    void setEncarcelado(boolean encarcelado){ this.encarcelado = encarcelado; };
    boolean tengoCartaLibertad(){ return cartaLibertad != null; };
    void venderPropiedad(Casilla casilla){
        
    };
    private int cuantasCasasHotelesTengo(){
        int total = 0;
        for(TituloPropiedad propiedad : propiedades){
            total += propiedad.casilla.getNumHoteles() + propiedad.casilla.getNumCasas();
        }
        return total;
    };
    private void eliminarDeMisPropiedades(Casilla casilla){
        int k = 0; boolean econtrado = false;
        for(; k < propiedades.size() && !econtrado; k++){
            if(propiedades.get(k).equals(casilla.getTitulo())){
                econtrado = true;
            }
        }
        propiedades.remove(k - 1);
    };
    private boolean esDeMipropiedad(Casilla casilla){
        boolean esmio = false;
        for(TituloPropiedad propiedad : propiedades){
            if(propiedad.equals(casilla.getTitulo())){
                esmio = true;
            }
        }
        return esmio;
    };
    private boolean tengoSaldo(int cantidad){ return (saldo >= cantidad); };
    
    @Override
    public String toString(){
        return "El jugador: " + nombre + " con saldo " + saldo + " " + (encarcelado ? "" : "no ") + "se encuentra encarcelado";
    }
}
