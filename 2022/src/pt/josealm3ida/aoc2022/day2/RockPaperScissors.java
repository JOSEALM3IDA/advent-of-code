package pt.josealm3ida.aoc2022.day2;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;

public class RockPaperScissors {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day2/input.txt";

    public static final int POINTS_LOSS = 0;
    public static final int POINTS_DRAW = 3;
    public static final int POINTS_WIN = 6;

    public static char convertToShape(char strategy) {
        return switch (strategy) {
            case 'X' -> 'A'; // Rock
            case 'Y' -> 'B'; // Paper
            case 'Z' -> 'C'; // Scissors
            default -> '?';
        };
    }

    public static char convertFromResult(char o, char s) {
        if (s == 'Y') return o;

        if (o == 'A') {
            if (s == 'X') return 'C';
            else return 'B';
        }

        if (o == 'B') {
            if (s == 'X') return 'A';
            else return 'C';
        }

        if (o == 'C') {
            if (s == 'X') return 'B';
            else return 'A';
        }

        return '?';
    }

    public static int getShapePoints(char shape) {
        return switch (shape) {
            case 'A' -> 1; // Rock
            case 'B' -> 2; // Paper
            case 'C' -> 3; // Scissors
            default -> 0;
        };
    }

    public static int getPoints(char o, char s) {
        if (o == s) return POINTS_DRAW + getShapePoints(s);
        if (o == 'A' && s == 'B') return POINTS_WIN + getShapePoints(s);
        if (o == 'A' && s == 'C') return POINTS_LOSS + getShapePoints(s);
        if (o == 'B' && s == 'A') return POINTS_LOSS + getShapePoints(s);
        if (o == 'B' && s == 'C') return POINTS_WIN + getShapePoints(s);
        if (o == 'C' && s == 'A') return POINTS_WIN + getShapePoints(s);
        if (o == 'C' && s == 'B') return POINTS_LOSS + getShapePoints(s);

        return 0;
    }

    public static int getTotalPoints1(String[] input) {
        int totalPoints = 0;

        for (String line : input) {
            String[] shapesStr = line.split(" ");
            char o = shapesStr[0].charAt(0);
            char s = convertToShape(shapesStr[1].charAt(0));

            totalPoints += getPoints(o, s);
        }

        return totalPoints;
    }

    public static int getTotalPoints2(String[] input) {
        int totalPoints = 0;

        for (String line : input) {
            String[] shapesStr = line.split(" ");
            char o = shapesStr[0].charAt(0);
            char s = shapesStr[1].charAt(0);

            totalPoints += getPoints(o, convertFromResult(o, s));
        }

        return totalPoints;
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        System.out.println("Total points for strategy (part 1): " + getTotalPoints1(input));
        System.out.println("Total points for strategy (part 2): " + getTotalPoints2(input));
    }
}
