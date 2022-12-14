package pt.josealm3ida.aoc2022.day14;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegolithReservoir {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day14/input.txt";

    public static final int SAND_SOURCE_X = 500;
    public static final int SAND_SOURCE_Y = 0;

    public static List<List<Character>> rockMap = new ArrayList<>();
    public static int[] sourceCoords = new int[]{0,0};

    public static void printRockMap() {
        for (List<Character> row : rockMap) {
            for (Character c : row) {
                System.out.print(c);
            }

            System.out.println();
        }
        System.out.println();
    }

    public static void addCol(boolean atStart) {
        if (atStart) sourceCoords[0]++;

        for (List<Character> row : rockMap) {
            if (atStart) row.add(0, '.');
            else row.add('.');
        }
    }

    public static void addNColumns(int nCols) {
        boolean atStart = nCols < 0;
        for (int i = 0; i < Math.abs(nCols); i++) addCol(atStart);
    }

    public static void addRow() {
        List<Character> newRow = new ArrayList<>();
        for (int i = 0; i < rockMap.get(0).size(); i++) newRow.add('.');

        rockMap.add(newRow);
    }

    public static void addNRows(int nRows) {
        for (int i = 0; i < nRows; i++) addRow();
    }

    public static List<int[]> structureStringToCoords(String[] points) {
        List<int[]> pathCoords = new ArrayList<>();
        for (String point : points) {
            String[] coordsStr = point.split(",");
            pathCoords.add(new int[]{Integer.parseInt(coordsStr[0]) - SAND_SOURCE_X, Integer.parseInt(coordsStr[1]) - SAND_SOURCE_Y});
        }
        return pathCoords;
    }

    public static void guaranteeExists(int[] coords) {
        int actualX = sourceCoords[0] + coords[0];

        boolean existsX = actualX >= 0 && actualX < rockMap.get(0).size();
        boolean existsY = coords[1] < rockMap.size();

        if (existsX && existsY) return;

        if (!existsX) addNColumns(coords[0] < 0 ? actualX: actualX + 1 - rockMap.get(0).size());
        if (!existsY) addNRows(coords[1] + 1 - rockMap.size());
    }

    public static void drawLine(int[] start, int[] end) {
        int actualStartX = sourceCoords[0] + start[0];
        int actualEndX = sourceCoords[0] + end[0];

        boolean isHorizontal = start[1] == end[1];

        int lineLen = isHorizontal ? Math.abs(actualStartX - actualEndX) : Math.abs(start[1] - end[1]);
        int smallest = isHorizontal ? Math.min(actualStartX, actualEndX) : Math.min(start[1], end[1]);
        for (int i = 0; i < lineLen + 1; i++) {
            if (isHorizontal) rockMap.get(start[1]).set(smallest + i, '#');
            else rockMap.get(smallest + i).set(actualStartX, '#');
        }
    }

    public static void setUpRockMap(String[] input) {
        List<Character> firstRow = new ArrayList<>();
        firstRow.add('+');
        rockMap.add(firstRow);

        for (String s : input) {
            List<int[]> structureCoords = structureStringToCoords(s.split(" -> "));

            int[] prevPointCoords = structureCoords.get(0);
            guaranteeExists(prevPointCoords);
            for (int i = 1; i < structureCoords.size(); i++) {
                int[] pointCoords = structureCoords.get(i);
                guaranteeExists(pointCoords);
                drawLine(prevPointCoords, pointCoords);
                prevPointCoords = pointCoords;
            }
        }

        addRow();
    }

    public static boolean addSandUnit(boolean hasFloor) {
        int[] currSandCoords = sourceCoords.clone();

        while (true) {
            if (currSandCoords[1] == rockMap.size() - 1) {
                if (!hasFloor) return true; // Already at map bottom edge, falls into abyss
                rockMap.get(currSandCoords[1]).set(currSandCoords[0], 'o'); // Settles
                return false;
            }

            List<Character> rowAfter = rockMap.get(currSandCoords[1] + 1);
            if (rowAfter.get(currSandCoords[0]) == '.') { // Doesn't hit anything, keeps going down
                currSandCoords[1]++;
                continue;
            }

            if (currSandCoords[0] == 0) {
                if (!hasFloor) return true; // Already at map left edge, falls into abyss
                addCol(true);
                continue;
            }

            if (rowAfter.get(currSandCoords[0] - 1) == '.') { // Left is not occupied, goes left
                currSandCoords[0]--;
                continue;
            }

            if (currSandCoords[0] + 1 == rowAfter.size()) {
                if (!hasFloor) return true; // Already at map right edge, falls into abyss
                addCol(false);
                currSandCoords[0]++;
                continue;
            }

            if (rowAfter.get(currSandCoords[0] + 1) == '.') { // Right is not occupied, goes right
                currSandCoords[0]++;
                continue;
            }

            // Both occupied, settles
            rockMap.get(currSandCoords[1]).set(currSandCoords[0], 'o');
            return false;
        }
    }

    public static int getSandUnitsAtRestNoFloor(String[] input) {
        boolean fellIntoAbyss = false;

        int sandCount = 0;
        while (!fellIntoAbyss) {
            fellIntoAbyss = addSandUnit(false);
            sandCount++;
        }

        printRockMap();
        return sandCount - 1;
    }

    public static int getSandUnitsAtRestWithFloor(String[] input) {
        int sandCount = 0;
        while (rockMap.get(sourceCoords[1]).get(sourceCoords[0]) == '+') {
            addSandUnit(true);
            sandCount++;
        }

        printRockMap();
        return sandCount;
    }

    private static void clearMap() {
        rockMap.get(sourceCoords[1]).set(sourceCoords[0], '+');

        for (List<Character> row : rockMap) {
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i) == 'o') row.set(i, '.');
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        setUpRockMap(input);
        int sandUnitsAtRestNoFloor = getSandUnitsAtRestNoFloor(input);
        clearMap();
        int sandUnitsAtRestWithFloor = getSandUnitsAtRestWithFloor(input);

        System.out.println("Number of sand units at rest with no floor: " + sandUnitsAtRestNoFloor);
        System.out.println("Number of sand units at rest with floor: " + sandUnitsAtRestWithFloor);
    }

}
