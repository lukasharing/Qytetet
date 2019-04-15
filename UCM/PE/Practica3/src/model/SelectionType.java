package model;

public enum SelectionType {
	ROULETTE("Ruleta"),
	DETE_TOURNAMENT("Torneo Determinista"),
	PRB_TOURNAMENT("Torneo Probabilista"),
	RANKING("Ranking"),
	TRUNCATION("Truncamiento");
	
	private final String name;
	
	private SelectionType(String n) {
		this.name = n;
	};
	
	@Override
	public String toString() { return this.name; };
};
