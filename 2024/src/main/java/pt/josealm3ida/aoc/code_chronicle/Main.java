package pt.josealm3ida.aoc.code_chronicle;

import java.util.ArrayList;
import java.util.List;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {
    
    public static void main(String[] args) {
        List<String> lines = Utils.readLines("input/code-chronicle.txt");
        List<List<List<Character>>> schematics = parseSchematics(lines);
        List<Lock> locks = parseLocks(schematics);
        List<Key> keys = parseKeys(schematics);

        System.out.println(part1(locks, keys));
    }

	private static int part1(List<Lock> locks, List<Key> keys) {
        int uniquePairs = 0;

        for (Lock lock : locks) {
            for (Key key : keys) {
                if (lock.fits(key)) {
                    uniquePairs++;
                }
            }
        }

        return uniquePairs;
	}

	private static List<List<List<Character>>> parseSchematics(List<String> lines) {
        List<List<List<Character>>> schematics = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 7) {
            schematics.add(Utils.readCharactersFromStringList(lines.subList(i, i + 7)));
            i++;
        }

        return schematics;
    }

    private static List<Lock> parseLocks(List<List<List<Character>>> schematics) {
        List<Lock> locks = new ArrayList<>();

        for (List<List<Character>> schematic : schematics) {
            if (!schematic.get(0).get(0).equals('#')) {
                continue;
            }

            locks.add(Lock.fromSchematic(schematic));
        }

        return locks;
	}

    private static List<Key> parseKeys(List<List<List<Character>>> schematics) {
        List<Key> keys = new ArrayList<>();

        for (List<List<Character>> schematic : schematics) {
            if (!schematic.get(0).get(0).equals('.')) {
                continue;
            }

            keys.add(Key.fromSchematic(schematic));
        }

        return keys;
	}

    private record Lock(List<Integer> heights) {

        public static Lock fromSchematic(List<List<Character>> schematic) {
            List<Integer> heights = new ArrayList<>();
            for (int c = 0; c < schematic.get(0).size(); c ++) {
                int colHeight = 0;
                for (int r = 0; r < schematic.size(); r++) {
                    if (!schematic.get(r).get(c).equals('#')) {
                        break;
                    }

                    colHeight++;
                }

                heights.add(colHeight - 1);
            }

            return new Lock(heights);
        }

        public boolean fits(Key key) {
            for (int i = 0; i < this.heights().size(); i++) {
                if (this.heights().get(i) + key.heights().get(i) > 5) {
                    return false;
                }
            }

            return true;
        }

    }
    
    private record Key(List<Integer> heights) {

        public static Key fromSchematic(List<List<Character>> schematic) {
            List<Integer> heights = new ArrayList<>();
            for (int c = 0; c < schematic.get(0).size(); c ++) {
                int colHeight = 0;
                for (int r = schematic.size() - 1; r >= 0; r--) {
                    if (!schematic.get(r).get(c).equals('#')) {
                        break;
                    }

                    colHeight++;
                }

                heights.add(colHeight - 1);
            }

            return new Key(heights);
        }

    }

}
