package modeloqytetet;
import java.util.ArrayList;

public class Tablero {
    private ArrayList<Casilla> casillas;
    private Casilla carcel;
    
    Tablero(){
        inicializar();
    }
    
    private void inicializar(){
        /* Estas calles existen en madrid. */
        casillas = new ArrayList<Casilla>();
        casillas.add(new Casilla(TipoCasilla.SALIDA, 0));
        TituloPropiedad casilla0 = new TituloPropiedad("Av. del pan duro", 50, 10f, 150, 250);
        casillas.add(new Casilla(casilla0, 1, 100)); 
        TituloPropiedad casilla1 = new TituloPropiedad("Calle de la magdalena", 54, 10.8f, 220, 290);
        casillas.add(new Casilla(casilla1, 2, 150));
        TituloPropiedad casilla2 = new TituloPropiedad("Calle de pizza con piña", 58, 11.6f, 290, 330);
        casillas.add(new Casilla(casilla2, 3, 200));
        casillas.add(new Casilla(TipoCasilla.SORPRESA, 4));
        casillas.add(new Casilla(TipoCasilla.JUEZ, 5));
        TituloPropiedad casilla3 = new TituloPropiedad("Glorieta dulce de leche", 62, 12.4f, 360, 370);
        casillas.add(new Casilla(casilla3, 6, 250)); 
        TituloPropiedad casilla4 = new TituloPropiedad("Calle de la Coca-Cola", 66, 13.2f, 430, 410);
        casillas.add(new Casilla(casilla4, 7, 300));
        casillas.add(new Casilla(TipoCasilla.PARKING, 8));
        TituloPropiedad casilla5 = new TituloPropiedad("Calle gran postre", 70, 14f, 500, 450);
        casillas.add(new Casilla(casilla5, 9, 350));
        casillas.add(carcel = new Casilla(TipoCasilla.CARCEL, 10));
        casillas.add(new Casilla(TipoCasilla.SORPRESA, 11));
        TituloPropiedad casilla6 = new TituloPropiedad("Calle tiramisu", 74, 14.8f, 570, 490);
        casillas.add(new Casilla(casilla6, 12, 400)); 
        TituloPropiedad casilla7 = new TituloPropiedad("Glorieta del helado", 78, 15.6f, 640, 530);
        casillas.add(new Casilla(casilla7, 13, 450)); 
        TituloPropiedad casilla8 = new TituloPropiedad("Av. Tosta Rica", 82, 16.4f, 710, 570);
        casillas.add(new Casilla(casilla8, 14, 500)); 
        casillas.add(new Casilla(TipoCasilla.IMPUESTO, 15));
        casillas.add(new Casilla(TipoCasilla.SORPRESA, 16));
        TituloPropiedad casilla9 = new TituloPropiedad("Calle Te Paquistani", 86, 17.2f, 780, 610);
        casillas.add(new Casilla(casilla9, 17, 550)); 
        TituloPropiedad casilla10 = new TituloPropiedad("Calle galletas María", 90, 18f, 850, 650);
        casillas.add(new Casilla(casilla10, 18, 600)); 
        TituloPropiedad casilla11 = new TituloPropiedad("Av. del postre caro", 94, 18.8f, 920, 690);
        casillas.add(new Casilla(casilla11, 19, 650));
    };
    
    public Casilla getCarcel(){ return carcel; };
    
    public boolean esCasillaCarcel(int numeroCasilla){ return casillas.get(numeroCasilla).getTipo() == TipoCasilla.CARCEL; };
    public Casilla obtenerCasillaNumero(int numeroCasilla){ return casillas.get(numeroCasilla); };
    public Casilla obtenerNuevaCasilla(Casilla casilla, int desplazamiento){ return casillas.get((casilla.getNumeroCasilla() + desplazamiento) % casillas.size()); };
    
    @Override
    public String toString(){
        String string_casillas = "";
        for(Casilla casilla : casillas){
            string_casillas += casilla.toString() + "\n";
        }
        return string_casillas;
    };
}
