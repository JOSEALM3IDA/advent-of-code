package pt.josealm3ida.aoc.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<String> readLines(final String filename) {
        final List<String> lines = new ArrayList<>();

        BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();

			while (line != null) {
                lines.add(line);
				line = reader.readLine();
			}

			reader.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

        return lines;
    } 

    public static List<List<Character>> readCharacters(final String filename) {
		List<String> lines = readLines(filename);

		List<List<Character>> charLists = new ArrayList<>();

        lines.forEach(l -> {
			charLists.add(new ArrayList<>(l.chars().mapToObj(c -> (char) c).toList()));
        });

		return charLists;
    }
}
