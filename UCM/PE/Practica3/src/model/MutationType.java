package model;

public enum MutationType {
	RANDOM("Aleatorio"),
	UNIFORM("Uniforme"),
	NONUNIFORM("No Uniforme"),
	INSERTION("Inserción"),
	SWAP("Intercambio"),
	INVERSION("Inversión"),
	HEURISTIC("Heurística"),
	SELF_METHOD_1("Método Propio 1"),
	SIMPLE_TERMINAL("Terminal Simple"),
	SUBTREE("SubÁrbol"),
	PERMUTATION("Permutación"),
	SIMPLE_FUNCTION("Funcional Simple");
	
	private final String name;
	
	private MutationType(String n) {
		this.name = n;
	};
	
	@Override
	public String toString() { return this.name; };
}
