package pt.josealm3ida.aoc2022.day12;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {

    private final int x, y, minCostFrom;
    private final List<Node> pathTo;
    private final char height;
    private final boolean isEnd;

    public Node(int[] coords, char height, List<Node> pathTo, int[] endCoords) {
        this.x = coords[0];
        this.y = coords[1];

        this.isEnd = this.x == endCoords[0] && this.y == endCoords[1];

        this.height = this.isEnd ? HillClimbingAlgorithm.END_HEIGHT : height;
        this.pathTo = pathTo;
        this.minCostFrom = (int) Math.sqrt(Math.pow(endCoords[0] - this.x, 2) + Math.pow(endCoords[1] - this.y, 2));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public int getCostTo() {
        return pathTo.size();
    }

    public List<Node> getPathToInclusive() {
        List<Node> pathToCopy = new ArrayList<>(pathTo);
        pathToCopy.add(this);
        return pathToCopy;
    }

    public int getMinCostFrom() {
        return minCostFrom;
    }

    public char getHeight() {
        return height;
    }

    @Override
    public int compareTo(Node n) {
        return (this.getCostTo() + minCostFrom) - (n.getCostTo() + n.getMinCostFrom());
    }
}
