package pt.josealm3ida.aoc.utils;

public record Coord(int i, int j) {

	public float getSlope(Coord c) {
		return (float) (c.i() - this.i()) / (float) (c.j() - this.j()); 
	}

}

