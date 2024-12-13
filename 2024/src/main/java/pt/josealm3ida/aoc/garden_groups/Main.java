package pt.josealm3ida.aoc.garden_groups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Direction;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<Region> regions = getContiguousRegions(Utils.readCharacters("input/garden-groups.txt"));
        System.out.println(part1(regions));
        System.out.println(part2(regions));
    }

    private static Region parseGroup(List<List<Character>> garden, Character tempChar) {
        Region group = null;

        for (int i = 0; i < garden.size(); i++) {
            List<Character> l = garden.get(i);
            for (int j = 0; j < l.size(); j++) {
                Character c = l.get(j);
                if (!c.equals(tempChar)) {
                    continue;
                }

                if (group == null) {
                    Map<Integer, List<Range>> lines = new HashMap<>();
                    lines.put(i, new ArrayList<>());
                    group = new Region(lines);
                }

                group.add(i, j);
            }
        }

        return group;
    }

    private static int part1(List<Region> regions) {
        int cost = 0;

        for (Region region : regions) {
            cost += region.area() * region.perimeter();
        }

        return cost;
    }

    private static int part2(List<Region> regions) {
        int cost = 0;

        for (Region region : regions) {
            cost += region.area() * region.sides();
        }

        return cost;
    }

    private static List<Region> getContiguousRegions(List<List<Character>> garden) {
        Set<Coord> visited = new HashSet<>();

        List<Region> regions = new ArrayList<>();

        for (int i = 0; i < garden.size(); i++) {
            for (int j = 0; j < garden.size(); j++) {
                Coord curr = new Coord(i, j);
                if (visited.contains(curr)) {
                    continue;
                }

                regions.add(floodFill(garden, visited, curr));
            }
        }

        return regions;
    }

    private static Region floodFill(final List<List<Character>> garden, Set<Coord> visited, Coord start) {
        final Character tempChar = '#';
        final List<List<Character>> tempGarden = new ArrayList<>();
        garden.forEach(l -> tempGarden.add(new ArrayList<>(l)));

        Stack<Coord> toVisit = new Stack<>();

        toVisit.add(start);

        while (!toVisit.isEmpty()) {
            Coord curr = toVisit.pop();
            if (visited.contains(curr)) {
                continue;
            }

            visited.add(curr);

            List<Coord> validNeighbors = getValidNeighbors(tempGarden, curr);
            validNeighbors.forEach(p -> toVisit.push(p));

            tempGarden.get(curr.i()).set(curr.j(), tempChar);
        }

        return parseGroup(tempGarden, tempChar);
    } 

    private static List<Coord> getValidNeighbors(final List<List<Character>> garden, Coord curr) {
        List<Coord> validNeighbors = new ArrayList<>();

        Character currType = garden.get(curr.i()).get(curr.j());

        for (Direction d : Direction.values()) {
            if (d.isDiagonal()) {
                continue;
            }

            int nexti = d.calcNextRow(curr.i());
            int nextj = d.calcNextCol(curr.j());

            boolean outOfBounds = nexti < 0 || nexti >= garden.size() || nextj < 0 || nextj >= garden.get(nexti).size();

            if (outOfBounds) {
                continue;
            }

            Character neighborType = garden.get(nexti).get(nextj);
            if (!neighborType.equals(currType)) {
                continue;
            }

            validNeighbors.add(new Coord(nexti, nextj));
        }
        
        return validNeighbors;
    }

    private static class Range {
        private int starting;
        private int ending;

        public Range(int starting, int ending) {
            this.starting = starting;
            this.ending = ending;
        }

        public boolean isInRange(int n) {
            return n >= starting && n <= ending;
        }

        public int size() {
            return ending - starting + 1;
        }

		public int getStarting() {
			return starting;
		}

		public void setStarting(int starting) {
			this.starting = starting;
		}

		public int getEnding() {
			return ending;
		}

		public void setEnding(int ending) {
			this.ending = ending;
		}

        @Override
        public String toString() {
            return "[%s - %s]".formatted(starting, ending);
        }

    }

    private record Region(Map<Integer, List<Range>> lines) {

        public int area() {
            return lines.values().stream().flatMap(List::stream).mapToInt(Range::size).sum();
        }

        public int sides() {
            int sides = 0;
            for (Map.Entry<Integer, List<Range>> line : lines.entrySet()) {
                int i = line.getKey();

                boolean hasLineAbove = hasLineAbove(i);
                boolean hasLineBelow = hasLineBelow(i);
                for (Range range : line.getValue()) {
                    if (hasLineAbove) {
                        for (int j = range.getStarting(); j <= range.getEnding(); j++) {
                            if (!lineContains(i - 1, j) && (j == range.getStarting() || lineContains(i - 1, j - 1))) {
                                sides++;
                            }

                            if (j == range.getStarting() && (!lineContains(i - 1, j) || lineContains(i - 1, j - 1))) {
                                sides++;
                            }
                        }

                        if (!lineContains(i - 1, range.getEnding()) || lineContains(i - 1, range.getEnding() + 1)) {
                            sides++;
                        }
                    } else {
                        sides += 3;
                    }

                    if (hasLineBelow) {
                        for (int j = range.getStarting(); j <= range.getEnding(); j++) {
                            if (!lineContains(i + 1, j) && (j == range.getStarting() || lineContains(i + 1, j - 1))) {
                                sides++;
                            }
                        }
                    } else {
                        sides++;
                    }
                }
            }

            return sides;
        }

        public int perimeter() {
            int perimeter = 0;
            for (Map.Entry<Integer, List<Range>> line : lines.entrySet()) {
                int i = line.getKey();

                boolean hasLineAbove = hasLineAbove(i);
                boolean hasLineBelow = hasLineBelow(i);
                for (Range range : line.getValue()) {
                    perimeter += 2; // start and end of range

                    if (hasLineAbove) {
                        for (int j = range.getStarting(); j <= range.getEnding(); j++) {
                            if (!lineContains(i - 1, j)) {
                                perimeter++;
                            }
                        }
                    } else {
                        perimeter += range.size();
                    }

                    if (hasLineBelow) {
                        for (int j = range.getStarting(); j <= range.getEnding(); j++) {
                            if (!lineContains(i + 1, j)) {
                                perimeter++;
                            }
                        }
                    } else {
                        perimeter += range.size();
                    }
                    
                }
            }

            return perimeter;
        }

        private boolean lineContains(int whichLine, int col) {
            if (whichLine < 0 || col < 0) {
                return false;
            }

            return lines.get(whichLine).stream().filter(r -> r.isInRange(col)).toList().size() == 1;
        }

        private boolean hasLineAbove(int whichLine) {
            return lines.containsKey(whichLine - 1);
        }

        private boolean hasLineBelow(int whichLine) {
            return lines.containsKey(whichLine + 1);
        }

        public void add(int whichLine, int colToAdd) {
            List<Range> ranges = lines.get(whichLine);
            if (ranges == null) {
                ranges = new ArrayList<>();
                ranges.add(new Range(colToAdd, colToAdd));
                lines.put(whichLine, ranges);
                return;
            }

            for (Range range : ranges) {
                if (colToAdd == range.getStarting() - 1) {
                    range.setStarting(colToAdd);
                    return;
                } 

                if (colToAdd == range.getEnding() + 1) {
                    range.setEnding(colToAdd);
                    return;
                }
            }

            ranges.add(new Range(colToAdd, colToAdd));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("\na:%s p:%s s:%s\n".formatted(area(), perimeter(), sides()));

            lines.entrySet().forEach(e -> {
                sb.append("\t%s %s\n".formatted(e.getKey(), e.getValue())); 
            });

            return sb.toString();
        }
    }
}
