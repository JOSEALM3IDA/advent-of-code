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

    private static final List<List<Character>> NUMERIC_KEY_PAD = List.of(
        List.of('7', '8' , '9'),
        List.of('4', '5' , '6'),
        List.of('1', '2' , '3'),
        List.of('#', '0' , '.')
    );

    public static void main(String[] args) {
        List<List<Character>> codes = Utils.readCharacters("input/keypad-conundrum.txt");

        System.out.println(part1(codes));
    }

    private static int part1(List<List<Character>> codes) {
        int totalComplexity = 0;
        for (List<Character> code : codes) {
            List<Sequence> directions = getShortestSequences(code.stream().map(Main::keyToInt).toList());
    
            for (int i = 0; i < 2; i++) {
                List<Sequence> newDirections = new ArrayList<>();
                for (Sequence prevRobotDirections : directions) {
                    List<Integer> newCode = prevRobotDirections.toString().chars().mapToObj(c -> (char) c).map(Main::keyToInt).toList();
                    List<Sequence> newSequences = getShortestSequences(newCode).stream().map(s -> 
                        new Sequence(s.seq().stream().map(d -> {
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

                    newDirections.addAll(newSequences);
                }

                directions = newDirections;
            }

            int shortestSize = Integer.MAX_VALUE;

            for (Sequence finalDirections : directions) {
                int currSize = finalDirections.seq().size();
                shortestSize = currSize < shortestSize ? currSize : shortestSize;
            }

            final int ss = shortestSize;
            directions.removeIf(s -> s.seq().size() > ss);

            StringBuilder sb = new StringBuilder();
            for (Character ch : code.subList(0, code.size() - 1)) {
                sb.append(ch);
            }
            
            totalComplexity += shortestSize * Integer.valueOf(sb.toString());
            System.out.println(totalComplexity);
        }

        return totalComplexity;
    }

	private static List<Sequence> getShortestSequences(List<Integer> code) {
        List<Sequence> sequences = new ArrayList<>();
        sequences.add(new Sequence(Collections.emptyList()));

        int prevKey = -1;
        for (int i = 0; i < code.size(); i++) {
            int nextKey = code.get(i);

            List<Sequence> shortestPaths = getShortestPathsToKey(prevKey, nextKey);
            List<Sequence> newSequences = new ArrayList<>();

            for (Sequence path : shortestPaths) {
                sequences.forEach(s -> {
                    Sequence newS = new Sequence(new ArrayList<>(s.seq()));
                    newS.seq().addAll(path.seq());
                    newS.seq().add(null);

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

    // if part 2 doesnt work, could mean that I need all paths, not just shortest (due to optimizations some robots up)
    private static List<Sequence> getShortestPathsToKey(int startingKey, int endKey) {
        List<List<Coord>> shortestPaths = aStar(keyToCoord(startingKey), keyToCoord(endKey)).stream().map(MazePath::getPath).toList();

        List<Sequence> directionPaths = new ArrayList<>();

        for (List<Coord> shortestPath : shortestPaths) {
            if (shortestPath.size() == 1) {
                return Collections.emptyList();
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

            directionPaths.add(new Sequence(directionPath));
        }

        return directionPaths;
    }

    private static int keyToInt(Character key) {
        return switch(key) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> Character.digit(key, 10);
            case '<' -> 1;
            case '^' -> 0;
            case 'v' -> 2;
            case '>' -> 3;
            case 'A' -> -1;
            default -> -2;
        };
    }

    private static Coord keyToCoord(int key) {
        int x = switch (key) {
            case 7, 4, 1 -> 0;
            case 8, 5, 2, 0 -> 1;
            case 9, 6, 3, -1 -> 2;
            default -> -1;
        };
        
        int y = switch (key) {
            case 7, 8, 9 -> 0;
            case 4, 5, 6 -> 1;
            case 1, 2, 3 -> 2;
            case 0, -1 -> 3;
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
            if (neighbor.x() >= NUMERIC_KEY_PAD.get(0).size() || neighbor.x() < 0
                || neighbor.y() >= NUMERIC_KEY_PAD.size() || neighbor.y() < 0
                || NUMERIC_KEY_PAD.get(neighbor.y()).get(neighbor.x()).equals('#')
            ) {
                continue;
            }

            neighbors.add(neighbor);
        }

        return neighbors;
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
