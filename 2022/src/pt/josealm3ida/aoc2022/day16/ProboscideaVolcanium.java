package pt.josealm3ida.aoc2022.day16;

import pt.josealm3ida.aoc2022.common.FileReaderUtils;

import java.io.IOException;
import java.util.*;

public class ProboscideaVolcanium {

    public static final String FILE_PATH = "src/pt/josealm3ida/aoc2022/day16/input.txt";

    public static final int TOTAL_TIME = 30;

    public static void parseInput(String[] input) {
        Valve.getValveList().clear();

        for (String s : input) {
            String[] split = s.split(" ");

            String name = split[1];
            int flowRate = Integer.parseInt(split[4].split("=")[1].split(";")[0]);
            String[] valves = String.join("", Arrays.copyOfRange(split, 9, split.length)).split(",");

            Valve v = Valve.getValve(name);
            v.setFlowRate(flowRate);

            List<Valve> connections = new ArrayList<>();
            for (String valveStr : valves) connections.add(Valve.getValve(valveStr));
            v.setConnections(connections);
        }

        Valve.sortValveList();
    }

    public static int getMaxPressureRelease(Valve curr, int opportunityValueCurr, int currTick, Set<Valve> visited) {
        int maxPressure = 0;
        visited.add(curr);
        for (Valve v : Valve.getUnstuckValveList()) {
            if (v.isStuck() || visited.contains(v)) continue;

            int distance = curr.getBestPathTo(v.getName()).size();

            if (distance >= TOTAL_TIME - currTick) continue;

            int opportunityValue = (TOTAL_TIME - currTick - distance) * v.getFlowRate();

            int pressure = getMaxPressureRelease(v, opportunityValue, currTick + distance, new HashSet<>(visited));
            if (curr.getName().equals("AA")) System.out.println(pressure);
            if (pressure > maxPressure) maxPressure = pressure;
        }

        return opportunityValueCurr + maxPressure;
    }

    public static int getMaxPressureRelease() {
        return getMaxPressureRelease(Valve.getValve("AA"), 0, 0, new HashSet<>());
    }

    public static void main(String[] args) throws IOException {
        String[] input = FileReaderUtils.linesToArray(FILE_PATH);

        parseInput(input);

        System.out.println("Max pressure that can be released in 30 mins: " + getMaxPressureRelease());
    }

}
