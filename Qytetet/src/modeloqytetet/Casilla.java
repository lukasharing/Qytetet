package modeloqytetet;

public class Casilla {
    private final int numeroCasilla;
    private final int coste;
    private int numHoteles;
    private int numCasas;
    private final TipoCasilla tipo;
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
    
    public boolean soyEdificable(){ return tipo == TipoCasilla.CALLE; };
    public boolean estaHipotecada(){ return titulo.getHipotecada(); };
    
    TituloPropiedad asignarPropietario(Jugador jugador){};
    int calcularValorHipoteca(){};
    int cancelarHipoteca(){};
    int cobrarAlquiler(){};
    int edificarCasa(){};
    int edificarHotel(){};
    boolean estaHipotecada(){};
    int hipotecar(){};
    int precioTotalComprar(){};
    boolean propietarioEncarcelado(){ return  };
    ~sePuedeEdificarCasa() : boolean{};
    ~sePuedeEdificarHotel() : boolean{};
    ~setNumCasas(nuevoNumero : int) : void{};
    ~setNumHoteles(nuevoNumero : int) : void{};
    ~soyEdificable() : boolean{};
    ~tengoPropietario() : boolean{};
    ~venderTitulo() : int{};
    -setTitulo(titulo : TituloPropiedad) : void{};
    -asignarTituloPropiedad() : void{};

    @Override
    public String toString() {
        return "Casilla{" + "numeroCasilla=" + numeroCasilla + ", coste=" + coste + ", numHoteles=" + numHoteles + ", numCasas=" + numCasas + ", tipo=" + tipo + ", titulo=" + titulo + '}';
    }
}
