package modeloqytetet;

public class Especulador extends Jugador {
	static int FACTOR_ESPECULADOR = 2;
	public int getFactorEspeculador(){ return FACTOR_ESPECULADOR; };
	
	private int fianza = 0;
	protected Especulador(Jugador jugador, int fianza){
		super(jugador);
		this.fianza = fianza;
	};
	
	protected void pagarImpuestos(int cantidad) { this.modificarSaldo(cantidad / 2); };
	
	protected void irACarcel(Casilla casilla) {
		if(!this.pagarFianza(Qytetet.PRECIO_LIBERTAD)) {
			this.setEncarcelado(true);
		}
	};
	
	protected Especulador convertirme(int fianza){
		return this;
	};
	
	private boolean pagarFianza(int cantidad) {
		if(this.tengoSaldo(cantidad)) {
			this.modificarSaldo(cantidad);
			return true;
		}
		return false;
	};
}
