package modeloqytetet;

/*
 * El precio de edificación debe estar entre 250 y 750€
 * El factor de revalorización de la venta estará entre el 10 y el 20% y puede ser positivo si la
 * vivienda se revaloriza o negativo si se devalúa.
 * El precio base del alquiler debe estar entre 50 y 100€
 * El precio base de la hipoteca debe estar entre 150 y 1000€
 */

public class TituloPropiedad {
    private Jugador propietario = null;
    private Casilla casilla;
    
    private final   String nombre;
    private boolean hipotecada;
    private int     alquilerBase;
    private float   factorRevalorizacion;
    private int     hipotecaBase;
    private int     precioEdificar;
    /**
     * @param {String}[ Nombre de la propiedad ]
     * @param {int}   [ Valor del alquiler base]
     * @param {float} [ Valor de la revalorización ]
     * @param {float} [ Valor de la hipoteca base ]
     * @param {float} [ Valor de la edificación ]
     * @see {Constructor de creación de las cartas Sorpresas}
    */
    public TituloPropiedad(String nombre, int alquilerBase, float factorRevalorizacion, int hipotecaBase, int precioEdificar){
        this.nombre                 = nombre;
        this.hipotecada             = false;
        this.alquilerBase           = alquilerBase;
        this.factorRevalorizacion   = factorRevalorizacion;
        this.hipotecaBase           = hipotecaBase;
        this.precioEdificar         = precioEdificar;
    };
    
    /* Getter */
    String getNombre(){ return nombre; };
    boolean getHipotecada(){ return hipotecada; };
    int getAlquilerBase(){ return alquilerBase; };
    float getFactorRevalorizacion(){ return factorRevalorizacion; };
    int getHipotecaBase(){ return hipotecaBase; };
    int getPrecioEdificar(){ return precioEdificar; };
    
    void setHipotecada(boolean hipotecada){ this.hipotecada = hipotecada; };
    void setPropietario(Jugador propietario){ this.propietario = propietario; };
    void setCasilla(Casilla casilla){ this.casilla = casilla; };
    
    boolean tengoPropietario(){ return propietario != null; };
    //void cobrarAlquiler(int coste){ return propietario != null; };
    boolean propietarioEncarcelado(){ return propietario.getEncarcelado(); };
    
    @Override
    public String toString() {
        return "TituloPropiedad{" + "nombre=" + nombre + ", hipotecada=" + hipotecada + ", alquilerBase=" + alquilerBase + ", factorRevalorizacion=" + factorRevalorizacion + ", hipotecaBase=" + hipotecaBase + ", precioEdificar=" + precioEdificar + '}';
    };
}
