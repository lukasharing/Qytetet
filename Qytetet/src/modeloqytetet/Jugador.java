package modeloqytetet;

import java.util.ArrayList;

public class Jugador {
    private ArrayList <TituloPropiedad> propiedades;
    private Casilla casillaActual;
    private boolean encarcelado = false;
    private String nombre;
    private int saldo = 7500;
    
    public Casilla getCasillaActual(){ return casillaActual; };
    public boolean getEncarcelado(){ return encarcelado; };
    public boolean tengoPropiedades(){ return (propiedades.size() > 0); };
    boolean actualizarPosicion(Casilla casilla){ 
        if(casilla.getNumeroCasilla() != casilla.getNumeroCasilla()){
            casillaActual = casilla; 
            return true;
        }else{
            return false;
        } 
    };
    boolean comprarTitulo(){
        propiedades.add(casillaActual.getTituloPropiedad());
        casillaActual.getTituloPropiedad().setHipotecada(true);
    };
    Sorpresa devolverCartaLibertad(){
    
    };
    void irACarcel(Casilla casilla){
    
    };
    void modificarSaldo(int cantidad){

    };
    int obtenerCapital(){};
    ArrayList<TituloPropiedad> obtenerPropiedadesHipotecadas(boolean hipotecada){
    
    };
    void pagarCobrarPorCasaYHotel(int cantidad){

    };
    boolean pagarLibertad(int cantidad){
    
    };
    boolean puedoEdificarCasa(Casilla casilla){
    
    };
    boolean puedoEdificarHotel(Casilla casilla){
    
    };
    boolean puedoHipotecar(Casilla casilla){
    
    };
    boolean puedoPagarHipoteca(Casilla casilla){
        
    };
    boolean puedoVenderPropiedad(Casilla casilla){
        
    };
    void setCartaLibertad(Sorpresa carta){
    
    };
    void setCasillaActual(Casilla casilla){
    
    };
    void setEncarcelado(boolean encarcelado){
    
    };
    boolean tengoCartaLibertad(){
    
    };
    void venderPropiedad(Casilla casilla){
    
    };
    private int cuantasCasasHotelesTengo(){};
    private void eliminarDeMisPropiedades(Casilla casilla){}
    private boolean esDeMipropiedad(Casilla casilla){}
    private boolean tengoSaldo(int cantidad){}
}
