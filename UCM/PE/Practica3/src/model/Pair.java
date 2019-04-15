package model;

public class Pair<N, M> {

	public final N first;
	public final M second;

	public Pair(N left, M right) {
		this.first = left;
		this.second = right;
	}

	@Override
	public String toString() {
		return "(" + this.first.toString() + ", " + this.second.toString() + ")";
	}
}