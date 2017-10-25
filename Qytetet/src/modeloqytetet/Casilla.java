package modeloqytetet;

public class Casilla {
    private int numeroCasilla = 0;
    private int coste;
    private int numHoteles;
    private int numCasas = 0;
    private TipoCasilla tipo;
    private TituloPropiedad titulo;

    
    Casilla(TipoCasilla tipo, int numero){
        this.numeroCasilla   = numero;
        this.numHoteles      = 0;
        this.numCasas        = 0;
        this.tipo            = tipo;
        this.titulo          = null;
        this.coste           = 0;
    };
    
    Casilla(TituloPropiedad titulo, int numero, int coste){
        this.numeroCasilla   = numero;
        this.numHoteles      = 0;
        this.numCasas        = 0;
        this.tipo            = TipoCasilla.CALLE;
        this.titulo          = titulo;
        this.coste           = coste;
    };
    
    /* GETTER / SETTER */
    public TipoCasilla getTipo(){ return tipo; };
    public TituloPropiedad getTitulo(){ return titulo; };
    int getNumeroCasilla(){ return numeroCasilla; };
    //int getCosteHipoteca(){};
    int getCoste(){ return coste; };
    int getNumCasas(){ return numCasas; };
    int getNumHoteles(){ return numHoteles; };
    int getPrecioEdificar(){ return titulo.getPrecioEdificar(); };
    
    void setNumHoteles(int a){ numHoteles = a; };
    void setNumCasas(int a){ numCasas = a; };
    private void setTitulo(TituloPropiedad propiedad){ titulo = propiedad; };
    
    boolean soyEdificable(){ return tipo == TipoCasilla.CALLE; };
    boolean estaHipotecada(){ return titulo.getHipotecada(); };
    boolean tengoPropietario(){ return titulo.tengoPropietario(); };
    
    TituloPropiedad asignarPropietario(Jugador jugador){
        titulo.setPropietario(jugador);
        return titulo;
    };
    //int calcularValorHipoteca(){};
    //int cancelarHipoteca(){};
    //int cobrarAlquiler(){};
    //int edificarCasa(){};
    //int edificarHotel(){};
    //boolean estaHipotecada(){};
    //int hipotecar(){};
    //int precioTotalComprar(){};
    //boolean propietarioEncarcelado(){ return };
    //boolean sePuedeEdificarCasa(){};
    //boolean sePuedeEdificarHotel(){};
    //int venderTitulo(){};
    //void asignarTituloPropiedad(){};

    @Override
    public String toString() {
        return "Casilla{" + "numeroCasilla=" + numeroCasilla + ", coste=" + coste + ", numHoteles=" + numHoteles + ", numCasas=" + numCasas + ", tipo=" + tipo + ", titulo=" + titulo + '}';
    }
}
