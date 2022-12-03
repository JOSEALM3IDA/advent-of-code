package pt.josealm3ida.aoc2022.day3;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class RucksackReorganization {

    public static final String filePath = "src/pt/josealm3ida/aoc2022/day3/input.txt";

    public static int getItemPriority(char item) {
        return (int) item - (Character.isLowerCase(item) ? 96 : 38);
    }

    public static int getSumPriority(String rucksack) {
        int length = rucksack.length();

        String compt1 = rucksack.substring(0, length/2);
        String compt2 = rucksack.substring(length/2, length);

        Set<Character> setCompt1 = compt1.chars().mapToObj(i -> (char) i).collect(Collectors.toSet());
        Set<Character> setCompt2 = compt2.chars().mapToObj(i -> (char) i).collect(Collectors.toSet());
        setCompt1.retainAll(setCompt2);

        int sumPriority = 0;
        for (char c : setCompt1) sumPriority += getItemPriority(c);

        return sumPriority;
    }

    public static int getSumAllPriorities1(String[] input) {
        int totalPriority = 0;
        for (String s : input) totalPriority += getSumPriority(s);
        return totalPriority;
    }

    public static int getSumAllPriorities2(String[] input) {
        int totalPriority = 0;

        for (int i = 0; i < input.length; i += 3) {
            Set<Character> commonItemSet = input[i].chars().mapToObj(k -> (char) k).collect(Collectors.toSet());
            for (int j = 1; j < 3; j++) {
                commonItemSet.retainAll(input[i + j].chars().mapToObj(k -> (char) k).collect(Collectors.toSet()));
            }

            totalPriority += getItemPriority(commonItemSet.iterator().next());
        }

        return totalPriority;
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(filePath);

        System.out.println("Priority sum of common compartment items: " + getSumAllPriorities1(input));
        System.out.println("Priority sum of item badges: " + getSumAllPriorities2(input));
    }
}
