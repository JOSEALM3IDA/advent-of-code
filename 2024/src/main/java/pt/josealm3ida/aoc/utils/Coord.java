package pt.josealm3ida.aoc.utils;

public record Coord(int x, int y) {

	public float getSlope(Coord c) {
		return (float) (c.x() - this.x()) / (float) (c.y() - this.y()); 
	}

	public int getMDistanceTo(Coord c) {
		return Math.abs(this.x() - c.x()) + Math.abs(this.y() - c.y());
	}

	public double getEDistanceTo(Coord c) {
		return Math.sqrt(Math.pow(c.x() - this.x(), 2) + Math.pow(c.y() - this.y(), 2));
	}

	public int getQuadrant(Coord midPoint) {
		if (midPoint.x() == this.x || midPoint.y() == this.y) {
			return -1;
		}

		boolean afterMidpointX = this.x > midPoint.x();
		boolean afterMidpointY = this.y > midPoint.y();

		if (afterMidpointX) {
			return afterMidpointY ? 3 : 1;
		}

		return afterMidpointY ? 2 : 0;
	}

}

