package pt.josealm3ida.aoc.warehouse_woes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Direction;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<String> allLines = Utils.readLines("input/warehouse-woes.txt");
        int splitIdx = allLines.indexOf("");
        List<List<Character>> warehouse = parseWarehouse(allLines.subList(0, splitIdx));
        List<Direction> movements = parseMovements(allLines.subList(splitIdx + 1, allLines.size()));

        System.out.println(part1(warehouse, movements));
        System.out.println(part2(warehouse, movements));

    }

    private static List<List<Character>> parseWarehouse(List<String> warehouseLines) {
		List<List<Character>> charLists = new ArrayList<>();

        warehouseLines.forEach(l -> {
			charLists.add(new ArrayList<>(l.chars().mapToObj(c -> (char) c).toList()));
        });

		return charLists;
    }

    private static List<Direction> parseMovements(List<String> movementLines) {
        List<Direction> movements = new ArrayList<>();

        movementLines.forEach(l -> {
            movements.addAll(l.chars().mapToObj(c -> {
                return switch (c) {
                    case '<' -> Direction.LEFT;
                    case '^' -> Direction.UP;
                    case 'v' -> Direction.DOWN;
                    case '>' -> Direction.RIGHT;
                    default -> null;
                };
            }).toList());
        });

        return movements;
    }

    private static int part1(List<List<Character>> startingWarehouse, final List<Direction> movements) {
        List<List<Character>> warehouse = Utils.deepCopyArray(startingWarehouse);

        for (Direction m : movements) {
            applyMovement1(warehouse, m);
        }

        return getBoxesGPS(warehouse).stream().mapToInt(Integer::valueOf).sum();
    }

    private static int part2(List<List<Character>> startingWarehouse, final List<Direction> movements) {
        List<List<Character>> warehouse = widenWarehouse(Utils.deepCopyArray(startingWarehouse));

        for (Direction m : movements) {
            warehouse = applyMovement2(warehouse, m);
        }

        return getBoxesGPS(warehouse).stream().mapToInt(Integer::valueOf).sum();
    }

    private static void applyMovement1(List<List<Character>> warehouse, Direction movement) {
        for (int y = 0; y < warehouse.size(); y++) {
            List<Character> row = warehouse.get(y);
            for (int x = 0; x < row.size(); x++) {
                if (row.get(x) != '@') {
                    continue;
                }

                int nx = movement.calcNextCol(x);
                int ny = movement.calcNextRow(y);

                Character nc = warehouse.get(ny).get(nx);

                if (nc.equals('#')) {
                    return;
                }

                if (nc.equals('.')) {
                    warehouse.get(ny).set(nx, '@');
                    warehouse.get(y).set(x, '.');
                    return;
                }

                int crateX = nx;
                int crateY = ny;
                Character crateC = warehouse.get(crateY).get(crateX);
                while (crateC.equals('[') || crateC.equals(']')) {
                    crateX = movement.calcNextCol(crateX);
                    crateY = movement.calcNextRow(crateY);
                }

                if (warehouse.get(crateY).get(crateX).equals('#')) {
                    return;
                }

                warehouse.get(crateY).set(crateX, 'O');
                warehouse.get(ny).set(nx, '@');
                warehouse.get(y).set(x, '.');

                return;
            }
        }
    }

    private static List<List<Character>> applyMovement2(List<List<Character>> warehouse, Direction movement) {
        for (int y = 0; y < warehouse.size(); y++) {
            List<Character> row = warehouse.get(y);
            for (int x = 0; x < row.size(); x++) {
                if (row.get(x) != '@') {
                    continue;
                }

                int nx = movement.calcNextCol(x);
                int ny = movement.calcNextRow(y);

                Character nc = warehouse.get(ny).get(nx);

                if (nc.equals('#')) {
                    return warehouse;
                }

                if (nc.equals('.')) {
                    warehouse.get(ny).set(nx, '@');
                    warehouse.get(y).set(x, '.');
                    return warehouse;
                }

                boolean boxLeft = nc.equals('[');
                List<List<Character>> newWarehouse = moveWideBox(warehouse, new Coord(boxLeft ? nx : nx - 1, ny), movement);
                if (newWarehouse == null) {
                    return warehouse;
                }

                warehouse = newWarehouse;

                warehouse.get(ny).set(nx, '@');
                warehouse.get(y).set(x, '.');
                if (movement.isVertical()) {
                    warehouse.get(ny).set(boxLeft ? x + 1 : x - 1, '.');
                }

                return warehouse;
            }
        }

        return null;
    }

    private static List<List<Character>> moveWideBox(final List<List<Character>> startingWarehouse, Coord boxLeft, Direction d) {
        List<List<Character>> warehouse = Utils.deepCopyArray(startingWarehouse);

        Stack<Coord> toVisitBoxes = new Stack<>();
        toVisitBoxes.add(boxLeft);

        Set<Coord> visitedBoxes = new HashSet<>();

        while (!toVisitBoxes.isEmpty()) {
            Coord curr = toVisitBoxes.pop();
            if (visitedBoxes.contains(curr)) {
                continue;
            }
            visitedBoxes.add(curr);
            
            int nx = d.calcNextCol(curr.x());
            int ny = d.calcNextRow(curr.y());

            Character ncLeft = warehouse.get(ny).get(nx);
            Character ncRight = warehouse.get(ny).get(nx + 1);

            if (ncLeft.equals('#') || ncRight.equals('#')) {
                return null;
            }

            if (d == Direction.RIGHT && ncRight.equals('.')
                || d == Direction.LEFT && ncLeft.equals('.')
                || d.isVertical() && ncLeft.equals('.') && ncRight.equals('.')
            ) {
                warehouse.get(ny).set(nx, '[');
                warehouse.get(ny).set(nx + 1, ']');
                continue;
            }

            if (ncLeft.equals('[')) {
                toVisitBoxes.push(new Coord(nx, ny));
                continue;
            }

            if (ncLeft.equals(']')) {
                toVisitBoxes.push(new Coord(nx - 1, ny));
                if (d.isVertical()) {
                    warehouse.get(ny).set(curr.x() - 1, '.');
                }
            }

            if (ncRight.equals('[')) {
                toVisitBoxes.push(new Coord(nx + 1, ny));
                if (d.isVertical()) {
                    warehouse.get(ny).set(curr.x() + 2, '.');
                }
            }

            warehouse.get(ny).set(nx, '[');
            warehouse.get(ny).set(nx + 1, ']');
        }

        return warehouse;
    } 

    private static List<List<Character>> widenWarehouse(List<List<Character>> warehouse) {
        List<List<Character>> widenedWarehouse = new ArrayList<>();

        for (int y = 0; y < warehouse.size(); y++) {
            List<Character> row = warehouse.get(y);
            List<Character> widenedRow = new ArrayList<>();

            for (int x = 0; x < row.size(); x++) {
                Character c = warehouse.get(y).get(x);

                switch (c) {
                    case '#':
                        widenedRow.add('#');
                        widenedRow.add('#');
                        break;

                    case 'O':
                        widenedRow.add('[');
                        widenedRow.add(']');
                        break;

                    case '.':
                        widenedRow.add('.');
                        widenedRow.add('.');
                        break;

                    case '@':
                        widenedRow.add('@');
                        widenedRow.add('.');
                        break;
                }
            }

            widenedWarehouse.add(widenedRow);
        }

        return widenedWarehouse;
    }

    private static List<Integer> getBoxesGPS(List<List<Character>> warehouse) {
        List<Integer> boxesGps = new ArrayList<>();

        for (int y = 0; y < warehouse.size(); y++) {
            List<Character> row = warehouse.get(y);
            for (int x = 0; x < row.size(); x++) {
                Character c = warehouse.get(y).get(x);
                if (c.equals('O') || c.equals('[')) {
                    boxesGps.add(100 * y + x);
                }
            }
        }

        return boxesGps;
    }

    private static void prettyPrint(List<List<Character>> warehouse) {
        for (List<Character> row : warehouse) {
            for (Character c : row) {
               System.out.print(c); 
            }
            System.out.println();
        }
    }

}
