package pt.josealm3ida.aoc2022.day9;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RopeBridge {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day9/input.txt";

    public static char getCharFor(int x, int y, int[] headPos, int[][] knots) {
        if (headPos[0] == x && headPos[1] == y) return 'H';

        for (int i = 0; i < knots.length; i++) {
            int[] knot = knots[i];
            if (knot[0] == x && knot[1] == y) return String.valueOf(i + 1).charAt(0);
        }

        return (x == 0 && y == 0) ? 's' : '.';
    }

    // HARDCODED GRID, ONLY WORKS FOR PART 2 LARGE EXAMPLE!
    public static void drawSimulation(int[] headPos, int[][] knots) {
        for (int i = 21; i >= 0; i--) {
            for (int j = 0; j < 26; j++) {
                System.out.print(getCharFor(j - 11, i - 5, headPos, knots));
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void moveTail(int[] headPos, int[] tailPos) {
        int[] diffVector = new int[]{tailPos[0] - headPos[0], tailPos[1] - headPos[1]};

        if (Math.abs(diffVector[0]) <= 1 && Math.abs(diffVector[1]) <= 1) return; // No movement needed

        if (diffVector[0] == 2 && diffVector[1] == 2) { tailPos[0]--; tailPos[1]--; return; } // Too much up-right, must move down-left
        if (diffVector[0] == -2 && diffVector[1] == 2) { tailPos[0]++; tailPos[1]--; return; } // Too much up-left, must move down-right

        if (diffVector[0] == 2 && diffVector[1] == -2) { tailPos[0]--; tailPos[1]++; return; } // Too much down-right, must move up-left
        if (diffVector[0] == -2 && diffVector[1] == -2) { tailPos[0]++; tailPos[1]++; return; } // Too much down-left, must move up-right

        if (diffVector[0] == 2) tailPos[0]--; // Too much right, must move left
        else if (diffVector[0] == -2) tailPos[0]++; // Too much left, must move right
        if (Math.abs(diffVector[0]) == 2) tailPos[1] = headPos[1]; // Maybe also update diagonal

        if (diffVector[1] == 2) tailPos[1]--; // Too much up, must move down
        else if (diffVector[1] == -2) tailPos[1]++; // Too much down, must move up
        if (Math.abs(diffVector[1]) == 2) tailPos[0] = headPos[0]; // Maybe also update diagonal
    }

    public static int[] motionToVector(char motion) {
        return switch (motion) {
            case 'R' -> new int[]{1, 0};
            case 'L' -> new int[]{-1, 0};
            case 'U' -> new int[]{0, 1};
            case 'D' -> new int[]{0, -1};
            default -> throw new IllegalStateException("Unexpected value: " + motion);
        };
    }

    public static int getTailNUniquePositions(String[] input, int nKnots) {
        int[] headPos = {0, 0};

        int[][] knots = new int[nKnots - 1][];
        for (int i = 0; i < nKnots - 1; i++) knots[i] = new int[]{0, 0};

        Set<String> visitedSet = new HashSet<>();

        for (String s : input) {
            String[] sSplit = s.split(" ");

            int[] motionVector = motionToVector(sSplit[0].charAt(0));
            int nSteps = Integer.parseInt(sSplit[1]);

            while (nSteps > 0) {
                headPos[0] += motionVector[0]; headPos[1] += motionVector[1];

                int[] currHead = headPos;
                for (int[] knot : knots) {
                    moveTail(currHead, knot);
                    currHead = knot;
                }

                visitedSet.add(knots[knots.length - 1][0] + "," + knots[knots.length - 1][1]);
                nSteps--;
            }

            // drawSimulation(headPos, knots);
        }

        return visitedSet.size();
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        System.out.println("Tail unique visited positions with 2 knots: " + getTailNUniquePositions(input, 2));
        System.out.println("Tail unique visited positions with 10 knots: " + getTailNUniquePositions(input, 10));
    }

}
