package pt.josealm3ida.aoc.disk_fragmenter;

import java.util.ArrayList;
import java.util.List;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<Character> chars = Utils.readCharacters("input/disk-fragmenter.txt").get(0);
        List<DriveSpace> drive = parseDrive(chars);
        System.out.println(part1(drive));
        System.out.println(part2(drive));
    }
    
    private static long part1(List<DriveSpace> drive) {
        return calculateChecksum(defragDrive(drive, false));
    }

    private static long part2(List<DriveSpace> drive) {
        return calculateChecksum(defragDrive(drive, true));
    }

    private static List<DriveSpace> parseDrive(final List<Character> chars) {
        List<DriveSpace> drive = new ArrayList<>();

        int currId = 0;

        for (int i = 0; i < chars.size(); i++) {
            boolean isFile = i % 2 == 0;

            int len = Integer.parseInt(chars.get(i).toString());
            if (len > 0) {
                drive.add(new DriveSpace(isFile ? String.valueOf(currId) : ".", len));

                if (isFile) {
                    currId++;
                }
            }
        }

        return drive;
    }

    private static List<DriveSpace> defragDrive(final List<DriveSpace> drive, boolean moveOnlyWholeFiles) {
        List<DriveSpace> defraggedDrive = new ArrayList<>(drive);
        
        for (int j = getNextFile(drive, defraggedDrive.size()); j != -1; j = getNextFile(defraggedDrive, j)) {
            DriveSpace src = defraggedDrive.get(j);

            int i = -1;
            while (true) {
                i = getNextEmptySpace(defraggedDrive, i);
                if (i == -1 || i >= j) {
                    break;
                }

                DriveSpace dest = defraggedDrive.get(i);

                int sizeDifference = dest.size() - src.size();
                if (sizeDifference >= 0) {
                    defraggedDrive.set(i, src);
                    defraggedDrive.set(j, new DriveSpace(".", src.size()));

                    if (sizeDifference > 0) {
                        defraggedDrive.add(i + 1, new DriveSpace(".", sizeDifference)); // Unused space left in destination
                        j++; // All indexes move one up due to adding
                    }

                    break;
                }

                if (!moveOnlyWholeFiles) {
                    sizeDifference = -sizeDifference;

                    defraggedDrive.set(i, new DriveSpace(src.id(), dest.size())); // Occupy the complete destination
                    defraggedDrive.set(j, new DriveSpace(src.id(), sizeDifference)); // Some data gets left behind

                    defraggedDrive.add(j + 1, new DriveSpace(".", dest.size())); // Created some space due to file moved
                    j++; // All indexes move one up due to adding
                    break;
                }
            }
        }

        return defraggedDrive;
    }

    private static int getNextEmptySpace(final List<DriveSpace> drive, int startingIdx) {
        int i = startingIdx + 1;
        if (i >= drive.size()) {
            return -1;
        }

        while (!drive.get(i).id().equals(".")) { 
            i++;

            if (i >= drive.size()) {
                return -1;
            }
        }

        return i;
    }

    private static int getNextFile(final List<DriveSpace> drive, int startingIdx) {
        int j = startingIdx - 1;
        if (j < 0) {
            return -1;
        }

        while (drive.get(j).id().equals(".")) { 
            j--;

            if (j < 0) {
                return -1;
            }
        }
        return j;
    }

    private static long calculateChecksum(final List<DriveSpace> defraggedDrive) {
        long checksum = 0;
        
        int currIdx = 0;
        for (int i = 0; i < defraggedDrive.size(); i++) {
            DriveSpace ds = defraggedDrive.get(i);
            
            if (ds.id().equals(".")) {
                currIdx += ds.size();
                continue;
            }

            int id = Integer.parseInt(ds.id());

            for (int j = 0; j < ds.size(); j++) {
                checksum += currIdx * id;
                currIdx++;
            }
        }

        return checksum;
    }

    private static void prettyPrint(final List<DriveSpace> drive) {
        drive.forEach(ds -> {
            for (int i = 0; i < ds.size(); i++) {
                System.out.print(ds.id());
            }
        });

        System.out.println();
    }

}
