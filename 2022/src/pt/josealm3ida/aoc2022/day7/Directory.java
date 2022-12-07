package pt.josealm3ida.aoc2022.day7;

import java.util.ArrayList;
import java.util.List;

public class Directory {

    private final String directoryName;

    private Directory parentDir = null;

    private final List<Directory> subDirectories = new ArrayList<>();

    private final List<File> fileList = new ArrayList<>();

    private int totalSize = 0;

    public Directory(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getName() {
        return directoryName;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public Directory getParentDir() {
        return parentDir;
    }

    public Directory getSubdirectory(String subDirName) {
        for (Directory d : subDirectories) {
            if (d.getName().equals(subDirName)) return d;
        }

        return null;
    }

    public int getDepth() {
        return parentDir == null ? 0 : parentDir.getDepth() + 1;
    }

    public void setParentDir(Directory parentDir) {
        this.parentDir = parentDir;
    }

    public void addSubDirectory(Directory d) {
        d.setParentDir(this);
        subDirectories.add(d);
    }

    public void addFile(File file) {
        file.setParentDir(this);
        fileList.add(file);
    }


    public void calcDirSize() {
        totalSize = 0;

        for (File f : fileList) totalSize += f.getFileSize();

        for (Directory d : subDirectories) {
            d.calcDirSize();
            totalSize += d.getTotalSize();
        }
    }

    public int getSumSizesBelow(int max) {
        int rtnSize = totalSize <= max ? totalSize : 0;

        for (Directory d : subDirectories) rtnSize += d.getSumSizesBelow(max);

        return rtnSize;
    }

    public Directory getSmallestDirAbove(int requiredDiskSpace) {
        Directory possibleDir = null;

        for (Directory d : subDirectories) {
            Directory smallest = d.getSmallestDirAbove(requiredDiskSpace);
            if (smallest == null) continue;
            if (possibleDir == null || smallest.getTotalSize() < possibleDir.getTotalSize()) possibleDir = smallest;
        }

        return possibleDir == null && totalSize >= requiredDiskSpace ? this : possibleDir;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String indent = "\t".repeat(getDepth());
        sb.append(indent).append("- ").append(directoryName).append(" (dir)");

        for (File f : fileList) {
            sb.append("\n").append(f.toString());
        }

        for (Directory d : subDirectories) {
            sb.append("\n").append(d.toString());
        }

        return sb.toString();
    }
}
