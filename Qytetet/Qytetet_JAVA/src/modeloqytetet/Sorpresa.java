package modeloqytetet;

public class Sorpresa {
    private final String        texto;
    private final TipoSorpresa  tipo;
    private final int           valor;
    /**
     * @param {String}      [ Nombre de la carta ]
     * @param {int}         [ Depende de TipoSorpresa ]
     * @param {TipoSorpresa}[ Tipo de sorpresa que te puede tocar ]
     * @see {Constructor de creación de las cartas Sorpresas}
    */
    public Sorpresa(String texto, int valor, TipoSorpresa tipo){
        this.texto  = texto;
        this.valor  = valor;
        this.tipo   = tipo;
    }
    
    public String getDescripcion(){ return texto; };
    public TipoSorpresa getTipo(){ return tipo; };
    public int getValor(){ return valor; };
    @Override
    public String toString() {
    	return "Existe una sorpresa de tipo " + this.tipo + " con un efecto de " + this.valor;
	}
}
