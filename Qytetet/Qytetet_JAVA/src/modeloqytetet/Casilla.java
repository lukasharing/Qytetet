package modeloqytetet;

public class Casilla {
    private int coste;
    private int numeroCasilla = 0;
    
    Casilla(int numero, int coste){
        this.numeroCasilla = numero;
        this.coste = coste;
    };
    
    /* GETTER / SETTER */
    int getCoste(){ return coste; };
    int getNumeroCasilla(){ return numeroCasilla; };
    //int getCosteHipoteca(){};
}
