package modeloqytetet;

public class Casilla {
	private int coste;
	private int numeroCasilla = 0;
	
	Casilla(int numero, int coste){
	    this.numeroCasilla = numero;
	    this.coste = coste;
	};

/* GETTER / SETTER */
    public int getCoste(){ return coste; };
    public int getNumeroCasilla(){ return numeroCasilla; };
    
    @Override
    public String toString() { return "Numero Casilla " + this.numeroCasilla; };
    //int getCosteHipoteca(){};
}
