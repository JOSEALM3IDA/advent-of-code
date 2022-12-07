package pt.josealm3ida.aoc2022.day7;

public class File {

    private final String fileName;
    private final int fileSize;
    private Directory parentDir;

    public File(String fileName, int fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public Directory getParentDir() {
        return parentDir;
    }

    public int getDepth() {
        return parentDir == null ? 0 : parentDir.getDepth() + 1;
    }

    public void setParentDir(Directory parentDir) {
        this.parentDir = parentDir;
    }

    @Override
    public String toString() {
        return "\t".repeat(getDepth()) + "- " + fileName + " (file, size=" + fileSize + ")";
    }
}
