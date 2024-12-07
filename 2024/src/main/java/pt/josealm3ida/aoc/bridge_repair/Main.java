package pt.josealm3ida.aoc.bridge_repair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<List<Long>> numbers = parseLines(Utils.readLines("input/bridge-repair.txt"));
        System.out.println(calculateTotalCalibrationResult(numbers, false));
        System.out.println(calculateTotalCalibrationResult(numbers, true));
    }

    private static List<List<Long>> parseLines(List<String> lines) {
        return lines.stream().map(l -> Arrays.stream(l.replace(":", "").split(" ")).map(n -> Long.parseLong(n)).toList()).toList();
    }

    private static long calculateTotalCalibrationResult(List<List<Long>> numbers, boolean includeThirdOperator) {
        long totalCalibrationResult = 0;

        for (List<Long> ns : numbers) {
            List<List<Operator>> possibleSolutions = getSolutions(
                ns.get(0),
                ns.subList(2, ns.size()),
                ns.get(1),
                includeThirdOperator
            );

            if (possibleSolutions != null) {
                totalCalibrationResult += ns.get(0);
                possibleSolutions.forEach(s -> Collections.reverse(s));
            }
        }

        return totalCalibrationResult;
    }

    private static List<List<Operator>> getSolutions(long testValue, List<Long> remainingValues, long currValue, boolean includeThirdOperator) {
        boolean hasRemainingValues = !remainingValues.isEmpty();
        if (!hasRemainingValues && currValue == testValue) {
            return new ArrayList<>();
        }

        if (currValue > testValue) {
            return null;
        }

        if (!hasRemainingValues) {
            return null;
        }

        List<List<Operator>> solutions = new ArrayList<>();
        for (Operator o : Operator.values()) {
            if (!includeThirdOperator && o == Operator.CONCAT) {
                continue;
            }

            long possibleNextValue = o.apply(currValue, remainingValues.get(0));

            List<List<Operator>> possibleSolutions = getSolutions(testValue, remainingValues.subList(1, remainingValues.size()), possibleNextValue, includeThirdOperator);
            if (possibleSolutions == null) {
                continue;
            }

            if (possibleSolutions.size() == 0) {
                possibleSolutions.add(new ArrayList<>());
            }

            for (List<Operator> s : possibleSolutions) {
                s.add(o);
            }

            solutions.addAll(possibleSolutions);
        }

        return solutions.isEmpty() ? null : solutions;
    }

    private enum Operator {
        ADD,
        MULTIPLY,
        CONCAT;

        public long apply(long n1, long n2) {
            return switch (this) {
                case ADD -> n1 + n2;
                case MULTIPLY -> n1 * n2;
                case CONCAT -> Long.parseLong(Long.toString(n1) + Long.toString(n2));
            };
        }
    }

}
