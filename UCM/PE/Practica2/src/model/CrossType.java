package model;

import java.util.Arrays;
import java.util.Optional;

public enum CrossType {
	MONOPOINT("Monopunto"),
	MULTIPOINT("Multipunto"),
	UNIFORM("Uniforme"),
	PARTIALLY_MAPPED("Emparejamiento Parcial (PMX)"),
	ORDERED("Cruce por Orden (OX)"),
	ORDERED_VARIANT("Variante de OX"),
	CICLES("Ciclos (CX)"),
	RECOMBINATION("Recombinación"),
	ORDINAL_CODIFICATION("Codificación Ordinal"),
	SELF_METHOD_1("Método propio 1"),
	SELF_METHOD_2("Método propio 2");
	
	private final String name;
	
	private CrossType(String n) {
		this.name = n;
	};
	
	@Override
	public String toString() { return this.name; };
}
