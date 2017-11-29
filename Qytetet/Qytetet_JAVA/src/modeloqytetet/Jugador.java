package modeloqytetet;

import java.util.ArrayList;

public class Jugador {
    public ArrayList <TituloPropiedad> propiedades;
    private Casilla casillaActual;
    private Sorpresa cartaLibertad;
    
    private boolean encarcelado = false;
    private String nombre;
    private int saldo = 0;
    
    public Jugador(String nombre){
        this.nombre = nombre;
        this.propiedades = new ArrayList<TituloPropiedad>();
        this.cartaLibertad = null;
    };
    
    public Casilla getCasillaActual(){ return casillaActual; };
    public boolean getEncarcelado(){ return encarcelado; };
    public String getNombre() { return nombre; };
    public boolean tengoPropiedades(){ return (this.propiedades.size() > 0); };

    
    boolean actualizarPosicion(Casilla casilla){
        boolean tienePropietario = false;
    	if(casilla.getNumeroCasilla() < casillaActual.getNumeroCasilla()){
        	this.modificarSaldo(Qytetet.SALDO_SALIDA);
        }
        this.setCasillaActual(casilla);
        if(casilla.getTipo() == TipoCasilla.CALLE && casilla.soyEdificable()) {
        	boolean tengoPropietario = casilla.tengoPropietario();
        	tienePropietario = tengoPropietario;
        	if(tengoPropietario) {
        		boolean encarcelado = casilla.propietarioEncarcelado();
        		if(!encarcelado) {
        			int costeAlquiler = casilla.cobrarAlquiler();
        			this.modificarSaldo(-costeAlquiler);
        		}
        	}
        }else if(casilla.getTipo() == TipoCasilla.IMPUESTO) {
        	int coste = casilla.getCoste();
        	this.modificarSaldo(-coste);
        }
        return tienePropietario;
    };
    
    public boolean estaBancarrota() { return saldo <= 0; }
    
    boolean comprarTitulo(){
    	boolean puedoComprar = false;
    	if(casillaActual.soyEdificable()) {
    		boolean tengoPropietario = casillaActual.tengoPropietario();
    		if(!tengoPropietario) {
    			int costeCompra = casillaActual.getCoste();
    			if(costeCompra <= saldo) {
    				TituloPropiedad titulo = casillaActual.asignarPropietario(this);
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
    boolean puedoEdificarCasa(Casilla casilla){
    	boolean esMia = this.esDeMipropiedad(casilla);
    	if(esMia) {
    		int costeEdificarCasa = casilla.getPrecioEdificar();
    		boolean tengoSaldo = tengoSaldo(-costeEdificarCasa);
    		return tengoSaldo;
    	}
    	return false;
    };
    
    boolean puedoEdificarHotel(Casilla casilla){
    	boolean esMia = this.esDeMipropiedad(casilla);
    	if(esMia) {
    		int costeEdificarHotel= casilla.getPrecioEdificar();
    		boolean tengoSaldo = tengoSaldo(costeEdificarHotel);
    		return tengoSaldo;
    	}
    	return false;
    };
    
    boolean puedoHipotecar(Casilla casilla){
    	boolean esMia = this.esDeMipropiedad(casilla);
    	return esMia;
    };
    //boolean puedoPagarHipoteca(Casilla casilla){};
    boolean puedoVenderPropiedad(Casilla casilla){
    	boolean esMia = esDeMipropiedad(casilla);
    	boolean hipotecada = casilla.estaHipotecada();
    	boolean puedoVender = esMia && !hipotecada;
    	return puedoVender;
    };
    void setCartaLibertad(Sorpresa carta){ cartaLibertad = carta; };
    void setCasillaActual(Casilla casilla){ casillaActual = casilla; };
    void setEncarcelado(boolean encarcelado){ this.encarcelado = encarcelado; };
    boolean tengoCartaLibertad(){ return cartaLibertad != null; };
    
    void venderPropiedad(Casilla casilla){
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
        boolean esMia = propiedades.contains(casilla.getTitulo());
        return esMia;
    };
    private boolean tengoSaldo(int cantidad){ return (saldo >= cantidad); };
    
    @Override
    public String toString(){
        return "El jugador " + this.nombre + " se encuentra en la casilla " + this.casillaActual.getNumeroCasilla() + " con saldo " + this.saldo + ", este " + (this.encarcelado ? "" : "no ") + " se encuentra encarcelado";
    }
}
