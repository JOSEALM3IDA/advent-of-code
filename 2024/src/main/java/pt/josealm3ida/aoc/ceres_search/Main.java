package pt.josealm3ida.aoc.ceres_search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    private static final char[] STARTING_CHARS_TO_FIND_PT1 = "XMAS".toCharArray();
    private static final char[] STARTING_CHARS_TO_FIND_PT2 = "MAS".toCharArray();

    public static void main(String[] args) {
        List<List<Character>> charList = convertInput(Utils.readLines("input/ceres-search.txt"));
        System.out.println(part1(charList));
        System.out.println(part2(charList));
    }

    public static List<List<Character>> convertInput(List<String> lines) {
        List<List<Character>> charList = new ArrayList<>();

        for (String l : lines) {
            List<Character> lineCharList = new ArrayList<>();
            for (char c : l.toCharArray()) {
                lineCharList.add(c);
            }
            charList.add(lineCharList);
        }

        return charList;
    }

    private static int part1(List<List<Character>> charList) {
        int wordCount = 0;
        for (int i = 0; i < charList.size(); i++) {
            List<Character> lineCharList = charList.get(i);

            for (int j = 0; j < lineCharList.size(); j++) {
                List<DirectionalCoord> allMatches = findAllMatchesFromChar(charList, STARTING_CHARS_TO_FIND_PT1, i, j); 
                wordCount += allMatches.size(); 
            }
        }

        return wordCount;
    }

    private static int part2(List<List<Character>> charList) {
        List<DirectionalCoord> allMatches = new ArrayList<>();
        for (int i = 0; i < charList.size(); i++) {
            List<Character> lineCharList = charList.get(i);

            for (int j = 0; j < lineCharList.size(); j++) {
                allMatches.addAll(findAllMatchesFromChar(charList, STARTING_CHARS_TO_FIND_PT2, i, j)); 
            }
        }

        return findXWords(charList, allMatches, STARTING_CHARS_TO_FIND_PT2.length / 2);
    }

    private static int findXWords(List<List<Character>> charList, List<DirectionalCoord> matches, int midPointIdx) {
        List<Coord> middleCoords = matches.stream()
            .filter(m -> m.direction().isDiagonal())
            .map(m -> {
                Direction d = m.direction();
                return new Coord(d.calcNextRowNTimes(m.row(), midPointIdx), d.calcNextColNTimes(m.col(), midPointIdx));
            })
            .toList();

        int repeatedCount = 0;
        Set<Coord> uniqueMiddleCoords = new HashSet<>(middleCoords);
        for (Coord c : uniqueMiddleCoords) {
            if (Collections.frequency(middleCoords, c) == 2) {
                repeatedCount++;
            }
        }

        return repeatedCount;
    }

    private static List<DirectionalCoord> findAllMatchesFromChar(List<List<Character>> charList, char[] charsToFind, int i, int j) {
        List<DirectionalCoord> foundMatches = new ArrayList<>();
            
        for (Direction d : Direction.values()) {
            if (hasDirectionalMatchFromChar(charList, d, charsToFind, i, j)) {
                foundMatches.add(new DirectionalCoord(i, j, d));
            }
        }

        return foundMatches;
    }

    private static boolean hasDirectionalMatchFromChar(List<List<Character>> charList, Direction direction, char[] charsToFind, int i, int j) {
        if (charsToFind.length == 0) {
            return true;
        }

        char currCharToSearch = charsToFind[0];
        boolean outOfBounds = i < 0 || i >= charList.size() || j < 0 || j >= charList.get(i).size();
        if (outOfBounds || charList.get(i).get(j) != currCharToSearch) {
            return false; 
        }

        char[] remainingCharsToFind = Arrays.copyOfRange(charsToFind, 1, charsToFind.length);
        return hasDirectionalMatchFromChar(charList, direction, remainingCharsToFind, direction.calcNextRow(i), direction.calcNextCol(j));
    }

}
