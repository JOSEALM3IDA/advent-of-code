package pt.josealm3ida.aoc.resonant_collinearity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<List<Character>> charList = Utils.readCharacters("input/resonant-collinearity.txt");
        Set<Antenna> allAntennas = parseInput(charList);
        Coord edge = new Coord(charList.size() - 1, charList.get(0).size() - 1);
        System.out.println(getNAntinodes(allAntennas, edge, false));
        System.out.println(getNAntinodes(allAntennas, edge, true));
    }

    private static Set<Antenna> parseInput(List<List<Character>> charList) {
        Set<Antenna> allAntennas = new HashSet<>();

        for (int i = 0; i < charList.size(); i++) {
            for (int j = 0; j < charList.get(i).size(); j++) {
                Character c = charList.get(i).get(j);
                if (c != '.') {
                    allAntennas.add(new Antenna(new Coord(i, j), c));
                }
            }
        }

        return allAntennas;
    }

    private static int getNAntinodes(Set<Antenna> allAntennas, Coord edge, boolean anyDistance) {
        Set<Coord> antinodes = new HashSet<>();

        for (Antenna a : allAntennas) {
            Set<Antenna> inLineAntennas = getInLineAntennas(allAntennas, a);
            for (Antenna b : inLineAntennas) {
                int diffi = b.coord().i() - a.coord().i();
                int diffj = b.coord().j() - a.coord().j();

                // Hack for part1 to still work with this code
                if (!anyDistance) {
                    diffi *= 2;
                    diffj *= 2;
                }

                Coord currPoint = a.coord();
                boolean outOfBounds;
                do {
                    Coord possibleAntinode = new Coord(currPoint.i() + diffi, currPoint.j() + diffj);

                    outOfBounds = possibleAntinode.i() < 0 || possibleAntinode.i() > edge.i();
                    outOfBounds = outOfBounds || possibleAntinode.j() < 0 || possibleAntinode.j() > edge.j();

                    if (!outOfBounds) {
                        antinodes.add(possibleAntinode);
                    }

                    currPoint = possibleAntinode;

                } while (anyDistance && !outOfBounds);
            }
        }

        return antinodes.size();
    }

    private static Set<Antenna> getInLineAntennas(Set<Antenna> antennaMap, Antenna antenna) {
        return antennaMap.stream().filter(a -> !a.equals(antenna) && a.frequency().equals(antenna.frequency())).collect(Collectors.toSet());
    }

}
