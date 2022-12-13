package pt.josealm3ida.aoc2022.day13;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DistressSignal {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day13/input.txt";

    public static int getMaxDepth(String packet) {
        int maxDepth = 0;
        int currDepth = 0;
        for (char c : packet.toCharArray()) {
            if (c == '[') {
                currDepth++;
                maxDepth = Math.max(maxDepth, currDepth);
            } else if (c == ']') currDepth--;
        }

        return maxDepth;
    }

    public static List<String> getElementsOfDepth(String packet, int depth) {
        int maxDepth = 0;
        int currDepth = 0;

        List<String> elementList = new ArrayList<>();
        char[] chars = packet.toCharArray();
        int startOfSubstring = -1;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '[') {
                currDepth++;
                maxDepth = Math.max(maxDepth, currDepth);

                if (currDepth == depth) {
                    startOfSubstring = i;
                } else if (currDepth == depth + 1) {
                    elementList.add(packet.substring(startOfSubstring, i));
                    startOfSubstring = i;
                }
            } else if (c == ']') {
                if (currDepth == depth) {
                    elementList.add(packet.substring(startOfSubstring, i));
                    startOfSubstring = -1;
                }

                currDepth--;
            }
        }

        return elementList;
    }

    //public static boolean isSorted(List<Integer> leftValue, List<Integer> rightValue) {}

    public static int getSumIndicesOrderedPairs(String[] input) {
        /*for (int i = 0; i < input.length; i += 3) {
            int maxDepth = getMaxDepth(input[i]);
            System.out.println(maxDepth);
            maxDepth = getMaxDepth(input[i+1]);
            System.out.println(maxDepth);
            System.out.println();

            if (maxDepth > 3) {
                System.out.println(i);
                System.out.println(getElementsOfDepth(input[i], 2).size());
            }
        }*/

        for (String s : getElementsOfDepth(input[21], 3))
            System.out.println(s);

        return 0;
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        System.out.println("Sum of indices of ordered pairs: " + getSumIndicesOrderedPairs(input));
    }

}
