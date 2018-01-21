package modeloqytetet;

public class OtraCasilla extends Casilla {
    private TipoCasilla tipo;
    
	OtraCasilla(TipoCasilla tipo, int numero, int coste) {
		super(numero, coste);
		this.tipo = tipo;
	}
	
	// GETTER/SETTER
	public TipoCasilla getTipo(){ return tipo; };
    
	
	public String toString() {
        String resultado = "La casilla ("+ this.getNumeroCasilla() + ") es de tipo " + this.tipo;
        return resultado;
    };
}
