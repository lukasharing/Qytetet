package modeloqytetet;

import java.util.ArrayList;

public class Jugador {
	static int FACTOR_ESPECULADOR = 1;
	public int getFactorEspeculador(){ return FACTOR_ESPECULADOR; };
	
	
    public ArrayList <TituloPropiedad> propiedades;
    private Casilla casillaActual;
    private Sorpresa cartaLibertad;
    
    private boolean encarcelado = false;
    private String nombre;
    private int saldo = 0;
    public int getSaldo() { return saldo; };
    
    public Jugador(String nombre){
        this.nombre = nombre;
        this.propiedades = new ArrayList<TituloPropiedad>();
        this.cartaLibertad = null;
    };
    
    protected Jugador(Jugador jugador) {
    	this.saldo = jugador.saldo;
    	this.cartaLibertad = jugador.cartaLibertad;
    	this.casillaActual = jugador.casillaActual;
    	this.propiedades = jugador.propiedades;
    	this.nombre = jugador.nombre;
    	this.saldo = jugador.saldo;
    	this.encarcelado = jugador.encarcelado;
    };
    
    protected Especulador convertirme(int fianza) {
    	return new Especulador(this, fianza);
    };
    
    protected void pagarImpuestos(int cantidad) { this.modificarSaldo(cantidad); };
    
    public Casilla getCasillaActual(){ return casillaActual; };
    public boolean getEncarcelado(){ return encarcelado; };
    public String getNombre() { return nombre; };
    public boolean tengoPropiedades(){ return (this.propiedades.size() > 0); };

    
    protected boolean actualizarPosicion(Casilla casilla){
        boolean tienePropietario = true;
    	if(casilla.getNumeroCasilla() < casillaActual.getNumeroCasilla()){
        	this.modificarSaldo(Qytetet.SALDO_SALIDA);
        }
        this.setCasillaActual(casilla);
        if(casilla instanceof Calle) {
        	boolean tengoPropietario = ((Calle)casilla).tengoPropietario();
        	tienePropietario = tengoPropietario;
        	if(tengoPropietario) {
        		boolean encarcelado = ((Calle)casilla).propietarioEncarcelado();
        		if(!encarcelado) {
        			int costeAlquiler = ((Calle)casilla).cobrarAlquiler();
        			this.modificarSaldo(-costeAlquiler);
        		}
        	}
        }else if(((OtraCasilla)(casilla)).getTipo() == TipoCasilla.IMPUESTO) {
        	this.pagarImpuestos(-casilla.getCoste());
        }
        return tienePropietario;
    };
    
    public boolean estaBancarrota() { return saldo <= 0; }
    
    boolean comprarTitulo(){
    	boolean puedoComprar = false;
    	if(casillaActual instanceof Calle) {
    		boolean tengoPropietario = ((Calle)casillaActual).tengoPropietario();
    		if(!tengoPropietario) {
    			int costeCompra = casillaActual.getCoste();
    			if(costeCompra <= saldo) {
    				TituloPropiedad titulo = ((Calle)casillaActual).asignarPropietario(this);
    				propiedades.add(titulo);
    				puedoComprar = true;
    				this.modificarSaldo(-costeCompra);
    			}
    		}
    	}
    	return puedoComprar;
	};
    Sorpresa devolverCartaLibertad(){
        Sorpresa carta = cartaLibertad;
        cartaLibertad = null;
        return carta;
    };
    void irACarcel(Casilla casilla){
        setCasillaActual(casilla);
        setEncarcelado(true);
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
    
    void pagarCobrarPorCasaYHotel(int cantidad){
    	int numeroTotal = cuantasCasasHotelesTengo();
    	this.modificarSaldo(-numeroTotal * cantidad);
    };
    
    boolean pagarLibertad(int cantidad){
    	boolean tengoSaldo = this.tengoSaldo(cantidad);
    	if(tengoSaldo) {
    		this.modificarSaldo(-cantidad);
    	}
    	return tengoSaldo;
    };
    boolean puedoEdificarCasa(Calle casilla){
    	boolean esMia = this.esDeMipropiedad(casilla);
    	if(!esMia) {
    		int costeEdificarCasa = casilla.getPrecioEdificar();
    		boolean tengoSaldo = tengoSaldo(costeEdificarCasa);
    		return tengoSaldo;
    	}
    	return false;
    };
    
    boolean puedoEdificarHotel(Calle casilla){
    	boolean esMia = this.esDeMipropiedad(casilla);
    	if(!esMia) {
    		int costeEdificarHotel= casilla.getPrecioEdificar();
    		boolean tengoSaldo = tengoSaldo(costeEdificarHotel);
    		return tengoSaldo;
    	}
    	return false;
    };
    
    boolean puedoHipotecar(Calle casilla){
    	return this.esDeMipropiedad(casilla);
    };
    boolean puedoPagarHipoteca(Casilla casilla){
    	// No se especifica NADA
    	return true;
    };
    boolean puedoVenderPropiedad(Calle casilla){
    	boolean esMia = esDeMipropiedad(casilla);
    	boolean hipotecada = casilla.estaHipotecada();
    	boolean puedoVender = esMia && !hipotecada;
    	return puedoVender;
    };
    void setCartaLibertad(Sorpresa carta){ cartaLibertad = carta; };
    void setCasillaActual(Casilla casilla){ casillaActual = casilla; };
    void setEncarcelado(boolean encarcelado){ this.encarcelado = encarcelado; };
    boolean tengoCartaLibertad(){ return cartaLibertad != null; };
    
    void venderPropiedad(Calle casilla){
    	int precioVenta = casilla.venderTitulo();
    	this.modificarSaldo(precioVenta);
    	this.eliminarDeMisPropiedades(casilla);
    };
    
    private int cuantasCasasHotelesTengo(){
        int total = 0;
        for(TituloPropiedad propiedad : propiedades){
            total += propiedad.casilla.getNumHoteles() + propiedad.casilla.getNumCasas();
        }
        return total;
    };
    private void eliminarDeMisPropiedades(Calle casilla){
        int k = 0; boolean econtrado = false;
        for(; k < propiedades.size() && !econtrado; ++k){
            if(propiedades.get(k).equals(casilla.getTitulo())){
                econtrado = true;
            }
        }
        propiedades.remove(k - 1);
    };
    private boolean esDeMipropiedad(Calle casilla){
        boolean esMia = propiedades.contains(casilla.getTitulo());
        return esMia;
    };
    protected boolean tengoSaldo(int cantidad){ return (saldo >= cantidad); };
    
    @Override
    public String toString(){
        return "El jugador " + this.nombre + " se encuentra en la casilla " + this.casillaActual.getNumeroCasilla() + " con saldo " + this.saldo + ", este " + (this.encarcelado ? "" : "no ") + " se encuentra encarcelado";
    }
}
