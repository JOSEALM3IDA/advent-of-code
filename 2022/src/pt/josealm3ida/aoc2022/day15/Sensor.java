package pt.josealm3ida.aoc2022.day15;

import java.util.Arrays;

public class Sensor {

    private final int[] coords;
    private final int[] coordsBeacon;
    private final int manhattanToBeacon;

    public Sensor(int[] coords, int[] coordsBeacon) {
        this.coords = coords;
        this.coordsBeacon = coordsBeacon;
        this.manhattanToBeacon = Math.abs(coordsBeacon[0] - coords[0]) + Math.abs(coordsBeacon[1] - coords[1]);
    }

    public boolean isPointDetected(int[] point) {
        return Math.abs(point[0] - coords[0]) + Math.abs(point[1] - coords[1]) <= manhattanToBeacon;
    }

    public int[] getCoordsBeacon() {
        return coordsBeacon;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "coords=" + Arrays.toString(coords) +
                ", manhattanToBeacon=" + manhattanToBeacon +
                '}';
    }
}
