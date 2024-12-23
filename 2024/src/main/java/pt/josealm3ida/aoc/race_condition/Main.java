package pt.josealm3ida.aoc.race_condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Direction;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {
    
    private static final int MIN_TIME_SAVED = 100;
    private static final int MAX_CHEAT_LENGTH_PT1 = 2;
    private static final int MAX_CHEAT_LENGTH_PT2 = 20;

    public static void main(String[] args) {
        List<List<Character>> maze = Utils.readCharacters("input/race-condition.txt");
        List<Coord> startEnd = getStartEnd(maze);
        List<Coord> startingPath = getStartingPath(maze, startEnd.get(0), startEnd.get(1));

        System.out.println(part1(maze, startingPath));
        System.out.println(part2(maze, startingPath));
    }

    private static int part1(List<List<Character>> maze, List<Coord> startingPath) {
        return getNCheatsOverMinTimeSaved(maze, startingPath, MAX_CHEAT_LENGTH_PT1);
    }

    private static int part2(List<List<Character>> maze, List<Coord> startingPath) {
        return getNCheatsOverMinTimeSaved(maze, startingPath, MAX_CHEAT_LENGTH_PT2);
    }

    private static int getNCheatsOverMinTimeSaved(List<List<Character>> maze, List<Coord> startingPath, int maxCheatLength) {
        Map<Integer, Integer> saves = new HashMap<>();
        for (Cheat cheat : getCheats(maze, startingPath, maxCheatLength)) {
            int timeSaved = startingPath.subList(startingPath.indexOf(cheat.start()), startingPath.indexOf(cheat.end())).size() - cheat.getLength();
            if (timeSaved >= MIN_TIME_SAVED) {
                if (!saves.containsKey(timeSaved)) {
                    saves.put(timeSaved, 0);
                }

                saves.put(timeSaved, saves.get(timeSaved) + 1);
            }
        }

        return saves.entrySet().stream().mapToInt(e -> e.getValue()).sum();
    }

    private static Set<Cheat> getCheats(List<List<Character>> maze, List<Coord> startingPath, int maxLength) {
        Set<Cheat> allCheats = new HashSet<>();

        startingPath.forEach(startCheat -> {
            int startIndex = startingPath.indexOf(startCheat);
            List<Coord> endsCheat = new ArrayList<>(startingPath.subList(startIndex + 1, startingPath.size())).stream().filter(endCheat -> startCheat.getMDistanceTo(endCheat) <= maxLength).toList();
            endsCheat.forEach(endCheat -> allCheats.add(new Cheat(startCheat, endCheat)));
        });

        return allCheats;
    }


    private static List<Coord> getStartEnd(List<List<Character>> maze) {
        Coord start = null;
        Coord end = null;

        for (int y = 0; y < maze.size(); y++) {
            for (int x = 0; x < maze.get(y).size(); x++) {
                Character c = maze.get(y).get(x);
                if (c.equals('S')) {
                    start = new Coord(x, y);
                } else if (c.equals('E')) {
                    end = new Coord(x, y);
                }
            } 
        }
        
        return List.of(start, end);
    }

    private static List<Coord> getStartingPath(List<List<Character>> maze, Coord start, Coord end) {
        List<Coord> currPath = new ArrayList<>(List.of(start));

        while (!currPath.getLast().equals(end)) {
            currPath.add(getNeighbors(maze, currPath.getLast()).stream().filter(n -> !currPath.contains(n)).toList().getFirst());
        }

        return currPath;
    }

    private static List<Coord> getNeighbors(List<List<Character>> maze, Coord c) {
        List<Coord> neighbors = new ArrayList<>();

        for (Direction d : Direction.values()) {
            if (d.isDiagonal()) {
                continue;
            }

            Coord neighbor = new Coord(d.calcNextCol(c.x()), d.calcNextRow(c.y()));

            if (maze.get(neighbor.y()).get(neighbor.x()).equals('#')) {
                continue;
            }

            neighbors.add(neighbor);
        }

        return neighbors;
    }

    private record Cheat(Coord start, Coord end) {

        public int getLength() {
            return start.getMDistanceTo(end);
        }

    }

}
