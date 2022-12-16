package pt.josealm3ida.aoc2022.day15;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
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

        Pattern p = Pattern.compile("\\d+");
        for (String s : input) {
            Matcher m = p.matcher(s);
            int[] numbers = new int[4];
            for (int i = 0; i < 4 && m.find(); i++) {
                numbers[i] = Integer.parseInt(m.group());
            }

            sensorList.add(new Sensor(new int[]{ numbers[0], numbers[1] }, new int[]{ numbers[2], numbers[3] }));
        }
    }

    public static int countNoBeaconPositions(int y, int startX, int endX, boolean countExistingBeacons) {
        int count = 0;
        for (int i = startX; i < endX; i++) {
            for (Sensor s : sensorList) {
                int[] coordsBeacon = s.getCoordsBeacon();
                if (s.isPointDetected(new int[]{i,y}) && (!countExistingBeacons || coordsBeacon[0] != i || coordsBeacon[1] != y)) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

    public static long getTuningFrequencyDistress() {

        int y;
        for (y = MIN_XY_DISTRESS; y < MAX_XY_DISTRESS; y++) {
            if (countNoBeaconPositions(y, MIN_XY_DISTRESS, MAX_XY_DISTRESS, false) == MAX_XY_DISTRESS) continue;

            int x;
            boolean foundX = false;
            for (x = MIN_XY_DISTRESS; x < MAX_XY_DISTRESS; x++) {
                boolean possible = true;
                for (Sensor s : sensorList) {
                    if (s.isPointDetected(new int[]{x, y})) {
                        possible = false;
                        break;
                    }
                }

                if (possible) {
                    foundX = true;
                    break;
                }
            }

            if (foundX) {
                System.out.println(y);
                return (long) x * TUNING_MULTIPLIER + y;
            }

        }

        return -1;
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        parseInput(input);

        //System.out.println(countNoBeaconPositions(0, MIN_XY_DISTRESS, MAX_XY_DISTRESS, false) != MAX_XY_DISTRESS);
        //System.out.println("Number of positions that cannot contain a beacon: " + countNoBeaconPositions(10, MIN_X_GRID, MAX_X_GRID,
        // true));
        System.out.println("Distress beacon's tuning frequency: " + getTuningFrequencyDistress());
    }

}
