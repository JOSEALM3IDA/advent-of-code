package pt.josealm3ida.aoc.ram_run;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Direction;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    private static int GRID_SIZE = 71;
    private static int DRAW_AFTER_N_BYTES = 1024;

    public static void main(String[] args) {
        List<Coord> bytes = Utils.readLines("input/ram-run.txt").stream().map(l -> Utils.readAllIntegersInString(l)).map(l -> new Coord(l.get(0), l.get(1))).toList();
        System.out.println(part1(bytes));
        System.out.println(part2(bytes));
    }

    private static int part1(List<Coord> bytes) {
        MazePath path = aStar(bytes.subList(0, DRAW_AFTER_N_BYTES));
        return path.getPath().size() - 1;
    }

    private static String part2(List<Coord> bytes) {
        for (int i = DRAW_AFTER_N_BYTES; i < bytes.size(); i++) {
            Coord currByte = bytes.get(i - 1);
            MazePath path = aStar(bytes.subList(0, i));

            if (path == null) {
                return "%s,%s".formatted(currByte.x(), currByte.y());
            }
        }

        return "-1,-1";
    }

    private static MazePath aStar(List<Coord> bytes) {
        Coord start = new Coord(0, 0);
        Coord end = new Coord(GRID_SIZE - 1, GRID_SIZE - 1);

        Queue<MazePath> queue = new PriorityQueue<>();
        queue.add(new MazePath(new ArrayList<>(List.of(start)), end));

        Set<Coord> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            MazePath curr = queue.poll();
            Coord currCoord = curr.getPath().getLast();

            if (visited.contains(currCoord)) {
                continue;
            }
           
            visited.add(currCoord);

            if (currCoord.equals(end)) {
                return curr;
            }

            queue.addAll(getValidNeighbors(bytes, currCoord).stream().map(c -> {
                List<Coord> newPath = new ArrayList<>(curr.getPath());
                newPath.add(c);
                return new MazePath(newPath, end);
            }).toList());
        }

        return null;
    }

    private static List<Coord> getValidNeighbors(List<Coord> bytes, Coord c) {
        List<Coord> neighbors = new ArrayList<>();

        for (Direction d : Direction.values()) {
            if (d.isDiagonal()) {
                continue;
            }

            Coord neighbor = new Coord(d.calcNextCol(c.x()), d.calcNextRow(c.y()));
            if (neighbor.x() >= GRID_SIZE || neighbor.x() < 0
                || neighbor.y() >= GRID_SIZE || neighbor.y() < 0
                || bytes.contains(neighbor)
            ) {
                continue;
            }

            neighbors.add(neighbor);
        }

        return neighbors;
    }


    private static class MazePath implements Comparable<MazePath> {

        private final List<Coord> path;
        private final Coord end;

        public MazePath(List<Coord> path, Coord end) {
            this.path = path;
            this.end = end;
        }

		public List<Coord> getPath() {
			return path;
		}

        public int getCost() {
            return this.path.size();
        }

        public int expectedTotalCostToEnd() {
            return this.getCost() + (int) path.getLast().getEDistanceTo(end);
        }

       @Override
        public int compareTo(MazePath mp) {
            return this.expectedTotalCostToEnd() - mp.expectedTotalCostToEnd();
        }

        @Override
        public final boolean equals(Object o) {
            return o.getClass() == this.getClass() && this.expectedTotalCostToEnd() == ((MazePath) o).expectedTotalCostToEnd(); 
        }

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
            path.forEach(c -> sb.append("(%s,%s)->".formatted(c.x(), c.y())));
            return sb.toString();
		}

    }

    private static void prettyPrint(List<Coord> bytes, List<Coord> path) {
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                Coord curr = new Coord(x, y);
                System.out.print(bytes.contains(curr) ? "#" : path.contains(curr) ? "O" : ".");
            }
            System.out.println();
        }
    }

}
