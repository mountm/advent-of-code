package year2024.Day8;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8Runner extends AoCDay {
    private static final Map<Character, List<Pair<Integer, Integer>>> antennas = new HashMap<>();
    private static final int PUZZLE_SIZE = 50;
    private static final boolean[][] hasAntiNode = new boolean[PUZZLE_SIZE][PUZZLE_SIZE];
    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        try {
            int row = 0;
            input = new BufferedReader(new FileReader("inputs/radio-map.txt"));
            while ((currentLine = input.readLine()) != null) {
                for (int i = 0; i < currentLine.length(); i++) {
                    char currentChar = currentLine.charAt(i);
                    if (currentChar != '.') {
                        System.out.println("Found antenna " + currentChar + " at position (" + row + ", " + i + ")");
                        if (antennas.containsKey(currentChar)) {
                            antennas.get(currentChar).add(Pair.of(row, i));
                        } else {
                            List<Pair<Integer, Integer>> entryList = new ArrayList<>();
                            entryList.add(Pair.of(row, i));
                            antennas.put(currentChar, entryList);
                        }
                    }
                }
                row++;
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
        antennas.entrySet().removeIf(e -> e.getValue().size() < 2);
        for (List<Pair<Integer, Integer>> list : antennas.values()) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = i+1; j < list.size(); j++) {
                    int vertStep = list.get(j).getLeft() - list.get(i).getLeft();
                    int horzStep = list.get(j).getRight() - list.get(i).getRight();
                    Pair<Integer, Integer> newPoint = Pair.of(list.get(i).getLeft() + vertStep, list.get(i).getRight() + horzStep);
                    while(newPoint.getLeft() >= 0 && newPoint.getLeft() < PUZZLE_SIZE && newPoint.getRight() >= 0 && newPoint.getRight() < PUZZLE_SIZE) {
                        hasAntiNode[newPoint.getLeft()][newPoint.getRight()] = true;
                        newPoint = Pair.of(newPoint.getLeft() + vertStep, newPoint.getRight() + horzStep);
                    }
                    newPoint = Pair.of(list.get(i).getLeft() - vertStep, list.get(i).getRight() - horzStep);
                    while(newPoint.getLeft() >= 0 && newPoint.getLeft() < PUZZLE_SIZE && newPoint.getRight() >= 0 && newPoint.getRight() < PUZZLE_SIZE) {
                        hasAntiNode[newPoint.getLeft()][newPoint.getRight()] = true;
                        newPoint = Pair.of(newPoint.getLeft() - vertStep, newPoint.getRight() - horzStep);
                    }
                    newPoint = Pair.of(list.get(j).getLeft() + vertStep, list.get(j).getRight() + horzStep);
                    while(newPoint.getLeft() >= 0 && newPoint.getLeft() < PUZZLE_SIZE && newPoint.getRight() >= 0 && newPoint.getRight() < PUZZLE_SIZE) {
                        hasAntiNode[newPoint.getLeft()][newPoint.getRight()] = true;
                        newPoint = Pair.of(newPoint.getLeft() + vertStep, newPoint.getRight() + horzStep);
                    }
                    newPoint = Pair.of(list.get(j).getLeft() - vertStep, list.get(j).getRight() - horzStep);
                    while(newPoint.getLeft() >= 0 && newPoint.getLeft() < PUZZLE_SIZE && newPoint.getRight() >= 0 && newPoint.getRight() < PUZZLE_SIZE) {
                        hasAntiNode[newPoint.getLeft()][newPoint.getRight()] = true;
                        newPoint = Pair.of(newPoint.getLeft() - vertStep, newPoint.getRight() - horzStep);
                    }
                }
            }
        }
        int count = 0;
        for (boolean[] row : hasAntiNode) {
            for (boolean entry : row) {
                if (entry) count++;
            }
        }
        System.out.println(count);
    }
}
