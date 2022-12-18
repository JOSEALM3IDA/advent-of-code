package pt.josealm3ida.aoc2022.day15;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

public class BeaconExclusionZone {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day15/input.txt";

    public static final int MIN_X_GRID = -1000000; // 1 M
    public static final int MAX_X_GRID = 5000000; // 5 M

    public static final int MIN_XY_DISTRESS = 0;
    public static final int MAX_XY_DISTRESS = 4000000;

    public static final int TUNING_MULTIPLIER = 4000000;

    public static final List<Sensor> sensorList = new ArrayList<>();

    public static void parseInput(String[] input) {
        sensorList.clear();

        Pattern p = Pattern.compile("-?\\d+");
        for (String s : input) {
            Matcher m = p.matcher(s);
            int[] numbers = new int[4];
            for (int i = 0; i < 4 && m.find(); i++) {
                numbers[i] = Integer.parseInt(m.group());
            }

            sensorList.add(new Sensor(new int[]{ numbers[0], numbers[1] }, new int[]{ numbers[2], numbers[3] }));
        }

        Collections.sort(sensorList);
    }

    public static int countNoBeaconPositions(int y, int startX, int endX, boolean countExistingBeacons) {
        int count = 0;
        for (int x = startX; x < endX; x++) {
            for (Sensor s : sensorList) {
                if (s.isPointDetected(new int[]{x,y}, countExistingBeacons)) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

    public static long getTuningFrequencyDistress() {
        for (Sensor s : sensorList) {
            for (String str : s.getOutsidePerimeterPoints()) {
                String[] coordsStr = str.split(",");
                int x = Integer.parseInt(coordsStr[0]);
                if (x < MIN_XY_DISTRESS || x > MAX_XY_DISTRESS) continue;

                int y = Integer.parseInt(coordsStr[1]);
                if (y < MIN_XY_DISTRESS || y > MAX_XY_DISTRESS) continue;

                boolean isSeen = false;
                for (Sensor s2 : sensorList) {
                    if (s2.isPointDetected(new int[]{x,y}, false)) {
                        isSeen = true;
                        break;
                    }
                }

                if (!isSeen) return (long) x * TUNING_MULTIPLIER + y;
            }
        }

        return -1;
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        parseInput(input);

        System.out.println("Number of positions that cannot contain a beacon (y=2000000): " + countNoBeaconPositions(2000000, MIN_X_GRID, MAX_X_GRID, true));
        System.out.println("Distress beacon's tuning frequency: " + getTuningFrequencyDistress());
    }

}
