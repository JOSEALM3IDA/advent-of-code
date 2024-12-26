package pt.josealm3ida.aoc.keypad_conundrum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Direction;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    private static final int NUM_DIRECTIONAL_KEYPADS_PT1 = 2;
    private static final int NUM_DIRECTIONAL_KEYPADS_PT2 = 25;

    private static final List<List<Character>> KEYPAD = List.of(
        List.of('#', '0' , '.'),
        List.of('1', '2' , '3'),
        List.of('4', '5' , '6'),
        List.of('7', '8' , '9')
    );

    public static void main(String[] args) {
        List<Code> codes = Utils.readLines("input/keypad-conundrum.txt").stream().map(s -> Code.from(s)).toList();

        System.out.println(part1(codes));
        System.out.println(part2(codes));
    }

    private static long part1(List<Code> codes) {
        return calculateComplexity(codes, NUM_DIRECTIONAL_KEYPADS_PT1);
    }

    private static long part2(List<Code> codes) {
        return calculateComplexity(codes, NUM_DIRECTIONAL_KEYPADS_PT2);
    }

    private static long calculateComplexity(List<Code> codes, int numDirectionalKeypads) {
        long totalComplexity = 0;

        Map<List<Integer>, Long> lengthMap = new HashMap<>();
        for (Code code : codes) {
            List<Sequence> directions = getShortestSequences(code).stream()
                .map(s -> new Sequence(s.seq().stream().map(d -> {
                        if (d == null) {
                            return d;
                        }

                        return switch (d) {
                            case Direction.UP -> Direction.DOWN;
                            case Direction.DOWN -> Direction.UP;
                            default -> d;
                        };
                    }).toList())
                ).toList();

            long optimal = Long.MAX_VALUE;
            for (Sequence s : directions) {
                long len = 0;
                Code subCode = Code.from(s);
                int prevKey = -1;
                for (int i = 0; i < subCode.cod().size(); i++) {
                    int currKey = subCode.cod().get(i); 
                    len += compute(lengthMap, prevKey, currKey, numDirectionalKeypads); 

                    prevKey = currKey;
                }

                optimal = Math.min(optimal, len);
            }

            StringBuilder sb = new StringBuilder();
            for (int nr : code.cod().subList(0, code.cod().size() - 1)) {
                sb.append(nr);
            }

            totalComplexity += optimal * Integer.valueOf(sb.toString());
        }

        return totalComplexity;
    }

	private static List<Sequence> getShortestSequences(Code code) {
        List<Sequence> sequences = new ArrayList<>();
        sequences.add(new Sequence(Collections.emptyList()));

        int prevKey = -1;
        for (int i = 0; i < code.cod().size(); i++) {
            int nextKey = code.cod().get(i);

            List<Sequence> shortestPaths = getShortestPathsToKey(prevKey, nextKey);
            List<Sequence> newSequences = new ArrayList<>();

            for (Sequence path : shortestPaths) {
                sequences.forEach(s -> {
                    Sequence newS = new Sequence(new ArrayList<>(s.seq()));
                    newS.seq().addAll(path.seq());

                    newSequences.add(newS);
                });
            }

            if (newSequences.isEmpty()) {
                sequences.forEach(s -> s.seq().add(null));
            } else {
                sequences = newSequences;
            }

            prevKey = nextKey;
        }

        return sequences;
	}

    private static long compute(Map<List<Integer>, Long> lengthMap, int startingKey, int endKey, int depth) {
        List<Integer> key = List.of(startingKey, endKey, depth);
        if (lengthMap.containsKey(key)) {
            return lengthMap.get(key);
        }

        List<Sequence> paths = getShortestPathsToKey(startingKey, endKey);

        if (depth == 1) {
            return (long) paths.get(0).seq().size();
        }

        if (paths.isEmpty()) {
            return 1l;
        }

        long optimal = Long.MAX_VALUE;
        for (Sequence path : paths) {
            if (path.seq().size() == 1) {
                return 1l;
            }

            long len = 0;
            Code code = Code.from(path);
            int prevKey = -1;
            for (int i = 0; i < code.cod().size(); i++) {
                int currKey = code.cod().get(i); 
                len += compute(lengthMap, prevKey, currKey, depth - 1);

                prevKey = currKey;
            }

            optimal = Math.min(optimal, len);
        }
        
        lengthMap.put(key, optimal);
        return optimal;
    }

    private static List<Sequence> getShortestPathsToKey(int startingKey, int endKey) {
        List<List<Coord>> shortestPaths = aStar(keyToCoord(startingKey), keyToCoord(endKey)).stream().map(MazePath::getPath).toList();

        List<Sequence> directionPaths = new ArrayList<>();

        for (List<Coord> shortestPath : shortestPaths) {
            if (shortestPath.size() == 1) {
                List<Direction> emptyDirections = new ArrayList<>();
                emptyDirections.add(null);
                return List.of(new Sequence(emptyDirections));
            }

            List<Direction> directionPath = new ArrayList<>();

            for (int i = 1; i < shortestPath.size(); i++) {
                Coord prev = shortestPath.get(i - 1);
                Coord curr = shortestPath.get(i);

                for (Direction d : Direction.values()) {
                    if (d.isDiagonal()) {
                        continue;
                    }

                    Coord c = new Coord(d.calcNextCol(prev.x()), d.calcNextRow(prev.y()));
                    if (c.equals(curr)) {
                        directionPath.add(d);
                        break;
                    }
                }
            }

            directionPath.add(null);
            directionPaths.add(new Sequence(directionPath));
        }

        return directionPaths;
    }

    private static Coord keyToCoord(int key) {
        int x = switch (key) {
            case 7, 4, 1 -> 0;
            case 8, 5, 2, 0 -> 1;
            case 9, 6, 3, -1 -> 2;
            default -> -1;
        };
        
        int y = switch (key) {
            case 0, -1 -> 0;
            case 1, 2, 3 -> 1;
            case 4, 5, 6 -> 2;
            case 7, 8, 9 -> 3;
            default -> -1;
        };

        return new Coord(x, y);
    }

    private static Character directionAsCharacter(Direction d) {
        return switch (d) {
            case LEFT -> '<';
            case UP -> '^';
            case DOWN -> 'v';
            case RIGHT -> '>';
            case null -> 'A';
            default -> '?';
        };
    }

    private static List<MazePath> aStar(Coord start, Coord end) {
        PriorityQueue<MazePath> queue = new PriorityQueue<>();
        queue.add(new MazePath(new ArrayList<>(List.of(start)), end));

        Map<Coord, Integer> visited = new HashMap<>();

        List<MazePath> bestPaths = new ArrayList<>();

        while (!queue.isEmpty()) {
            MazePath curr = queue.poll();
            Coord currCoord = curr.getPath().getLast();

            if (visited.containsKey(currCoord) && visited.get(currCoord) < curr.getCost()) {
                continue;
            }
           
            if (!visited.containsKey(currCoord)) {
                visited.put(currCoord, curr.getCost());
            }

            if (currCoord.equals(end)) {
                if (bestPaths.isEmpty() || bestPaths.get(0).getCost() == curr.getCost()) {
                    bestPaths.add(curr);
                }
                continue;
            }

            queue.addAll(getValidNeighbors(currCoord).stream().map(c -> {
                List<Coord> newPath = new ArrayList<>(curr.getPath());
                newPath.add(c);
                return new MazePath(newPath, end);
            }).toList());
        }

        return bestPaths;
    }

    private static List<Coord> getValidNeighbors(Coord c) {
        List<Coord> neighbors = new ArrayList<>();

        for (Direction d : Direction.values()) {
            if (d.isDiagonal()) {
                continue;
            }

            Coord neighbor = new Coord(d.calcNextCol(c.x()), d.calcNextRow(c.y()));
            if (neighbor.x() >= KEYPAD.get(0).size() || neighbor.x() < 0
                || neighbor.y() >= KEYPAD.size() || neighbor.y() < 0
                || KEYPAD.get(neighbor.y()).get(neighbor.x()).equals('#')
            ) {
                continue;
            }

            neighbors.add(neighbor);
        }

        return neighbors;
    }

    private record Code(List<Integer> cod) {

        public static Code from(String s) {
            return new Code(s.chars().mapToObj(c -> (char) c).map(c -> keyFromChar(c)).toList());
        }

        public static Code from(Sequence s) {
            return new Code(s.seq().stream().map(Main::directionAsCharacter).map(Code::keyFromChar).toList());
        }

        private static int keyFromChar(char c) {
            return switch(c) {
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> Character.digit(c, 10);
                case '<' -> 1;
                case '^' -> 0;
                case 'v' -> 2;
                case '>' -> 3;
                case 'A' -> -1;
                default -> -2;
            };
        }

    }

    private record Sequence(List<Direction> seq) {

		@Override
		public String toString() {
            StringBuilder sb = new StringBuilder();

            seq.forEach(d -> sb.append(directionAsCharacter(d)));

            return sb.toString();
		}

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

}
