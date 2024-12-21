package pt.josealm3ida.aoc.chronospatial_computer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<String> lines = Utils.readLines("input/chronospatial-computer.txt"); 
        Long regA = Long.valueOf(Utils.readAllIntegersInString(lines.get(0)).get(0));
        List<Integer> program = Utils.readAllIntegersInString(lines.get(4));

        System.out.println(part1(regA, program));
        System.out.println(part2(program));
    }

    private static String part1(Long initialRegA, List<Integer> program) {
        return runProgram(initialRegA, program);
    }

    private static Long part2(List<Integer> program) {
        return Long.parseLong(bfs(program).get(0), 8);
    }

    private static List<String> bfs(List<Integer> program) {
        Queue<String> toVisit = new LinkedList<>();
        toVisit.add("1");
        toVisit.add("2");
        toVisit.add("3");
        toVisit.add("4");
        toVisit.add("5");
        toVisit.add("6");
        toVisit.add("7");

        String expected = String.join(",", program.stream().map(String::valueOf).toList());

        List<String> allPossibilities = new ArrayList<>();

        while (!toVisit.isEmpty()) {
            String curr = toVisit.poll();

            if (curr.length() > program.size()) {
                continue;
            }

            String output = runProgram(Long.valueOf(curr, 8), program);

            if (output.equals(expected)) {
                allPossibilities.add(curr);
                continue;
            }

            String expectedSubstr = expected.substring(expected.length() - output.length(), expected.length());
            if (!output.equals(expectedSubstr)) {
                continue;
            }

            toVisit.add(curr + "0");
            toVisit.add(curr + "1");
            toVisit.add(curr + "2");
            toVisit.add(curr + "3");
            toVisit.add(curr + "4");
            toVisit.add(curr + "5");
            toVisit.add(curr + "6");
            toVisit.add(curr + "7");
        }

        return allPossibilities;
    } 

    private static String runProgram(Long initialRegA, List<Integer> program) {
        List<Long> registers = new ArrayList<>(List.of(initialRegA, 0l, 0l));
        List<Integer> output = new ArrayList<>();

        int i = 0;
        while (true) {
            if (i >= program.size()) {
                break;
            }

            int literalOperand = program.get(i + 1);
            long comboOperand = switch (literalOperand) {
                case 4 -> registers.get(0);
                case 5 -> registers.get(1);
                case 6 -> registers.get(2);
                case 7 -> Long.MAX_VALUE; 
                default -> literalOperand;
            };

            int opcode = program.get(i);
            switch (opcode) {
                case 0: // adv
                    registers.set(0, registers.get(0) / (int) Math.pow(2, comboOperand));
                    break;

                case 1: // bxl
                    registers.set(1, registers.get(1) ^ literalOperand);
                    break;

                case 2: // bst
                    registers.set(1, comboOperand & 0x7);
                    break;

                case 3: // jnz
                    if (registers.get(0) != 0) {
                        i = literalOperand;
                        i -= 2;
                    }
                    break;

                case 4: // bxc
                    registers.set(1, registers.get(1) ^ registers.get(2));
                    break;

                case 5: // out
                    output.add((int) (comboOperand & 0x7));
                    break;

                case 6: // bdv
                    registers.set(1, registers.get(0) / (int) Math.pow(2, comboOperand));
                    break;

                case 7: // cdv
                    registers.set(2, registers.get(0) / (int) Math.pow(2, comboOperand));
                    break;
            }

            i += 2;
        }

        return String.join(",", output.stream().map(String::valueOf).toList());
    }

}
