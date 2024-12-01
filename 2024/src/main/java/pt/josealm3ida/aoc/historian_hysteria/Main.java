package pt.josealm3ida.aoc.historian_hysteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(final String[] args) {
        List<List<Integer>> lines = parseLines(Utils.readLines("input/historian-hysteria.txt"));
        List<Integer> leftList = lines.get(0); 
        List<Integer> rightList = lines.get(1);

        System.out.println(part1(leftList, rightList));
        System.out.println(part2(leftList, rightList));
    }

    private static List<List<Integer>> parseLines(List<String> lines) {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        lines.forEach(l -> {
            String[] splits = l.split("   ");
            leftList.add(Integer.parseInt(splits[0]));
            rightList.add(Integer.parseInt(splits[1]));
        });

        return List.of(leftList, rightList);
    }

    private static int part1(List<Integer> leftList, List<Integer> rightList) {
        Collections.sort(leftList);
        Collections.sort(rightList);

        int sum = 0;

        for (int i = 0; i < leftList.size(); i++) {
            sum += Math.abs(leftList.get(i) - rightList.get(i));
        }

        return sum;
    }

    private static Map<Integer, Integer> getNrOccurrencesMap(List<Integer> list) {
        Map<Integer, Integer> nrOccurrencesMap = new HashMap<>();

        list.forEach(n -> {
            int nrOccurrences = Optional.ofNullable(nrOccurrencesMap.get(n)).orElse(0);
            nrOccurrencesMap.put(n, ++nrOccurrences);
        });

        return nrOccurrencesMap;
    }

    private static int part2(List<Integer> leftList, List<Integer> rightList) {
        Map<Integer, Integer> nrOccurrencesMap = getNrOccurrencesMap(rightList);

        return leftList.stream().map(n -> {
            int nrOccurrences = Optional.ofNullable(nrOccurrencesMap.get(n)).orElse(0);
            return n * nrOccurrences;
        }).mapToInt(Integer::intValue).sum();
    }

}
