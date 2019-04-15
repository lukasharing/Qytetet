package model;

public enum MutationType {
	RANDOM("Aleatorio"),
	UNIFORM("Uniforme"),
	NONUNIFORM("No Uniforme"),
	INSERTION("Inserción"),
	SWAP("Intercambio"),
	INVERSION("Inversión"),
	HEURISTIC("Heurística"),
	SELF_METHOD_1("Método Propio 1");
	
	private final String name;
	
	private MutationType(String n) {
		this.name = n;
	};
	
	@Override
	public String toString() { return this.name; };
}
