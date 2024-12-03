package pt.josealm3ida.aoc.mull_it_over;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<String> lines = Utils.readLines("input/mull-it-over.txt"); 
        String joinedInput = String.join("\n", lines);

        System.out.println(part1(joinedInput));
        System.out.println(part2(joinedInput));
    }

    private static int part1(String line) {
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        Matcher matcher = pattern.matcher(line);

        int sum = 0;
        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));

            sum += x * y;
        }

        return sum;
    }

    private static int part2(String line) {
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        Matcher matcher = pattern.matcher(line);

        int sum = 0;
        int lastMatchEndIdx = 0;
        boolean isDo = true;
        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));

            String strBetween = line.substring(lastMatchEndIdx, matcher.start());
            
            int lastIndexDo = strBetween.lastIndexOf("do()");
            int lastIndexDont = strBetween.lastIndexOf("don't()");

            if (lastIndexDo > lastIndexDont) {
                isDo = true;
            } else if (lastIndexDont > lastIndexDo) {
                isDo = false;
            }

            if (isDo) {
                sum += x * y;
            }
        }

        return sum;
    }

}
