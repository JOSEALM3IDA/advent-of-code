package pt.josealm3ida.aoc2022.day5;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.*;

public class SupplyStacks {

    public static final String filePath = "src/pt/josealm3ida/aoc2022/day5/input.txt";

    public static List<Stack<Character>> getStacks(List<String> stackStrings, int nStacks) {
        List<Stack<Character>> stackList = new ArrayList<>();
        for (int j = 0; j < nStacks; j++) {
            stackList.add(new Stack<>());
        }

        for (int j = stackStrings.size() - 1; j >= 0; j--) {
            for (int k = 0; k < nStacks; k++) {
                String stackString = stackStrings.get(j);
                if (k >= stackString.length()) continue;;

                char c = stackString.charAt(k);
                if (c == '-') continue;

                stackList.get(k).push(stackString.charAt(k));
            }
        }

        return stackList;
    }

    public static void applyInstructions(List<Stack<Character>> stackList, String[] instructions, boolean is9001) {
        for (String instruction : instructions) {
            String[] split = instruction.split(" ");

            int nElements = Integer.parseInt(split[1]);

            Stack<Character> sourceStack = stackList.get(Integer.parseInt(split[3]) - 1);
            Stack<Character> destinationStack = stackList.get(Integer.parseInt(split[5]) - 1);

            List<Character> movedCrates = new ArrayList<>();
            while (nElements > 0) {
                movedCrates.add(sourceStack.pop());
                nElements--;
            }

            if (is9001) Collections.reverse(movedCrates);

            for (char c : movedCrates) destinationStack.push(c);
        }
    }

    public static String buildTopCratesStr(List<Stack<Character>> stackList) {
        StringBuilder sb = new StringBuilder();
        for (char c : stackList.stream().map(Vector::lastElement).toList()) sb.append(c);
        return sb.toString();
    }

    public static String getTopCrates(String[] input, boolean is9001) {
        List<String> stackStrings = new ArrayList<>();

        String s = input[0];
        int i = 0;
        while (s.charAt(1) != '1') {
            stackStrings.add(s.replace("   ", "-").replaceAll("\\[([A-Z]+)]", "$1").replace(" ", ""));
            s = input[++i];
        }

        String trimmedStackIdLine = s.trim();
        int nStacks = Integer.parseInt(String.valueOf(trimmedStackIdLine.charAt(trimmedStackIdLine.length() - 1)));

        List<Stack<Character>> stackList = getStacks(stackStrings, nStacks);

        applyInstructions(stackList, Arrays.copyOfRange(input, i + 2, input.length), is9001);
        return buildTopCratesStr(stackList);
    }


    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(filePath);

        System.out.println("Crates on top (CrateMover 9000): " + getTopCrates(input, false));
        System.out.println("Crates on top (CrateMover 9001): " + getTopCrates(input, true));
    }
}
