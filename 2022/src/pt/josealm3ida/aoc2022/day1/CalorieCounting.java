package pt.josealm3ida.aoc2022.day1;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalorieCounting {

    public static final String filePath = "src/pt/josealm3ida/aoc2022/day1/input.txt";

    public static int getNGreatestSum(String[] input, int n) {
        List<Integer> allElves = new ArrayList<>();

        int curr = 0;
        for (String line : input) {
            if (line.isEmpty()) {
                allElves.add(curr);
                curr = 0;
                continue;
            }

            curr += Integer.parseInt(line);
        }

        return allElves.stream().sorted(Collections.reverseOrder()).limit(n).mapToInt(Integer::intValue).sum();
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(filePath);

        System.out.println("Top elf: " + getNGreatestSum(input, 1));
        System.out.println("Top 3 elves: " + getNGreatestSum(input, 3));
    }
}
