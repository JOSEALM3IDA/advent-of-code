package pt.josealm3ida.aoc2022.day8;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TreetopTreeHouse {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day8/input.txt";

    public static int[][] getTreeGrid(String[] input) {
        int nRows = input.length;
        int nCols = input[0].length();

        int[][] treeGrid = new int[nRows][];
        for (int i = 0; i < nRows; i++) {
            treeGrid[i] = new int[nCols];

            for (int j = 0; j < nCols; j++) {
                treeGrid[i][j] = input[i].charAt(j) - 48;
            }
        }

        return treeGrid;
    }

    public static int getNVisibleTrees(int[][] treeGrid) {
        int nRows = treeGrid.length;
        int nCols = treeGrid[0].length;
        Set<String> visibleTrees = new HashSet<>();

        for (int i = 0; i < nRows; i++) {
            // Left -> Right
            int highest = -1;
            for (int j = 0; j < nCols; j++) {
                if (treeGrid[i][j] <= highest) continue;

                visibleTrees.add(i + "," + j);
                highest = treeGrid[i][j];
            }

            // Right -> Left
            highest = -1;
            for (int j = nCols - 1; j >= 0; j--) {
                if (treeGrid[i][j] <= highest) continue;

                visibleTrees.add(i + "," + j);
                highest = treeGrid[i][j];
            }
        }

        for (int j = 0; j < nCols; j++) {
            // Up -> Down
            int lastHeight = -1;
            for (int i = 0; i < nRows; i++) {
                if (treeGrid[i][j] <= lastHeight) continue;

                visibleTrees.add(i + "," + j);
                lastHeight = treeGrid[i][j];
            }

            // Down -> Up
            lastHeight = -1;
            for (int i = nRows - 1; i >= 0; i--) {
                if (treeGrid[i][j] <= lastHeight) continue;

                visibleTrees.add(i + "," + j);
                lastHeight = treeGrid[i][j];
            }
        }

        return visibleTrees.size();
    }

    public static int getScenicScore(int[][] treeGrid, int x, int y) {
        int nRows = treeGrid.length;
        int nCols = treeGrid[0].length;

        int scenicScore = 1;
        int maxHeight = treeGrid[x][y];

        // Left -> Right
        int currScenicScore = 0;
        for (int j = y + 1; j < nCols; j++) {
            currScenicScore++;
            if (treeGrid[x][j] >= maxHeight) break;
        }

        scenicScore *= currScenicScore;

        // Right -> Left
        currScenicScore = 0;
        for (int j = y - 1; j >= 0; j--) {
            currScenicScore++;
            if (treeGrid[x][j] >= maxHeight) break;
        }

        scenicScore *= currScenicScore;

        // Up -> Down
        currScenicScore = 0;
        for (int i = x + 1; i < nRows; i++) {
            currScenicScore++;
            if (treeGrid[i][y] >= maxHeight) break;
        }

        scenicScore *= currScenicScore;

        // Down -> Up
        currScenicScore = 0;
        for (int i = x - 1; i >= 0; i--) {
            currScenicScore++;
            if (treeGrid[i][y] >= maxHeight) break;
        }

        scenicScore *= currScenicScore;

        return scenicScore;
    }

    public static int getHighestScenicScore(int[][] treeGrid) {
        int nRows = treeGrid.length;
        int nCols = treeGrid[0].length;
        int highestScenicScore = 0;

        for (int i = 1; i < nRows - 1; i++) {
            for (int j = 1; j < nCols - 1; j++) {
                highestScenicScore = Math.max(highestScenicScore, getScenicScore(treeGrid, i, j));
            }
        }

        return highestScenicScore;
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        int[][] treeGrid = getTreeGrid(input);

        System.out.println("Number of visible trees from outside: " + getNVisibleTrees(treeGrid));
        System.out.println("Highest scenic score: " + getHighestScenicScore(treeGrid));
    }


}
