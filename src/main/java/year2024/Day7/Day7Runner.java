package year2024.Day7;

import base.AoCDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day7Runner extends AoCDay {
    public static List<List<Long>> equations = new ArrayList<>();
    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        try {
            input = new BufferedReader(new FileReader("inputs/equations.txt"));
            while ((currentLine = input.readLine()) != null) {
                List<Long> equation = new ArrayList<>();
                equation.add(Long.parseLong(currentLine.split(":")[0],10));
                equation.addAll(Arrays.stream(currentLine.split(":")[1].trim().split(" ")).map(Long::parseLong).collect(Collectors.toList()));
                equations.add(equation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long sum = 0;
        for (List<Long> equation : equations) {
            long desiredTotal = equation.remove(0);
            if (canGetDesiredTotal(desiredTotal, equation)) {
                sum += desiredTotal;
            }
        }
        System.out.println(sum);
    }

    private static boolean canGetDesiredTotal(long desiredTotal, List<Long> equation) {
        if (equation.size() < 2) return equation.get(0) == desiredTotal;
        if (desiredTotal < equation.get(0)) return false;
        long addVal = equation.get(0) + equation.get(1);
        long mulVal = equation.get(0) * equation.get(1);
        long concatVal = Long.parseLong(String.valueOf(equation.get(0)) + String.valueOf(equation.get(1)), 10);
        List<Long> addList = new ArrayList<>();
        List<Long> mulList = new ArrayList<>();
        List<Long> concatList = new ArrayList<>();
        addList.add(addVal);
        mulList.add(mulVal);
        concatList.add(concatVal);
        addList.addAll(equation.subList(2, equation.size()));
        mulList.addAll(equation.subList(2, equation.size()));
        concatList.addAll(equation.subList(2, equation.size()));
        return canGetDesiredTotal(desiredTotal, addList) || canGetDesiredTotal(desiredTotal, mulList) || canGetDesiredTotal(desiredTotal, concatList);
    }

}
