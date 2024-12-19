package pt.josealm3ida.aoc.reindeer_maze;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Direction;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<List<Character>> maze = Utils.readCharacters("input/reindeer-maze.txt");
        List<Coord> startEnd = getStartEnd(maze);
        List<MazePath> bestPaths = aStar(maze, startEnd.get(0), startEnd.get(1));
        prettyPrint(maze, bestPaths);
        System.out.println(part1(bestPaths));
        System.out.println(part2(bestPaths));
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

    private static int part1(List<MazePath> bestPaths) {
        return bestPaths.get(0).getCost();
    }

    private static int part2(List<MazePath> bestPaths) {
        return (new HashSet<>(bestPaths.stream().flatMap(mp -> mp.getPath().stream()).toList())).size();
    }

    private static List<MazePath> aStar(List<List<Character>> maze, Coord start, Coord end) {
        PriorityQueue<MazePath> queue = new PriorityQueue<>();
        queue.add(new MazePath(new ArrayList<>(List.of(start)), end));

        Map<Coord, Integer> visited = new HashMap<>();

        List<MazePath> bestPaths = new ArrayList<>();

        while (!queue.isEmpty()) {
            MazePath curr = queue.poll();
            Coord currCoord = curr.getPath().getLast();

            if (visited.containsKey(currCoord) && visited.get(currCoord) + 1000 < curr.getCost()) { // + 1000 -> hack because of rotations possibly not yet counted
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

            queue.addAll(getValidNeighbors(maze, currCoord).stream().map(c -> {
                List<Coord> newPath = new ArrayList<>(curr.getPath());
                newPath.add(c);
                return new MazePath(newPath, end);
            }).toList());
        }

        return bestPaths;
    }

    private static List<Coord> getValidNeighbors(List<List<Character>> maze, Coord c) {
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


    private static void prettyPrint(List<List<Character>> maze, List<MazePath> mps) {
        Set<Coord> visitedCoords = new HashSet<>(mps.stream().flatMap(mp -> mp.getPath().stream()).toList());

        for (int y = 0; y < maze.size(); y++) {
            for (int x = 0; x < maze.get(y).size(); x++) {
                Character toPrint = maze.get(y).get(x);
                if (toPrint == '.' && visitedCoords.contains(new Coord(x, y))) {
                    toPrint = 'O';
                }
                System.out.print(toPrint);
            }
            System.out.println();
        }
    }

    
    private static class MazePath implements Comparable<MazePath> {

        private final List<Coord> path;
        private final Coord end;
        private final int cost;

        public MazePath(List<Coord> path, Coord end) {
            this.path = path;
            this.end = end;
            this.cost = calcCost();
        }

		public List<Coord> getPath() {
			return path;
		}

        public int getCost() {
            return this.cost;
        }

        public int calcCost() {
            if (path.size() == 0) {
                return 0;
            }

            int currCost = 0;

            for (int i = 1; i < path.size(); i++) {
                currCost += getMoveCostToNeighbor(path.subList(0, i), path.get(i));
            }

            return currCost;
        }

        public Direction getCurrDirection() {
            return getDirection(path);
		}
	
        public int expectedTotalCostToEnd() {
            return this.cost + 1000 * getMinDirectionChangesTo(path.getLast(), getCurrDirection(), end) + path.getLast().getMDistanceTo(end);
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

        private static int getMinDirectionChangesTo(Coord curr, Direction currDirection, Coord c) {
            int endQuadrant = c.getQuadrant(curr);
            int i = switch (endQuadrant) {
                case 0 -> currDirection == Direction.LEFT || currDirection == Direction.UP ? 1 : 2;
                case 1 -> currDirection == Direction.RIGHT || currDirection == Direction.UP ? 1 : 2;
                case 2 -> currDirection == Direction.LEFT || currDirection == Direction.DOWN ? 1 : 2;
                case 3 -> currDirection == Direction.RIGHT || currDirection == Direction.DOWN ? 1 : 2;
                default -> {
                    if (currDirection == Direction.UP && curr.x() == c.x()) {
                        yield curr.y() < c.y() ? 2 : 0;
                    } else if (currDirection == Direction.DOWN && curr.x() == c.x()) {
                        yield curr.y() > c.y() ? 2 : 0;
                    } else if (currDirection == Direction.LEFT && curr.y() == c.y()) {
                        yield curr.x() < c.x() ? 2 : 0;
                    } else if (currDirection == Direction.RIGHT && curr.y() == c.y()) {
                        yield curr.x() > c.x() ? 2 : 0;
                    } else {
                        yield 1;
                    }
                }
            };

            return i;
        }

        public static int getMoveCostToNeighbor(List<Coord> pathUpToCurr, Coord c) {
            return 1 + 1000 * getMinDirectionChangesTo(pathUpToCurr.getLast(), getDirection(pathUpToCurr), c);
        }

        public static Direction getDirection(List<Coord> pathUpToCurr) {
            if (pathUpToCurr.size() <= 1) {
                return Direction.LEFT;
            }

            Direction currDirection = Direction.LEFT;
            Coord previous = pathUpToCurr.get(0);
            for (Coord curr : pathUpToCurr.subList(1, pathUpToCurr.size())) {
                int nx = currDirection.calcNextCol(previous.x());
                int ny = currDirection.calcNextRow(previous.y());
                if (curr.x() == nx && curr.y() == ny) {
                    previous = curr;
                    continue;
                }

                if (curr.x() < previous.x() && curr.y() == previous.y()) {
                    currDirection = Direction.LEFT;
                } else if (curr.x() > previous.x() && curr.y() == previous.y()) {
                    currDirection = Direction.RIGHT;
                } else if (curr.x() == previous.x() && curr.y() < previous.y()) {
                    currDirection = Direction.UP;
                } else if (curr.x() == previous.x() && curr.y() > previous.y()) {
                    currDirection = Direction.DOWN;
                }

                previous = curr;
            }
            
            return currDirection;
        }

    }

}
