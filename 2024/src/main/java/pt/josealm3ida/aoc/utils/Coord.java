package pt.josealm3ida.aoc.utils;

public record Coord(int x, int y) {

	public float getSlope(Coord c) {
		return (float) (c.x() - this.x()) / (float) (c.y() - this.y()); 
	}

}

