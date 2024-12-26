package year2024;

import base.AoCDay;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static year2024.CrossedWires.AdderGate.*;

public class CrossedWires extends AoCDay {

    enum AdderGate {
        CARRY_OR,
        SUM_XOR,
        INPUTS_XOR,
        INPUTS_AND,
        CARRY_AND
    }

    private final int INPUT_SIZE = 44;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 24, false, 0);
        Map<String, Long> wireValues = new HashMap<>();
        // input1, input2, operation, output
        Set<Quartet<String, String, String, String>> operations = new HashSet<>();
        for (String line : lines) {
            if (line.contains(": ")) {
                String[] vals = line.split(": ");
                wireValues.put(vals[0], Long.parseLong(vals[1], 10));
            } else if (line.contains(" -> ")) {
                String[] vals = line.split(" -> ");
                String output = vals[1];
                String[] inputs = vals[0].split(" ");
                String input1 = inputs[0];
                String input2 = inputs[2];
                String operation = inputs[1];
                if (!wireValues.containsKey(input1)) wireValues.put(input1, null);
                if (!wireValues.containsKey(input2)) wireValues.put(input2, null);
                if (!wireValues.containsKey(output)) wireValues.put(output, null);
                operations.add(Quartet.with(input1, input2, operation, output));
            }
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solvePartOne(wireValues, new HashSet<>(operations));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePartTwo(operations);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int solvePartTwo(Set<Quartet<String, String, String, String>> operations) {
        Map<AdderGate, Set<Triplet<String, String, String>>> assignments = new HashMap<>();
        Set<String> faultyElements = new HashSet<>();
        assignments.put(INPUTS_XOR, operations.stream().filter(o -> o.getValue2().equals("XOR") && (o.getValue0().contains("x") || o.getValue0().contains("y"))).map(o -> Triplet.with(o.getValue0(), o.getValue1(), o.getValue3())).collect(Collectors.toSet()));
        assignments.put(INPUTS_AND, operations.stream().filter(o -> o.getValue2().equals("AND") && (o.getValue0().contains("x") || o.getValue0().contains("y"))).map(o -> Triplet.with(o.getValue0(), o.getValue1(), o.getValue3())).collect(Collectors.toSet()));
        assignments.put(SUM_XOR, operations.stream().filter(o -> o.getValue2().equals("XOR") && !o.getValue0().contains("x") && !o.getValue0().contains("y")).map(o -> Triplet.with(o.getValue0(), o.getValue1(), o.getValue3())).collect(Collectors.toSet()));
        assignments.put(CARRY_AND, operations.stream().filter(o -> o.getValue2().equals("AND") && !o.getValue0().contains("x") && !o.getValue0().contains("y")).map(o -> Triplet.with(o.getValue0(), o.getValue1(), o.getValue3())).collect(Collectors.toSet()));
        assignments.put(CARRY_OR, operations.stream().filter(o -> o.getValue2().equals("OR")).map(o -> Triplet.with(o.getValue0(), o.getValue1(), o.getValue3())).collect(Collectors.toSet()));
        for (Triplet<String, String, String> assignment : assignments.get(INPUTS_XOR)) {
            String presumptiveOutputWire = assignment.getValue2();
            if (assignment.getValue0().contains("00")) {
                // half adder, this should be z00
                if (!presumptiveOutputWire.equals("z00")) {
                    faultyElements.add(presumptiveOutputWire);
                    faultyElements.add("z00");
                }
            } else {
                // this should feed into a SUM_XOR and a CARRY_AND
                if (assignments.get(SUM_XOR).stream().noneMatch(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire))) {
                    System.out.println(presumptiveOutputWire);
                    System.out.println("Claims to be the output of INPUTS_XOR, but it doesn't feed a SUM_XOR");
                    System.out.println(operations.stream().filter(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire)).collect(Collectors.toSet()));
                    faultyElements.add(presumptiveOutputWire);
                }
                if (assignments.get(CARRY_AND).stream().noneMatch(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire))) {
                    System.out.println(presumptiveOutputWire);
                    System.out.println("Claims to be the output of INPUTS_XOR, but it doesn't feed a CARRY_AND");
                    System.out.println(operations.stream().filter(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire)).collect(Collectors.toSet()));
                    faultyElements.add(presumptiveOutputWire);
                }
            }
        }
        for (Triplet<String, String, String> assignment : assignments.get(INPUTS_AND)) {
            String presumptiveOutputWire = assignment.getValue2();
            if (assignment.getValue0().contains("00")) {
                // half adder, this should be treated as a carry bit
                // this should feed into a SUM_XOR and a CARRY_AND
                if (assignments.get(SUM_XOR).stream().noneMatch(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire))) {
                    System.out.println(presumptiveOutputWire);
                    System.out.println("Claims to be the output of INPUTS_AND in half adder, but it doesn't feed a SUM_XOR");
                    System.out.println(operations.stream().filter(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire)).collect(Collectors.toSet()));
                    faultyElements.add(presumptiveOutputWire);
                }
                if (assignments.get(CARRY_AND).stream().noneMatch(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire))) {
                    System.out.println(presumptiveOutputWire);
                    System.out.println("Claims to be the output of INPUTS_AND in half adder, but it doesn't feed a CARRY_AND");
                    System.out.println(operations.stream().filter(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire)).collect(Collectors.toSet()));
                    faultyElements.add(presumptiveOutputWire);
                }
            } else {
                // this should feed into an OR
                if (assignments.get(CARRY_OR).stream().noneMatch(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire))) {
                    System.out.println(presumptiveOutputWire);
                    System.out.println("Claims to be the output of INPUTS_AND, but it doesn't feed a CARRY_OR");
                    System.out.println(operations.stream().filter(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire)).collect(Collectors.toSet()));
                    faultyElements.add(presumptiveOutputWire);
                }
            }
        }
        for (Triplet<String, String, String> assignment : assignments.get(SUM_XOR)) {
            String presumptiveOutputWire = assignment.getValue2();
            // this should be an output bit
            if (!presumptiveOutputWire.contains("z")) {
                System.out.println(presumptiveOutputWire);
                System.out.println("Claims to be the output of SUM_XOR, but it's not an output bit");
                System.out.println(operations.stream().filter(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire)).collect(Collectors.toSet()));
                faultyElements.add(presumptiveOutputWire);
            }
        }
        for (Triplet<String, String, String> assignment : assignments.get(CARRY_AND)) {
            String presumptiveOutputWire = assignment.getValue2();
            // this should feed into an OR
            if (assignments.get(CARRY_OR).stream().noneMatch(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire))) {
                System.out.println(presumptiveOutputWire);
                System.out.println("Claims to be the output of CARRY_AND, but it doesn't feed a CARRY_OR");
                System.out.println(operations.stream().filter(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire)).collect(Collectors.toSet()));
                faultyElements.add(presumptiveOutputWire);
            }
        }
        for (Triplet<String, String, String> assignment : assignments.get(CARRY_OR)) {
            String presumptiveOutputWire = assignment.getValue2();
            // this should either be the final output bit, or feed into a SUM_XOR and CARRY_AND
            if (!presumptiveOutputWire.equals("z" + (INPUT_SIZE + 1))) {
                if (assignments.get(SUM_XOR).stream().noneMatch(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire))) {
                    System.out.println(presumptiveOutputWire);
                    System.out.println("Claims to be the output of CARRY_OR, but it doesn't feed a SUM_XOR");
                    System.out.println(operations.stream().filter(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire)).collect(Collectors.toSet()));
                    faultyElements.add(presumptiveOutputWire);
                }
                if (assignments.get(CARRY_AND).stream().noneMatch(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire))) {
                    System.out.println(presumptiveOutputWire);
                    System.out.println("Claims to be the output of CARRY_OR, but it doesn't feed a CARRY_AND");
                    System.out.println(operations.stream().filter(o -> o.getValue0().equals(presumptiveOutputWire) || o.getValue1().equals(presumptiveOutputWire)).collect(Collectors.toSet()));
                    faultyElements.add(presumptiveOutputWire);
                }
            }
        }
        System.out.println(faultyElements);
        return faultyElements.size();
    }

    private long solvePartOne(Map<String, Long> wireValues, Set<Quartet<String, String, String, String>> operations) {
        while (!operations.isEmpty()) {
            for (Iterator<Quartet<String, String, String, String>> i = operations.iterator(); i.hasNext();) {
                Quartet<String, String, String, String> operation = i.next();
                Pair<String, Long> result = doOperation(operation, wireValues);
                if (result != null) {
                    wireValues.put(result.getValue0(), result.getValue1());
                    i.remove();
                }
            }
        }
        return wireValues.entrySet().stream().filter(e -> e.getKey().contains("z")).map(e -> e.getValue() << Integer.parseInt(e.getKey().substring(1), 10)).reduce(Long::sum).orElse(-1L);
    }

    private Pair<String, Long> doOperation(Quartet<String, String, String, String> operation, Map<String, Long> wireValues) {
        Long input1 = wireValues.get(operation.getValue0());
        Long input2 = wireValues.get(operation.getValue1());
        if (input1 == null || input2 == null) return null;
        return switch (operation.getValue2()) {
            case "AND" -> Pair.with(operation.getValue3(), input1 & input2);
            case "OR" -> Pair.with(operation.getValue3(), input1 | input2);
            case "XOR" -> Pair.with(operation.getValue3(), input1 ^ input2);
            default -> null;
        };
    }
}
