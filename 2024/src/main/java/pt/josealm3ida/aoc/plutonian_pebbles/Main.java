package pt.josealm3ida.aoc.plutonian_pebbles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    private static int NR_BLINKS_PT1 = 25;
    private static int NR_BLINKS_PT2 = 75;

    public static void main(String[] args) {
        List<Long> stones = Arrays.stream(Utils.readLines("input/plutonian-pebbles.txt").get(0).split(" ")).map(Long::parseLong).toList();
        Map<Long, Long> stoneMap = parseStones(stones);

        System.out.println(blinkMultiple(stoneMap, NR_BLINKS_PT1));
        System.out.println(blinkMultiple(stoneMap, NR_BLINKS_PT2));
    }

    private static Map<Long, Long> parseStones(List<Long> stones) {
        Map<Long, Long> stoneMap = new HashMap<>();

        stones.forEach(s -> {
            addToMap(stoneMap, s, 1l);
        });

        return stoneMap;
    }

    private static Long blinkMultiple(final Map<Long, Long> stoneMap, int nTimes) {
        Map<Long, Long> changedStoneMap = new HashMap<>(stoneMap);

        for (int i = 0; i < nTimes; i++) {
            changedStoneMap = blink(changedStoneMap);
        }

        return changedStoneMap.values().stream().mapToLong(Long::valueOf).sum();
    }

    private static Map<Long, Long> blink(Map<Long, Long> stoneMap) {
        Map<Long, Long> changedStoneMap = new HashMap<>();

        stoneMap.entrySet().forEach(e -> {
            Long s = e.getKey();
            Long n = e.getValue();

            if (s == 0) {
                addToMap(changedStoneMap, 1l, n);
                return;
            }

            String sStr = String.valueOf(s);
            if (sStr.length() % 2 == 0) {
                int midIdx = sStr.length() / 2;
                addToMap(changedStoneMap, sStr.substring(0, midIdx), n);
                addToMap(changedStoneMap, sStr.substring(midIdx), n);
                return;
            }

            addToMap(changedStoneMap, s * 2024, n);
        });

        return changedStoneMap;
    }

    private static void addToMap(Map<Long, Long> stoneMap, String s, Long n) {
        addToMap(stoneMap, Long.parseLong(s), n);
    }

    private static void addToMap(Map<Long, Long> stoneMap, Long s, Long n) {
        Long count = stoneMap.get(s);

        if (count == null) {
            count = 0l;
        }

        count += n;

        stoneMap.put(s, count);
    }

}
