package pt.josealm3ida.aoc.claw_contraption;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<Machine> machines = parseMachines(Utils.readLines("input/claw-contraption.txt"));
        System.out.println(part1(machines));
        System.out.println(part2(machines));
    }

    private static List<Machine> parseMachines(List<String> lines) {
        List<Machine> machines = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\d+");

        for (int i = 0; i < lines.size(); i += 4) {
            String machineStr = lines.get(i) + lines.get(i + 1) + lines.get(i + 2);
            Matcher matcher = pattern.matcher(machineStr);
            List<Integer> ns = new ArrayList<>();
            while (matcher.find()) {
                ns.add(Integer.parseInt(matcher.group()));
            }

            machines.add(new Machine(new Coord(ns.get(0), ns.get(1)), new Coord(ns.get(2), ns.get(3)), new Coord(ns.get(4), ns.get(5))));
        }

        return machines;
    }

    private static long part1(List<Machine> machines) {
        final int maxPresses = 100;
        long totalTokens = 0;

        for (Machine m : machines) {
            totalTokens += m.solve(maxPresses);
        }

        return totalTokens;
    }

    private static long part2(List<Machine> machines) {
        final long prizeConversion = 10000000000000l; 
        long totalTokens = 0;

        for (Machine m : machines) {
            totalTokens += m.solve(prizeConversion);
        }

        return totalTokens;
    }

    private record Machine(Coord btnA, Coord btnB, Coord prize) {

        private long applyPress(long nA, long nB, long prizeX, long prizeY) {
            return prizeX == btnA.x() * nA + btnB.x() * nB && prizeY == btnA.y() * nA + btnB.y() * nB? nA * 3 + nB : 0; 
        }
        
        public long solve(int maxN) {
            return solve(maxN, 0);
        }

        public long solve(long prizeConversion) {
            return solve(0, prizeConversion);
        }

        private long solve(int maxN, long prizeConversion) {
            long pX = prize.x() + prizeConversion;
            long pY = prize.y() + prizeConversion;

            double nA = Math.round(((double) (pX*btnB.y() - pY*btnB.x()) / (double) (btnB.y()*btnA.x() - btnA.y()*btnB.x())));
            double nB = Math.round(((((double) pY)/btnB.y()) - nA * ((double) btnA.y()/btnB.y())));

            if (maxN != 0 && (nA > maxN || nB > maxN)) {
                return 0;
            }

            return applyPress((long) nA, (long) nB, pX, pY);
        }

    }

}
