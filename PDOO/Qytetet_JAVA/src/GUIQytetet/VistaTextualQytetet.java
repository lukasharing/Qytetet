package GUIQytetet;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class VistaTextualQytetet {
	private static final Scanner in = new Scanner(System.in);
	
	private boolean comprobarOpcion(String lectura, int min, int max) {
		boolean valido = true;   
		try {
			int opcion = Integer.parseInt(lectura);
			if (opcion< min || opcion > max) { // No es un entero entre los válidos
				this.mostrar("El numero debe encontrarse entre min y max.");
				valido = false;
			}
		} catch (NumberFormatException e){
			this.mostrar("Debe introducir un numero.");
			valido = false;
		}
		if (!valido) {
			this.mostrar("Seleccion erronea, inténtelo de nuevo.");
		}
		return valido;
	};
	
	public int menuElegirPropiedad(ArrayList<String> listaPropiedades) {
		Map<Integer, String> menuEP = new TreeMap();
		for(int i = 0; i < listaPropiedades.size(); ++i) {
			menuEP.put(i, listaPropiedades.get(i));
		}
		int salida = this.seleccionMenu(menuEP);
		return salida;
	};
	
	public int menuGestionInmobiliaria() {
		this.mostrar("Elige la gestion inmobiliaria que deseas hacer");
	    Map<Integer, String> menuGI = new TreeMap();
	    menuGI.put(0, "Siguiente Jugador"); 
	    menuGI.put(1, "Edificar casa");
	    menuGI.put(2, "Edificar Hotel"); 
	    menuGI.put(3, "Vender propiedad");  	
	    menuGI.put(4, "Hipotecar Propiedad"); 
	    menuGI.put(5, "Cancelar Hipoteca");
	    int salida = this.seleccionMenu(menuGI);
	    return salida;
	};
	
	public int menuSalirCarcel() {
		this.mostrar("Elige el metodo para salir de la carcel");
	    Map<Integer, String> menuGI = new TreeMap();
	    menuGI.put(0, "Pagar libertad"); 
	    menuGI.put(1, "Tirar el dado");
	    int valor = this.seleccionMenu(menuGI);
		return valor;
	};

	public boolean elegirQuieroComprar() {
		this.mostrar("El jugador actual puede comprar la calle actual:");
	    Map<Integer, String> menuGI = new TreeMap();
	    menuGI.put(0, "No comprar calle"); 
	    menuGI.put(1, "Comprar calle");
	    int valor = this.seleccionMenu(menuGI);
		return valor == 0 ? false : true;
	};
	
	public ArrayList<String> obtenerNombreJugadores() {
		boolean valido = true; 
		String lectura;
		ArrayList<String> nombres = new ArrayList();
		do{ 
			this.mostrar("Escribe el número de jugadores: (de 2 a 4):");
			lectura = in.nextLine();
			valido = this.comprobarOpcion(lectura, 2, 4);
		}while(!valido);

		for (int i = 1; i <= Integer.parseInt(lectura); ++i){
			this.mostrar("Nombre del jugador " + i + ": ");
			nombres.add (in.nextLine());
		}
		return nombres;
	};
	
	private int seleccionMenu(Map<Integer, String> menu) {
		boolean valido = true; 
		int numero;
		String lectura;
		do {
			for(Map.Entry<Integer, String> fila : menu.entrySet()) {
				numero = fila.getKey();
				String texto = fila.getValue();
				this.mostrar(numero + " : " + texto);
			}
			this.mostrar("Elige una opción: ");
			lectura = in.nextLine();
			valido = this.comprobarOpcion(lectura, 0, menu.size() - 1);
		}while (!valido);
		return Integer.parseInt(lectura);
	};
	
	public void mostrar(String texto) {
		System.out.println(texto);
	};
}
