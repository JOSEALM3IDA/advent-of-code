package pt.josealm3ida.aoc2022.day11;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonkeyInTheMiddle {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day11/input.txt";

    private static long calcMonkeyBusiness(String[] input, int nRounds, boolean isVeryWorrying) {
        List<Monkey> monkeyList = new ArrayList<>();
        List<String> monkeyInfo = new ArrayList<>();

        if (isVeryWorrying) Monkey.setIsVeryWorrying(isVeryWorrying);

        for (String s : input) {
            if (s.isEmpty()) {
                monkeyList.add(Monkey.getMonkey(monkeyInfo));
                monkeyInfo.clear();
                continue;
            }

            monkeyInfo.add(s);
        }

        monkeyList.add(Monkey.getMonkey(monkeyInfo));

        int maxWorry = 1;
        for (Monkey m : monkeyList) maxWorry *= m.getTestDivisible();
        Monkey.setMaxWorry(maxWorry);

        for (int i = 0; i < nRounds; i++) for (Monkey m : monkeyList) m.executeTurn(monkeyList);

        Collections.sort(monkeyList);
        Collections.reverse(monkeyList);

        return (long) monkeyList.get(0).getNItemsInspected() * monkeyList.get(1).getNItemsInspected();
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        System.out.println("Level of monkey business after 20 rounds: " + calcMonkeyBusiness(input, 20, false));
        System.out.println("Level of very worrying monkey business after 10000 rounds: " + calcMonkeyBusiness(input, 10000, true));
    }

}
