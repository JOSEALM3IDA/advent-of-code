package pt.josealm3ida.aoc2022.day15;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Sensor implements Comparable<Sensor>{

    private final int[] coords;
    private final int[] coordsBeacon;
    private final int manhattanToBeacon;

    public Sensor(int[] coords, int[] coordsBeacon) {
        this.coords = coords;
        this.coordsBeacon = coordsBeacon;
        this.manhattanToBeacon = Math.abs(coordsBeacon[0] - coords[0]) + Math.abs(coordsBeacon[1] - coords[1]);
    }

    public boolean isPointDetected(int[] point, boolean countBeacon) {
        if (point[0] == coordsBeacon[0] && point[1] == coordsBeacon[1]) return countBeacon;

        return Math.abs(point[0] - coords[0]) + Math.abs(point[1] - coords[1]) <= manhattanToBeacon;
    }

    public int[][] getEdges() {
        int[][] edges = new int[4][];
        edges[0] = new int[]{coords[0], coords[1] - manhattanToBeacon};
        edges[1] = new int[]{coords[0] + manhattanToBeacon, coords[1]};
        edges[2] = new int[]{coords[0], coords[1] + manhattanToBeacon};
        edges[3] = new int[]{coords[0] - manhattanToBeacon, coords[1]};
        return edges;
    }

    public Set<String> getOutsidePerimeterPoints() {
        int[][] edges = getEdges();
        int nPoints = manhattanToBeacon + 1;

        Set<String> outsidePerimeterSet = new HashSet<>();

        // Up -> Right
        for (int i = 0; i < nPoints; i++) outsidePerimeterSet.add((edges[0][0] + i) + "," + (edges[0][1] - 1 + i));

        // Right -> Down
        for (int i = 0; i < nPoints; i++) outsidePerimeterSet.add((edges[1][0] + 1 - i) + "," + (edges[1][1] + i));

        // Down -> Left
        for (int i = 0; i < nPoints; i++) outsidePerimeterSet.add((edges[2][0] - i) + "," + (edges[2][1] + 1 - i));

        // Left -> Up
        for (int i = 0; i < nPoints; i++) outsidePerimeterSet.add((edges[3][0] - 1 + i) + "," + (edges[3][1] - i));

        return outsidePerimeterSet;
    }

    @Override
    public int compareTo(Sensor o) {
        return o.coords[0] - coords[0];
    }
}
