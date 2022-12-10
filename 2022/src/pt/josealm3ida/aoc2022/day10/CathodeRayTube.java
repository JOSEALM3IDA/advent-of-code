package pt.josealm3ida.aoc2022.day10;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;

public class CathodeRayTube {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day10/input.txt";

    public static boolean isCycleRelevantSum(int cycle) {
        return ((cycle + 20) % 40) == 0 && cycle <= 220;
    }

    public static String getPixels(int X, int cycle) {
        int drawnPixel = (cycle - 1) % 40;
        return (drawnPixel == 0 ? "\n" : "") + (Math.abs(drawnPixel - X) <= 1 ? '#' : '.');
    }

    public static int getSumStrengths(String[] input) {
        int X = 1;
        int cycle = 1;
        int signalStrength = 0;
        StringBuilder sb = new StringBuilder();

        for (String s : input) {
            String[] sSplit = s.split(" ");

            String instruction = sSplit[0];

            sb.append(getPixels(X, cycle));
            if (isCycleRelevantSum(cycle)) signalStrength += cycle * X;

            cycle++;
            if (instruction.equals("noop")) continue;

            sb.append(getPixels(X, cycle));
            if (isCycleRelevantSum(cycle)) signalStrength += cycle * X;

            cycle++;
            X += Integer.parseInt(sSplit[1]);;
        }

        System.out.println("Image output:" + sb);
        return signalStrength;
    }


    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        System.out.println("Sum of signal strengths: " + getSumStrengths(input));
    }

}
