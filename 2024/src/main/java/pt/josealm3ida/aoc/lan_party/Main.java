package pt.josealm3ida.aoc.lan_party;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        Map<String, Computer> computerMap = parseLines(Utils.readLines("input/lan-party.txt")); 
        System.out.println(part1(computerMap));
        System.out.println(part2(computerMap));
    }

    private static Map<String, Computer> parseLines(List<String> lines) {
        List<List<String>> links = lines.stream().map(l -> {
            String[] split = l.split("-");
            return List.of(split[0], split[1]);
        }).toList();

        Map<String, Computer> computerMap = new HashMap<>();

        links.forEach(l -> {
            Computer c1 = computerMap.getOrDefault(l.get(0), new Computer(l.get(0), new ArrayList<>()));
            Computer c2 = computerMap.getOrDefault(l.get(1), new Computer(l.get(1), new ArrayList<>()));

            computerMap.putIfAbsent(l.get(0), c1);
            computerMap.putIfAbsent(l.get(1), c2);

            c1.connections().add(c2);
            c2.connections().add(c1);
        });

        return computerMap;
    }

	private static int part1(Map<String, Computer> computerMap) {
        Set<List<Computer>> groups = computerMap.values().stream()
            .flatMap(c -> c.asInterconnectedGroupsOf3().stream().filter(l -> {
                boolean keep = false;

                for (Computer lc : l) {
                    if (lc.startsWithT()) {
                        keep = true;
                        break;
                    }
                }

                return keep;
            })).filter(c -> !c.isEmpty())
            .collect(Collectors.toSet());

        return groups.size();
	}

	private static String part2(Map<String, Computer> computerMap) {
        Set<List<Computer>> groups = computerMap.values().stream()
            .flatMap(c -> c.asInterconnectedGroups().stream())
            .collect(Collectors.toSet());

        return groups.stream().max(Comparator.comparing(List::size)).get().stream().map(Computer::name).collect(Collectors.joining(","));
	}

    private record Computer(String name, List<Computer> connections) implements Comparable<Computer> {

        public Set<List<Computer>> asInterconnectedGroupsOf3() {
            Set<List<Computer>> interconnectedGroups = new HashSet<>();

            for (Computer c1 : this.connections) {
                for (Computer c2 : this.connections) {
                    if (c1.equals(c2) || !c1.isConnectedTo(c2)) {
                        continue;
                    }

                    List<Computer> interconnectedGroup = new ArrayList<>(List.of(this, c1, c2));
                    Collections.sort(interconnectedGroup);
                    interconnectedGroups.add(interconnectedGroup);
                }
            }

            return interconnectedGroups;
        }

        public Set<List<Computer>> asInterconnectedGroups() {
            Set<List<Computer>> interconnectedGroups = new HashSet<>();

            for (Computer c1 : this.connections) {
                List<Computer> interconnectedGroup = new ArrayList<>(List.of(this, c1));

                for (Computer c2 : this.connections) {
                    if (c1.equals(c2) || !c1.isConnectedTo(c2)) {
                        continue;
                    }

                    interconnectedGroup.add(c2);
                    interconnectedGroups.add(interconnectedGroup);
                }
            }

            // This hack (removing computer groups which don't all connect to each other) works for my input
            // No guarantees it would work for all inputs. This just worked for me and example so I keep it like this 
            interconnectedGroups.removeIf(g -> {
                for (Computer c1 : g) {
                    for (Computer c2 : g) {
                        if (c1.equals(c2)) {
                            continue;
                        }

                        if (!c1.isConnectedTo(c2)) {
                            return true;
                        }
                    }
                }

                return false;
            });

            interconnectedGroups.forEach(g -> Collections.sort(g));

            return new HashSet<>(interconnectedGroups);
        }

        public List<String> connectionsToString() {
            return this.connections.stream().map(c -> c.name()).toList();
        }

        public boolean isConnectedTo(Computer c) {
            return connectionsToString().contains(c.name());
        }

        public boolean startsWithT() {
            return this.name().startsWith("t");
        }

		@Override
		public int compareTo(Computer c) {
            return this.name().compareTo(c.name());
		}

        @Override
        public boolean equals(Object o) {
            return this.name().equals(((Computer) o).name());
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

		@Override
		public String toString() {
			return name;
		}

    }

}
