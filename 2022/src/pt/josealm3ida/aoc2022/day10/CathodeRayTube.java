package pt.josealm3ida.aoc2022.day10;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;

public class CathodeRayTube {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day10/input.txt";

    public static boolean isCycleRelevantSum(int cycle) {
        return ((cycle + 20) % 40) == 0 && cycle <= 220;
    }

    public static int getSumStrengths(String[] input) {
        int X = 1;
        int cycle = 1;
        int signalStrength = 0;

        for (String s : input) {
            String[] sSplit = s.split(" ");

            String instruction = sSplit[0];

            if (isCycleRelevantSum(cycle)) {
                System.out.println("Strength at cycle " + cycle + ": " + cycle * X);
                signalStrength += cycle * X;
            }

            cycle++;
            if (instruction.equals("noop")) continue;

            if (isCycleRelevantSum(cycle)) {
                System.out.println("Strength at cycle " + cycle + ": " + cycle * X);
                signalStrength += cycle * X;
            }

            cycle++;
            X += Integer.parseInt(sSplit[1]);;
        }

        return signalStrength;
    }

    public static String drawLetters(String[] input) {
        int X = 1;
        int cycle = 1;

        StringBuilder sb = new StringBuilder();

        for (String s : input) {
            String[] sSplit = s.split(" ");

            String instruction = sSplit[0];

            int drawnPixel = (cycle - 1) % 40;
            if (drawnPixel == 0) sb.append('\n');
            sb.append(Math.abs(drawnPixel - X) <= 1 ? '#' : '.');

            cycle++;
            if (instruction.equals("noop")) continue;

            drawnPixel = (cycle - 1) % 40;
            if (drawnPixel == 0) sb.append("\n");
            sb.append(Math.abs(drawnPixel - X) <= 1 ? '#' : '.');

            cycle++;
            X += Integer.parseInt(sSplit[1]);;
        }

        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        System.out.println("Sum of signal strengths: " + getSumStrengths(input));
        System.out.println("Image output: " + drawLetters(input));
    }

}
