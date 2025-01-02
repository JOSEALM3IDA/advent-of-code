package pt.josealm3ida.aoc.crossed_wires;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<String> lines = Utils.readLines("input/crossed-wires.txt");
        Map<String, Boolean> initialValues = parseInitialValues(lines);
        List<Instruction> instructions = parseInstructions(lines);

        System.out.println(part1(initialValues, instructions));
        System.out.println(part2(initialValues, instructions, false));
    }

    private static Map<String, Boolean> parseInitialValues(List<String> lines) {
        Map<String, Boolean> initialValues = new HashMap<>();

        for (String line : lines.subList(0, lines.indexOf(""))) {
            String[] split = line.split(": ");
            initialValues.put(split[0], Integer.valueOf(split[1]) == 0 ? false : true);
        }

        return initialValues;
    }

    private static List<Instruction> parseInstructions(List<String> lines) {
        List<Instruction> instructions = new ArrayList<>();

        for (String line : lines.subList(lines.indexOf("") + 1, lines.size())) {
            String[] split1 = line.split(" -> ");
            String[] split2 = split1[0].split(" ");
            instructions.add(new Instruction(split2[0], split2[2], split1[1], Gate.fromString(split2[1])));
        }

        return instructions;
    }

    private static long part1(Map<String, Boolean> initialValues, List<Instruction> instructions) {
        return combineBinary(applyInstructions(initialValues, instructions), 'z');
	}

    private static String part2(Map<String, Boolean> initialValues, List<Instruction> instructions, boolean bruteForce) {
        return bruteForce ? bruteForce(initialValues, instructions) : generateDot(initialValues, instructions);
    }

    // Generates a DOT file so that the problem can be solved visually (with knowledge of how a ripple-carry adder works)
    private static String generateDot(Map<String, Boolean> initialValues, List<Instruction> instructions) {
        long input1 = combineBinary(initialValues, 'x');
        long input2 = combineBinary(initialValues, 'x');
        long output = combineBinary(applyInstructions(initialValues, instructions), 'z');

        if (input1 + input2 == output) {
            return "Already solved!";
        }

        Map<String, Instruction> instructionMap = new LinkedHashMap<>();
        List<Instruction> sortedInstruction = new ArrayList<>(instructions);
        sortedInstruction.sort(new Comparator<Instruction>() {
            public int compare(Instruction o1, Instruction o2) {
                return o2.out().compareTo(o1.out());
            }
        });

        sortedInstruction.forEach(i -> instructionMap.put(i.out(), i));

        new File("output").mkdirs();
        try (PrintWriter writer = new PrintWriter("output/crossed-wires.dot", "UTF-8")) {
			writer.println("digraph crossedWires {");

            for (Map.Entry<String, Instruction> instructionEntry : instructionMap.entrySet()) {
                Instruction ins = instructionEntry.getValue();
                writer.println("\t{%s %s} -> %s".formatted(ins.in1(), ins.in2(), ins.out()));
            }

            writer.print("\t{rank = same; edge[style = invis];");
            boolean notFirst = false;
            for (String key : instructionMap.keySet().stream().filter(k -> k.startsWith("z")).toList()) {
                if (notFirst) {
                    writer.print(" -> ");
                }

                notFirst = true;
                writer.print("%s".formatted(key));                
            }
            writer.println("; rankdir=LR}");

			writer.println("}");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
            return e.toString();
		}

        return "Dot file generated. Execute with dot -Tsvg crossed-wires.dot > crossed-wires.svg"+
            "\nHint: Look for XOR nodes which don't come from from a xy and that don't output to a z. Also, if the XOR inputs directly from xy and outputs to z, that's also wrong.";
    }

    private static String bruteForce(Map<String, Boolean> initialValues, List<Instruction> instructions) {
        long input1 = combineBinary(initialValues, 'x');
        long input2 = combineBinary(initialValues, 'y');

        for (int n1 = 0; n1 < instructions.size(); n1++) {
            for (int n2 = 0; n2 < instructions.size(); n2++) {
                if (n2 == n1) {
                    continue;
                }

                // Swap outputs, n1 with n2 and n3 with n4
                List<Instruction> newInstructions = new ArrayList<>(instructions);
                List<Instruction> swappedInstructions = List.of(
                    instructions.get(n1),
                    instructions.get(n2)
                );

                newInstructions.set(n1, Instruction.getSwappedInstruction(swappedInstructions.get(0), swappedInstructions.get(1).out()));
                newInstructions.set(n2, Instruction.getSwappedInstruction(swappedInstructions.get(1), swappedInstructions.get(0).out()));
                long output = combineBinary(applyInstructions(initialValues, newInstructions), 'z');

                if (input1 + input2 == output) {
                    System.out.println("%s <-> %s".formatted(swappedInstructions.get(0).out(), swappedInstructions.get(1).out()));
                    System.out.println("Possible solution: " + Set.of(
                        "gdd", "z05", "cwt", "z09", "pqt", "z37", // Hard coded for my input (found by analyzing the graph visually)
                        swappedInstructions.get(0).out(), // correct: jmv or css
                        swappedInstructions.get(1).out()  // correct: jmv or css
                    ).stream().sorted().collect(Collectors.joining(",")));
                }
            }
        }

        return "Change the input values (x, y) for different possibilities.";
    }


    private static Map<String, Boolean> applyInstructions(Map<String, Boolean> initialValues, List<Instruction> initialInstructions) {
        Map<String, Boolean> currValues = new HashMap<>(initialValues);
        List<Instruction> instructions = new ArrayList<>(initialInstructions);

        int nOutputs = (int) instructions.stream().filter(i -> i.out().startsWith("z")).count();
        Set<String> doneOutputs = new HashSet<>();

        while (doneOutputs.size() < nOutputs) {
            int sizeBefore = instructions.size();
            for (int i = 0; i < instructions.size(); i++) {
                if (doneOutputs.size() == nOutputs) {
                    break;
                }

                Instruction instruction = instructions.get(i);

                Boolean v1 = currValues.get(instruction.in1()); 
                Boolean v2 = currValues.get(instruction.in2()); 

                if (v1 == null || v2 == null) {
                    continue;
                }

                instructions.remove(i);

                currValues.put(instruction.out(), instruction.gate().apply(v1, v2));
                if (instruction.out().startsWith("z")) {
                    doneOutputs.add(instruction.out());
                }
            }

            if (instructions.size() == sizeBefore) {
                return null;
            }
        }
        
        return currValues;
    }

    private static long combineBinary(Map<String, Boolean> values, char regStartChar) {
        if (values == null) {
            return -1;
        }

        List<AbstractMap.SimpleEntry<Integer, Boolean>> relevantValues = values.entrySet().stream()
            .filter(ks -> ks.getKey().startsWith(String.valueOf(regStartChar)))
            .map(ks -> new AbstractMap.SimpleEntry<Integer, Boolean>(Integer.parseInt(ks.getKey().substring(1, ks.getKey().length())), ks.getValue()))
            .collect(Collectors.toCollection(ArrayList::new));

        relevantValues.sort(new Comparator<AbstractMap.SimpleEntry<Integer, Boolean>>() {
            public int compare(AbstractMap.SimpleEntry<Integer, Boolean> o1, AbstractMap.SimpleEntry<Integer, Boolean> o2) {
                return o2.getKey().compareTo(o1.getKey());
            }
        });

        StringBuilder sb = new StringBuilder();

        relevantValues.forEach(ks -> sb.append(ks.getValue() ? 1 : 0));

        return Long.parseLong(sb.toString(), 2);
    }

    private record Instruction (String in1, String in2, String out, Gate gate) {

		public static Instruction getSwappedInstruction(Instruction instruction, String newOut) {
            return new Instruction(instruction.in1(), instruction.in2(), newOut, instruction.gate());
		}

    }

    private enum Gate {
        AND,
        OR,
        XOR;

        public boolean apply(boolean v1, boolean v2) {
            return switch (this) {
                case AND -> v1 && v2;
                case OR -> v1 || v2;
                case XOR -> v1 ^ v2;
                default -> false;
            };
        }

        public static Gate fromString(String s) {
            for (Gate g : Gate.values()) {
                if (g.toString().equals(s)) {
                    return g;
                }
            }

            return null;
        } 
    }

}
