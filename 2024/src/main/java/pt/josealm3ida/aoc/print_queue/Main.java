package pt.josealm3ida.aoc.print_queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<String> lines = Utils.readLines("input/print-queue.txt");

        int i = lines.indexOf("");
        Map<Integer, List<Integer>> orderingRules = parseRules(lines.subList(0, i));
        List<List<Integer>> updates = parseUpdates(lines.subList(i + 1, lines.size()));

        System.out.println(part1(orderingRules, updates));
        System.out.println(part2(orderingRules, updates));
    }

    private static Map<Integer, List<Integer>> parseRules(List<String> orderingRulesStrList) {
        Map<Integer, List<Integer>> orderingRules = new HashMap<>();

        orderingRulesStrList.forEach(r -> {
            String[] rulesSplit = r.split("\\|");
            
            int key = Integer.parseInt(rulesSplit[0]);
            
            List<Integer> value = orderingRules.get(key);
            if (value == null) {
                value = new ArrayList<>();
                orderingRules.put(key, value);
            }

            value.add(Integer.parseInt(rulesSplit[1]));
        });

        return orderingRules;
    }

    private static List<List<Integer>> parseUpdates(List<String> updatesStrList) {
        List<List<Integer>> updates = new ArrayList<>();

        updatesStrList.forEach(u -> {
            List<Integer> update = (new ArrayList<>(Arrays.asList(u.split(",")))).stream().map(Integer::parseInt).toList();
            updates.add(update);
        });

        return updates;
    }

    private static int part1(Map<Integer, List<Integer>> orderingRules, List<List<Integer>> updates) {
        int sum = 0;
        
        for (List<Integer> u : updates) {
            if (isInRightOrder(orderingRules, u)) {
                sum += u.get(u.size() / 2);
            }
        }

        return sum;
    }

    private static int part2(Map<Integer, List<Integer>> orderingRules, List<List<Integer>> updates) {
        int sum = 0;
        
        List<List<Integer>> incorrectUpdates = new ArrayList<>();
        for (List<Integer> u : updates) {
            if (!isInRightOrder(orderingRules, u)) {
                incorrectUpdates.add(u);
            }
        }

        for (List<Integer> u : incorrectUpdates) {
            List<Integer> fixedUpdate = new ArrayList<>(u);
            Collections.sort(fixedUpdate, new PageComparator(orderingRules));
            sum += fixedUpdate.get(fixedUpdate.size() / 2);
        }

        return sum;
    }

    private static boolean isInRightOrder(Map<Integer, List<Integer>> orderingRules, List<Integer> update) {
        List<Integer> copyUpdate = new ArrayList<>(update);
        Collections.sort(copyUpdate, new PageComparator(orderingRules));
        return copyUpdate.equals(update);
    }

    public static class PageComparator implements Comparator<Integer> {

        Map<Integer, List<Integer>> orderingRules;

        public PageComparator(Map<Integer, List<Integer>> orderingRules) {
            this.orderingRules = orderingRules;
        }

        @Override
        public int compare(Integer i1, Integer i2) {
            if (i1 == i2) {
                return 0;
            }

            List<Integer> nrsAfterI1 = orderingRules.get(i1);

            if (nrsAfterI1 == null) {
                return 1;
            }

            return nrsAfterI1.contains(i2) ? -1 : 1;
        }
    }

}
