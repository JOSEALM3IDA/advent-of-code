package pt.josealm3ida.aoc2022.day7;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;

public class NoSpaceLeftOnDevice {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day7/input.txt";

    public static final int TOTAL_DISK_SPACE = 70000000;
    public static final int UPDATE_REQUIRED_DISK_SPACE = 30000000;

    public static Directory parseTerminalOutput(String[] terminalOutput) {
        Directory rootDir = new Directory("/");
        Directory currDir = rootDir;

        for (String line : terminalOutput) {
            String[] lineSplit = line.split(" ");

            switch (line.charAt(0)) {
                case '$' -> {
                    if (lineSplit[1].equals("ls")) break;

                    if (lineSplit[2].equals("/")) {
                        currDir = rootDir;
                        break;
                    }

                    if (lineSplit[2].equals("..")) {
                        currDir = currDir.getParentDir();
                        break;
                    }

                    currDir = currDir.getSubdirectory(lineSplit[2]);
                }

                case 'd' -> {
                    Directory d = new Directory(lineSplit[1]);
                    currDir.addSubDirectory(d);
                }

                default -> currDir.addFile(new File(lineSplit[1], Integer.parseInt(lineSplit[0])));
            }

        }

        rootDir.calcDirSize();
        return rootDir;
    }

    public static int getSizeDirToDelete(Directory root, int requiredDiskSpace) {
        int diskSpaceToClear = requiredDiskSpace - (TOTAL_DISK_SPACE - root.getTotalSize());
        return root.getSmallestDirAbove(diskSpaceToClear).getTotalSize();
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        Directory root = parseTerminalOutput(input);

        System.out.println("Sum of dir sizes at or below 100000: " + root.getSumSizesBelow(100000));
        System.out.println("Total size of directory to delete: " + getSizeDirToDelete(root, UPDATE_REQUIRED_DISK_SPACE));
    }
}
