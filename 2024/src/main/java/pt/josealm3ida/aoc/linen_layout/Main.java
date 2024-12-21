package pt.josealm3ida.aoc.linen_layout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<String> lines = Utils.readLines("input/linen-layout.txt");
        List<String> availableTowels = Arrays.stream(lines.get(0).split(", ")).toList();
        List<String> requestedDesigns = lines.subList(2, lines.size());

        System.out.println(part1(availableTowels, requestedDesigns));
        System.out.println(part2(availableTowels, requestedDesigns));
    }

	private static int part1(List<String> availableTowels, List<String> requestedDesigns) {
        int possibleDesigns = 0;

        Map<String, Long> knownPossible = new HashMap<>();

        Set<String> knownNotPossible = new HashSet<>();

        for (String design : requestedDesigns) {
            long ctn = countDesigns(availableTowels, knownPossible, knownNotPossible, design);
            if (ctn != 0) {
                possibleDesigns++;
            } 
        }

        return possibleDesigns;
	}

	private static long part2(List<String> availableTowels, List<String> requestedDesigns) {
        long possibleDesigns = 0;

        Map<String, Long> knownPossible = new HashMap<>();

        Set<String> knownNotPossible = new HashSet<>();

        for (String design : requestedDesigns) {
            possibleDesigns += countDesigns(availableTowels, knownPossible, knownNotPossible, design);
        }

        return possibleDesigns;
    }

    // Recursive bruteforce + memoization = dynamic programming
    private static long countDesigns(List<String> availableTowels, Map<String, Long> knownPossible, Set<String> knownNotPossible, String design) {
        if (design.isEmpty()) {
            return 1;
        }

        long cnt = 0;
        for (String t : availableTowels) {
            if (t.length() > design.length()) {
                continue;
            }

            if (!design.startsWith(t)) {
                continue;
            }

            String subDesign = design.substring(t.length());
            long subCount = 0;
            if (knownPossible.containsKey(subDesign)) {
                subCount = knownPossible.get(subDesign);
            } else if (knownNotPossible.contains(subDesign)) {
                continue;
            } else {
                subCount = countDesigns(availableTowels, knownPossible, knownNotPossible, design.substring(t.length()));
            }

            if (subCount == 0) {
                continue;
            }

            cnt += subCount;
        }

        if (cnt > 0) {
            knownPossible.put(design, cnt);
        } else {
            knownNotPossible.add(design);
        }

        return cnt;
    }

}
