package pt.josealm3ida.aoc2022.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FileReaderUtils {
    public static String[] linesToArray(String fileName) throws IOException {
        Reader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        List<String> lines = new ArrayList<>();
        String line;

        while ((line = bufferedReader.readLine()) != null) lines.add(line);

        bufferedReader.close();

        return lines.toArray(new String[0]);
    }

    public static char[][] linesToCharMatrix(String fileName) throws IOException {
        Reader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        List<char[]> lines = new ArrayList<>();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line.toCharArray());
        }

        bufferedReader.close();

        return lines.toArray(new char[0][0]);
    }
}
