package pt.josealm3ida.aoc2022.day12;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.*;

public class HillClimbingAlgorithm {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day12/input.txt";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static final char START_MARKER = 'S';
    public static final char START_HEIGHT = 'a';

    public static final char END_MARKER = 'E';
    public static final char END_HEIGHT = 'z';

    public static void printVisitedAndPath(Set<String> visited, List<Node> path, char[][] input, int[][] startEnd) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {

                if (startEnd[0][0] == i && startEnd[0][1] == j) {
                    System.out.print(ANSI_WHITE_BACKGROUND + START_MARKER + ANSI_RESET);
                    continue;
                }

                if (startEnd[1][0] == i && startEnd[1][1] == j) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + END_MARKER + ANSI_RESET);
                    continue;
                }

                boolean inPath = false;

                for (Node n : path) {
                    if (n.getX() == i && n.getY() == j) {
                        inPath = true;
                        break;
                    }
                }

                System.out.print(inPath ? ANSI_GREEN_BACKGROUND + input[i][j] + ANSI_RESET : visited.contains(i + "," + j) ? ANSI_RED_BACKGROUND + input[i][j] + ANSI_RESET : input[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int[][] findStartAndEnd(char[][] input) {
        int[][] rtn = new int[2][];
        rtn[0] = new int[]{-1, -1};
        rtn[1] = new int[]{-1, -1};

        boolean foundStart = false;
        boolean foundEnd = false;

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                if (foundStart && foundEnd) break;

                if (input[i][j] == START_MARKER) {
                    foundStart = true;
                    rtn[0][0] = i; rtn[0][1] = j;
                } else if (input[i][j] == END_MARKER) {
                    foundEnd = true;
                    rtn[1][0] = i; rtn[1][1] = j;
                }
            }

            if (foundStart && foundEnd) break;
        }

        return rtn;
    }

    public static int calcFewestSteps(char[][] input) {
        int[][] startEnd = findStartAndEnd(input);

        Queue<Node> nodeQueue = new PriorityQueue<>();
        Set<String> toVisitCoordSet = new HashSet<>();
        input[startEnd[1][0]][startEnd[1][1]] = END_HEIGHT;

        nodeQueue.add(new Node(startEnd[0], START_HEIGHT, new ArrayList<>(), startEnd[1]));

        Node curr = null;
        while (!nodeQueue.isEmpty()) {
            curr = nodeQueue.poll();

            int x = curr.getX();
            int y = curr.getY();

            if (curr.isEnd()) break;

            int height = curr.getHeight();

            if (x + 1 < input.length && !toVisitCoordSet.contains((x + 1) + "," + (y)) && height - input[x + 1][y] >= -1) { // Down
                toVisitCoordSet.add((x + 1) + "," + (y));
                nodeQueue.add(new Node(new int[]{x + 1, y}, input[x + 1][y], curr.getPathToInclusive(), startEnd[1]));
            }

            if (x - 1 >= 0 && !toVisitCoordSet.contains((x - 1) + "," + (y)) && height - input[x - 1][y] >= -1) { // Up
                toVisitCoordSet.add((x - 1) + "," + (y));
                nodeQueue.add(new Node(new int[]{x - 1, y}, input[x - 1][y], curr.getPathToInclusive(), startEnd[1]));
            }

            if (y + 1 < input[x].length && !toVisitCoordSet.contains((x) + "," + (y + 1)) && height - input[x][y + 1] >= -1) { // Right
                toVisitCoordSet.add((x) + "," + (y + 1));
                nodeQueue.add(new Node(new int[]{x, y + 1}, input[x][y + 1], curr.getPathToInclusive(), startEnd[1]));
            }

            if (y - 1 >= 0 && !toVisitCoordSet.contains((x) + "," + (y - 1)) && height - input[x][y - 1] >= -1) { // Left
                toVisitCoordSet.add((x) + "," + (y - 1));
                nodeQueue.add(new Node(new int[]{x, y - 1}, input[x][y - 1], curr.getPathToInclusive(), startEnd[1]));
            }
        }

        printVisitedAndPath(toVisitCoordSet, curr.getPathToInclusive(), input, startEnd);
        return curr.isEnd() ? curr.getCostTo() : -1;
    }

    public static int getBestSmallestHikeSteps(char[][] input) {
        int[][] startEnd = findStartAndEnd(input);
        input[startEnd[0][0]][startEnd[0][1]] = START_HEIGHT;

        int smallest = Integer.MAX_VALUE;
        for (int i = 0; i < input.length; i++) {
            input[i][0] = START_MARKER;
            int steps = calcFewestSteps(deepCopy(input));
            input[i][0] = START_HEIGHT;

            smallest = Math.min(steps, smallest);
        }

        return smallest;
    }

    public static char[][] deepCopy(char[][] input) {
        return Arrays.stream(input).map(char[]::clone).toArray(char[][]::new);
    }

    public static void main(String[] args) throws IOException {
        char[][] input = FileReaderUtils.linesToCharMatrix(FILE_PATH);

        int fewestSteps = calcFewestSteps(deepCopy(input));
        int bestHikeDistance = getBestSmallestHikeSteps(deepCopy(input));
        System.out.println("Fewest steps to destination: " + fewestSteps);
        System.out.println("Best hike distance: " + bestHikeDistance);


    }

}
