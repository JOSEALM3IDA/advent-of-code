package pt.josealm3ida.aoc2022.day11;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Monkey implements Comparable<Monkey> {

    private static boolean isVeryWorrying;
    private static int maxWorry;

    public static void setIsVeryWorrying(boolean isVeryWorrying) {
        Monkey.isVeryWorrying = isVeryWorrying;
    }

    public static void setMaxWorry(int maxWorry) {
        Monkey.maxWorry = maxWorry;
    }

    public static Monkey getMonkey(List<String> monkeyInfo) {
        String startingItemsStr = monkeyInfo.get(1).substring(18);
        String[] startingItemsSplit = startingItemsStr.split(", ");
        Queue<Long> itemQueue = new PriorityQueue<>();
        for (String s : startingItemsSplit) itemQueue.add(Long.parseLong(s));

        String operationStr = monkeyInfo.get(2);
        String[] operationStrSplit = operationStr.trim().split(" ");

        String operationMemberStr = operationStrSplit[5];
        int operationMember = operationMemberStr.equals("old") ? -1 : Integer.parseInt(operationMemberStr);

        Operation operation = Operation.getOperation(operationMember == -1 ? '?' : operationStrSplit[4].charAt(0));

        String testStr = monkeyInfo.get(3);
        String[] testStrSplit = testStr.split(" ");
        int testDivisibleBy = Integer.parseInt(testStrSplit[testStrSplit.length - 1]);

        String testTrueStr = monkeyInfo.get(4);
        String testFalseStr = monkeyInfo.get(5);
        int[] testResult = new int[]{
                Integer.parseInt(testTrueStr.substring(testTrueStr.length() - 1)),
                Integer.parseInt(testFalseStr.substring(testFalseStr.length() - 1))
        };

        return new Monkey(itemQueue, operation, operationMember, testDivisibleBy, testResult);
    }

    private final Queue<Long> itemQueue;
    private final Operation operation;
    private final int operationMember;
    private final int testDivisible;
    private final int[] testResult;

    private int nItemsInspected = 0;

    private Monkey(Queue<Long> itemQueue, Operation operation, int operationMember, int testDivisibleBy, int[] testResult) {
        this.itemQueue = itemQueue;
        this.operation = operation;
        this.operationMember = operationMember;
        this.testDivisible = testDivisibleBy;
        this.testResult = testResult;
    }

    public void throwAt(Long item) {
        itemQueue.add(item);
    }

    public void executeTurn(List<Monkey> monkeyList) {
        while (!itemQueue.isEmpty()) {
            nItemsInspected++;
            long item = operation.applyOperation(itemQueue.poll(), operationMember);

            if (!Monkey.isVeryWorrying) item = (int) Math.floor(item / 3.0);

            item %= maxWorry;

            monkeyList.get(item % testDivisible == 0 ? testResult[0] : testResult[1]).throwAt(item);
        }
    }

    public int getNItemsInspected() {
        return nItemsInspected;
    }

    public int getTestDivisible() {
        return testDivisible;
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "itemQueue=" + itemQueue +
                ", operation=" + operation +
                ", operationMember=" + operationMember +
                ", testDivisible=" + testDivisible +
                ", testResult=" + Arrays.toString(testResult) +
                ", nItemsInspected=" + nItemsInspected +
                '}';
    }

    @Override
    public int compareTo(Monkey m) {
        return getNItemsInspected() - m.getNItemsInspected();
    }
}
