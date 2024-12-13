package pt.josealm3ida.aoc.guard_gallivant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    private static List<Character> GUARD_CHARS = List.of('^', '>', 'v', '<');

    public static void main(String[] args) {
        List<List<Character>> charList = Utils.readCharacters("input/guard-gallivant.txt");
        Character[][] map = charList.stream()
            .map(l -> l.stream().toArray(Character[]::new))
            .toArray(Character[][]::new);

        prettyPrintMap(map);

        Coord guardStartingPosition = getGuardPosition(map);
        Character guardStartingOrientation = map[guardStartingPosition.x()][guardStartingPosition.y()];
        Map<Coord, List<Character>> walkedMap = new HashMap<>();

        System.out.println(part1(deepCopyArray(map), walkedMap, guardStartingPosition));
        System.out.println(part2(deepCopyArray(map), walkedMap, guardStartingPosition));
    }

    private static int part1(Character[][] map, Map<Coord, List<Character>> walkedMap, Coord guardStartingPosition) {
        walkUntilDone(map, walkedMap, guardStartingPosition);
        return walkedMap.size();
    }

    private static int part2(Character[][] map, Map<Coord, List<Character>> walkedMap, Coord guardStartingPosition) {
        int obstructionCount = 0;

        for (Map.Entry<Coord, List<Character>> entry : walkedMap.entrySet()) {
            Coord c = entry.getKey();
            if (c == guardStartingPosition) continue;

            Character[][] newMap = deepCopyArray(map);
            newMap[c.x()][c.y()] = 'O';

            Map<Coord, List<Character>> newWalkedMap = new HashMap<>();
            if (walkUntilDone(newMap, newWalkedMap, guardStartingPosition) == DoneReason.LOOP) {
                obstructionCount++;
            }
        }

        return obstructionCount;
    }

    private static DoneReason walkUntilDone(Character[][] map, Map<Coord, List<Character>> walkedMap, Coord guardStartingPosition) {
        Coord guardCurrPosition = guardStartingPosition;
        while (true) {
            guardCurrPosition = walk(map, walkedMap, guardCurrPosition);
            if (guardCurrPosition == null || guardCurrPosition.x() == -1) {
                break;
            }
        }

        return guardCurrPosition == null ? DoneReason.OUT_OF_BOUNDS : DoneReason.LOOP;
    }

    private static Coord walk(Character[][] map, Map<Coord, List<Character>> walkedMap, Coord guardPosition) {
        int i = guardPosition.x();
        int j = guardPosition.y();

        int nexti = i;
        int nextj = j;

        Character curr = map[i][j];
        switch (curr) {
            case '^':
                nexti--;
                break;
            case 'v':
                nexti++;
                break;

            case '<':
                nextj--;
                break;

            case '>':
                nextj++; 
                break;
        }

        if (!addToWalkedMap(walkedMap, guardPosition, map[i][j])) { // Stuck in loop
            return new Coord(-1, -1);
        }

        map[i][j] = 'X';

        // Out of bounds
        if (nexti < 0 || nexti >= map.length || nextj < 0 || nextj >= map[i].length) {
            return null;
        }

        Character next = map[nexti][nextj];

        if (next == '#' || next == 'O') {
            map[i][j] = rotateguard(curr);
            return guardPosition;
        }

        map[nexti][nextj] = curr;
        return new Coord(nexti, nextj);
    }

    private static Character rotateguard(Character guard) {
        int idx = GUARD_CHARS.indexOf(guard);
        if (idx == GUARD_CHARS.size() - 1) {
            idx = -1;
        }
        return GUARD_CHARS.get(idx + 1);
    }

    private static boolean addToWalkedMap(Map<Coord, List<Character>> walkedMap, Coord guardPosition, Character guard) {
        List<Character> orientations = walkedMap.get(guardPosition);

        if (orientations == null) {
            orientations = new ArrayList<>();
            walkedMap.put(guardPosition, orientations);
        } else if (orientations.contains(guard)) {
            return false;
        }

        orientations.add(guard);
        return true;
    }

    private static Coord getGuardPosition(Character[][] map) {
        int i = 0;
        int j = 0;
        boolean doBreak = false;
        for (i = 0; i < map.length; i++) {
            for (j = 0; j < map[i].length; j++) {
                if (GUARD_CHARS.contains(map[i][j])) {
                    doBreak = true;
                    break;
                }
            }

            if (doBreak) {
                break;
            }
        }

        return new Coord(i, j);
    }

    private static Character[][] deepCopyArray(Character[][] arr) {
        return Arrays.stream(arr)
             .map((Character[] row) -> row.clone())
             .toArray((int length) -> new Character[length][]);
    }

    private static void prettyPrintMap(Character[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    private static enum DoneReason {
        OUT_OF_BOUNDS,
        LOOP
    }

}
