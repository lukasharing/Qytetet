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
        titulo.setCasilla(this);
        this.coste           = coste;
    };
    
    /* GETTER / SETTER */
    public TipoCasilla getTipo(){ return tipo; };
    public TituloPropiedad getTitulo(){ return titulo; };
    public int getNumeroCasilla(){ return numeroCasilla; };
    //int getCosteHipoteca(){};
    int getCoste(){ return coste; };
    int getNumCasas(){ return numCasas; };
    int getNumHoteles(){ return numHoteles; };
    int getPrecioEdificar(){ return titulo.getPrecioEdificar(); };
    
    void setNumHoteles(int a){ numHoteles = a; };
    void setNumCasas(int a){ numCasas = a; };
    private void setTitulo(TituloPropiedad propiedad){ titulo = propiedad; };
    
    boolean soyEdificable(){ return this.tipo == TipoCasilla.CALLE; };
    boolean estaHipotecada(){ return titulo.getHipotecada(); };
    boolean tengoPropietario(){ return titulo.tengoPropietario(); };
    
    TituloPropiedad asignarPropietario(Jugador jugador){
        titulo.setPropietario(jugador);
        return titulo;
    };
    int calcularValorHipoteca(){
    	int hipotecaBase = titulo.getHipotecaBase();
    	int cantidadRecibida = hipotecaBase * (1 + (int)(numCasas * 0.5 + numHoteles));
    	return cantidadRecibida;
    };
    //int cancelarHipoteca(){};
    int cobrarAlquiler(){
    	int costeAlquilerBase = titulo.getAlquilerBase();
    	int costeAlquiler = costeAlquilerBase + (int)(numCasas * 0.5 + numHoteles * 2);
    	titulo.cobrarAlquiler(costeAlquiler);
    	return 1;
    };
    int edificarCasa(){
    	this.setNumCasas(numCasas + 1);
    	int costeEdificarCasa = this.getPrecioEdificar();
    	return costeEdificarCasa;
    };
    int edificarHotel(){
    	this.setNumHoteles(numHoteles + 1);
    	int costeEdificarHotel = this.getPrecioEdificar();
    	return costeEdificarHotel;
    };
    int hipotecar(){
    	titulo.setHipotecada(true);
    	int cantidadRecibida = this.calcularValorHipoteca();
    	return cantidadRecibida;
    };
    //int precioTotalComprar(){};
    boolean propietarioEncarcelado(){ return true; };
    boolean sePuedeEdificarCasa(){ return numCasas < 4; };
    boolean sePuedeEdificarHotel(){ return numHoteles < 4; };
    
    int venderTitulo(){
    	setNumCasas(0);
    	setNumHoteles(0);
    	titulo.setPropietario(null);
    	int precioCompra = coste + this.getPrecioEdificar() * (numCasas + numHoteles);
    	int precioVenta = precioCompra * (int)(1 + titulo.getFactorRevalorizacion());
    	return precioVenta;
    };
    //void asignarTituloPropiedad(){};

    @Override
    public String toString() {
        String resultado = "La casilla ("+ this.numeroCasilla + ") ";
        if(this.tipo == TipoCasilla.CALLE) {
        	resultado += this.getTitulo().getNombre() + " y valorada en " + this.coste + " tiene " + this.numCasas + " casas y " + this.numHoteles + " hoteles.";
        }else {
        	resultado += "es de tipo " + this.tipo;
        }
        return resultado;
    }
}
