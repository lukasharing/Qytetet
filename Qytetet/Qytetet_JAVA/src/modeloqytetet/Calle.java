package modeloqytetet;

public class Calle extends Casilla {
    private int numHoteles;
    private int numCasas;
    private TituloPropiedad titulo;
    
    // Constructor
	Calle(TituloPropiedad titulo, int numero, int coste) {
		super(numero, coste);
		this.numCasas = 0;
		this.numHoteles = 0;
	};
	
	// GETTER / SETTER
    public TituloPropiedad getTitulo(){ return titulo; };
	// Precio
    
    // Edificaciones
    int getNumCasas(){ return numCasas; };
    int getNumHoteles(){ return numHoteles; };
    void setNumHoteles(int a){ numHoteles = a; };
    void setNumCasas(int a){ numCasas = a; };
    int getPrecioEdificar(){ return titulo.getPrecioEdificar(); };
    
    // Título
    private void setTitulo(TituloPropiedad propiedad){ titulo = propiedad; };
    
    // Operaciones de construcción
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
    
    // Operaciones con la carta
    TituloPropiedad asignarPropietario(Jugador jugador){
        titulo.setPropietario(jugador);
        return titulo;
    };
    
    int hipotecar(){
    	titulo.setHipotecada(true);
    	int cantidadRecibida = this.calcularValorHipoteca();
    	return cantidadRecibida;
    };
    
    int venderTitulo(){
    	setNumCasas(0);
    	setNumHoteles(0);
    	titulo.setPropietario(null);
    	int precioCompra = this.getCoste() + this.getPrecioEdificar() * (numCasas + numHoteles);
    	int precioVenta = precioCompra * (int)(1 + titulo.getFactorRevalorizacion());
    	return precioVenta;
    };
    
    int cobrarAlquiler(){
    	int costeAlquilerBase = titulo.getAlquilerBase();
    	int costeAlquiler = costeAlquilerBase + (int)(numCasas * 0.5 + numHoteles * 2);
    	titulo.cobrarAlquiler(costeAlquiler);
    	return costeAlquiler;
    };
    
    // Información
    int calcularValorHipoteca(){
    	int hipotecaBase = titulo.getHipotecaBase();
    	int cantidadRecibida = hipotecaBase * (1 + (int)(numCasas * 0.5 + numHoteles));
    	return cantidadRecibida;
    };
    boolean estaHipotecada(){ return titulo.getHipotecada(); };
    boolean tengoPropietario(){ return titulo.tengoPropietario(); };
    
    boolean propietarioEncarcelado(){ return this.titulo.propietarioEncarcelado(); };
    boolean sePuedeEdificarCasa(){ 
    	int max_casas = 4;
    	if(titulo != null && titulo.getPropietario().getFactorEspeculador() == 2) {
    		max_casas *= 2;
    	}
    	return numCasas <= max_casas;
	};
    boolean sePuedeEdificarHotel(){
    	int max_hoteles = 4;
    	if(titulo != null && titulo.getPropietario().getFactorEspeculador() == 2) {
    		max_hoteles *= 2;
    	}
    	return numHoteles <= max_hoteles;
    };
    
    @Override
    public String toString() {
        String resultado = "La casilla ("+ this.getNumeroCasilla() + ") " + this.getTitulo().getNombre() + " y valorada en " + this.getCoste() + " tiene " + this.numCasas + " casas y " + this.numHoteles + " hoteles.";
        return resultado;
    };
    
    //int cancelarHipoteca(){ return 0; };
    //int precioTotalComprar(){ return 0; };
    //void asignarTituloPropiedad(){};
}
