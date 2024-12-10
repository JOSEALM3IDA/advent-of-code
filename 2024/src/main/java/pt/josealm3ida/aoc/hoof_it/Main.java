package pt.josealm3ida.aoc.hoof_it;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Direction;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<List<Integer>> topographicMap = Utils.readCharacters("input/hoof-it.txt").stream()
            .map(l -> l.stream()
                .map(e -> e.equals('.') ? 10 : Character.getNumericValue(e))
                .toList()
            ).toList();

        System.out.println(calcScore(topographicMap, true));
        System.out.println(calcScore(topographicMap, false));
    }

    private static int calcScore(final List<List<Integer>> topographicMap, boolean onlyLongestPaths) {
        int totalScore = 0;

        for (int i = 0; i < topographicMap.size(); i++) {
            for (int j = 0; j < topographicMap.get(i).size(); j++) {
                if (topographicMap.get(i).get(j) == 0) {
                    Coord trailhead = new Coord(i, j);
                    List<CoordPath> trailheadTrails = dfs(topographicMap, new CoordPath(trailhead, 0, new ArrayList<>(List.of(trailhead))), onlyLongestPaths); 

                    totalScore += trailheadTrails.size();
                }

            }
        }

        return totalScore;
    }

    private static List<CoordPath> dfs(List<List<Integer>> topographicMap, CoordPath startingPoint, boolean getOnlyLongestPaths) {
        Stack<CoordPath> toVisit = new Stack<>();
        Map<Coord, List<CoordPath>> allPaths = new HashMap<>();

        toVisit.add(startingPoint);

        while (!toVisit.isEmpty()) {
            CoordPath curr = toVisit.pop();

            if (curr.elevation() == 9) {
                List<CoordPath> allPathsToCurr = allPaths.get(curr.coord());
                if (allPathsToCurr == null) {
                    allPathsToCurr = new ArrayList<>();
                    allPaths.put(curr.coord(), allPathsToCurr);
                }

                allPathsToCurr.add(curr);
            }

            List<CoordPath> validNeighbors = getValidNeighbors(topographicMap, curr);
            validNeighbors.forEach(p -> toVisit.push(p));
        }

        return getOnlyLongestPaths
            ? allPaths.values().stream()
                .map(l -> l.stream()
                    .max(Comparator.comparingInt(c -> c.pathTo().size()))
                    .orElse(null)
                ).filter(Objects::nonNull)
                .toList()
            :  allPaths.values().stream().flatMap(List::stream).toList();
    } 

    private static List<CoordPath> getValidNeighbors(List<List<Integer>> topographicMap, CoordPath curr) {
        Coord currCoord = curr.coord();
        int currElevation = curr.elevation();

        List<CoordPath> validNeighbors = new ArrayList<>();

        for (Direction d : Direction.values()) {
            if (d.isDiagonal()) {
                continue;
            }

            int nexti = d.calcNextRow(currCoord.i());
            int nextj = d.calcNextCol(currCoord.j());

            boolean outOfBounds = nexti < 0 || nexti >= topographicMap.size() || nextj < 0 || nextj >= topographicMap.get(nexti).size();

            if (outOfBounds) {
                continue;
            }

            int neighborElevation = topographicMap.get(nexti).get(nextj);
            if (currElevation + 1 != neighborElevation) {
                continue;
            }

            Coord neighborCoord = new Coord(nexti, nextj);

            List<Coord> pathToNeighbor = new ArrayList<>(curr.pathTo());
            pathToNeighbor.add(neighborCoord);

            validNeighbors.add(new CoordPath(neighborCoord, neighborElevation, pathToNeighbor));
        }

        return validNeighbors;
    }

    private static void prettyPrintPaths(final List<List<Integer>> topographicMap, List<CoordPath> paths) {
        List<List<Character>> printMap = new ArrayList<>();
        for (List<Integer> line : topographicMap) {
            printMap.add(new ArrayList<Character>(line.stream().map(i -> '.').toList()));
        }

        for (CoordPath coordPath : paths) {
            List<Coord> pathTo = coordPath.pathTo(); 
            for (Coord c : pathTo) {
                printMap.get(c.i()).set(c.j(), Character.forDigit(topographicMap.get(c.i()).get(c.j()), 10));
            }
        }

        for (List<Character> line : printMap) {
            for (Character c : line) {
                System.out.print(c);
            }

            System.out.println();
        }
    }

    record CoordPath (Coord coord, int elevation, List<Coord> pathTo) {}

}
