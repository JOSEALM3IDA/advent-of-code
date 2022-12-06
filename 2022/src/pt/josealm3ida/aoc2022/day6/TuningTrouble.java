package pt.josealm3ida.aoc2022.day6;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TuningTrouble {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day6/input.txt";

    public static final int START_PACKET_NUM_DIFF_CHARS = 4;
    public static final int START_MESSAGE_NUM_DIFF_CHARS = 14;

    public static int findStartPacketIndex(String datastream, int nDiffChars) {
        List<Character> charList = new ArrayList<>();

        for (int i = 0; i < datastream.length(); i++) {
            if (charList.size() == nDiffChars) return i;

            char currChar = datastream.charAt(i);
            int charIdx = charList.indexOf(currChar);

            if (charIdx != -1) charList = charList.subList(charIdx + 1, charList.size());

            charList.add(currChar);
        }

        return -1;
    }


    public static void main(String[] args) throws IOException {
        String input = FileReaderUtils.linesToArray(FILE_PATH)[0];

        System.out.println("First start-of-packet marker: " + findStartPacketIndex(input, START_PACKET_NUM_DIFF_CHARS));
        System.out.println("First start-of-message marker: " + findStartPacketIndex(input, START_MESSAGE_NUM_DIFF_CHARS));
    }
}
