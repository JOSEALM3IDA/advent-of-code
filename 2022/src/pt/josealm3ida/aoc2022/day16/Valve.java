package pt.josealm3ida.aoc2022.day16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Valve implements Comparable<Valve> {

    private static final List<Valve> valveList = new ArrayList<>();
    private static final List<Valve> unstuckValveList = new ArrayList<>();

    public static Valve getValve(String name) {
        for (Valve v : valveList) {
            if (v.getName().equals(name)) return v;
        }

        Valve v = new Valve(name);
        valveList.add(v);
        return v;
    }

    public static List<Valve> getValveList() {
        return valveList;
    }

    public static List<Valve> getUnstuckValveList() {
        return unstuckValveList;
    }

    private final String name;
    private int flowRate;
    private List<Valve> connections;

    private Valve(String name) {
        this.name = name;
    }

    public static void sortValveList() {
        Collections.sort(valveList);
        for (Valve v : valveList) {
            Collections.sort(v.getConnections());
            if (!v.isStuck()) unstuckValveList.add(v);
        }
    }

    public void setFlowRate(int flowRate) {
        this.flowRate = flowRate;
    }

    public void setConnections(List<Valve> connections) {
        this.connections = connections;
    }

    public String getName() {
        return name;
    }

    public int getFlowRate() {
        return flowRate;
    }

    public List<Valve> getConnections() {
        return connections;
    }

    public List<Valve> getBestPathTo(String destinationValve) {
        return getBestPathTo(new ArrayList<>(), destinationValve);
    }

    private List<Valve> getBestPathTo(List<Valve> pathTo, String destinationValve) {
        if (this.name.equals(destinationValve)) return new ArrayList<>(List.of(this));

        pathTo.add(this);

        List<Valve> bestPath = null;
        for (Valve v : connections) {
            if (pathTo.contains(v)) continue;

            List<Valve> path = v.getBestPathTo(new ArrayList<>(pathTo), destinationValve);

            if (path == null) continue;

            if (bestPath == null || path.size() < bestPath.size()) bestPath = path;
        }

        if (bestPath == null) return null;

        bestPath.add(0, this);

        return bestPath;
    }

    public boolean isStuck() {
        return flowRate == 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Valve v) {
        return v.getFlowRate() - getFlowRate();
    }
}
