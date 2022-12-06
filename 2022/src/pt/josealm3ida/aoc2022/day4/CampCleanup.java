package pt.josealm3ida.aoc2022.day4;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;

public class CampCleanup {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day4/input.txt";

    public static boolean isContained(int[] range1, int[] range2) {
        return range1[0] <= range2[0] && range1[1] >= range2[1];
    }

    public static boolean hasOverlap(int[] range1, int[] range2) {
        return ( range1[0] <= range2[0] && range1[1] >= range2[0] ) || ( range1[0] <= range2[1] && range1[1] >= range2[1] );
    }

    public static int[][] getRangesFromStr(String s) {
        String[] ranges = s.split(",");

        String[] range1str = ranges[0].split("-");
        String[] range2str = ranges[1].split("-");

        int[] range1 = { Integer.parseInt(range1str[0]), Integer.parseInt(range1str[1]) };
        int[] range2 = { Integer.parseInt(range2str[0]), Integer.parseInt(range2str[1]) };

        return new int[][]{range1, range2};
    }

    public static int getNContainedRanges(String[] input) {
        int nContained = 0;
        for (String s : input) {
            int[][] ranges = getRangesFromStr(s);
            if (isContained(ranges[0], ranges[1]) || isContained(ranges[1], ranges[0])) nContained++;
        }

        return nContained;
    }

    public static int getNOverlappedRanges(String[] input) {
        int nOverlapped = 0;
        for (String s : input) {
            int[][] ranges = getRangesFromStr(s);
            if (hasOverlap(ranges[0], ranges[1]) || hasOverlap(ranges[1], ranges[0])) nOverlapped++;
        }

        return nOverlapped;
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        System.out.println("Num of pairs with contained ranges: " + getNContainedRanges(input));
        System.out.println("Num of pairs with overlapping ranges: " + getNOverlappedRanges(input));
    }
}
