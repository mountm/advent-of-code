package year2023;

import base.AoCDay;
import year2023.Day3.GridNode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class GearRatios extends AoCDay {

    Pattern DIGITS = Pattern.compile("\\d+");

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 3, false, 0);
        List<GridNode> nodes = findNodes(lines);
        int[][] grid = generateAdjacencyMatrix(nodes);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = addPartNumbers(nodes, grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = addGearRatios(nodes, grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private void checkDiagGrid(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = i; j < grid.length; j++) {
                if (grid[i][j] != grid[j][i]) {
                    System.out.println("(" + i +", " + j + ") = " + grid[i][j]);
                    System.out.println("(" + j +", " + i + ") = " + grid[j][i]);
                }
            }
        }
    }

    private int[][] generateAdjacencyMatrix(List<GridNode> nodes) {
        int[][] result = new int[nodes.size()][nodes.size()];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                if (isAdjacent(nodes.get(i), nodes.get(j))) {
                    result[i][j] = 1;
                }
            }
        }
        return result;
    }

    private boolean isAdjacent(GridNode n1, GridNode n2) {
        if (n1.equals(n2)) return false;
        if (n1.isSymbol() == n2.isSymbol()) return false; // this could technically be true but we don't care
        if (Math.abs(n1.getI() - n2.getI()) > 1) return false;
        if (n1.getI() == n2.getI()) return n1.getjEnd() == n2.getjStart() || n2.getjEnd() == n1.getjStart();
        if (n1.isSymbol()) return n1.getjStart() >= n2.getjStart() - 1 && n1.getjStart() <= n2.getjEnd();
        return n2.getjStart() >= n1.getjStart() - 1 && n2.getjStart() <= n1.getjEnd();
    }

    private List<GridNode> findNodes(List<String> lines) {
        List<GridNode> nodes = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            nodes.addAll(findNodesOnLine(lines, i));
        }
        return nodes;
    }

    private List<GridNode> findNodesOnLine(List<String> lines, int i) {
        List<GridNode> nodes = new ArrayList<>();
        String line = lines.get(i);
        for (int j = 0; j < line.length(); j++) {
            if (isValidSymbol(line.charAt(j))) {
                nodes.add(new GridNode(true, i, j, j+1, 0));
            }
        }
        Matcher m = DIGITS.matcher(line);
        while(m.find()) {
            nodes.add(new GridNode(false, i, m.start(), m.end(), Integer.parseInt(m.group(), 10)));
        }
        return nodes;
    }

    private long addGearRatios(List<GridNode> nodes, int[][] grid) {
        long sum = 0L;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).isSymbol() && IntStream.of(grid[i]).sum() == 2) {
                int gearRatio = 1;
                for (int j = 0; j < grid.length; j++) {
                    if(grid[i][j] == 1) gearRatio *= nodes.get(j).getVal();
                }
                sum += gearRatio;
            }
        }
        return sum;
    }

    private long addPartNumbers(List<GridNode> nodes, int[][] grid) {
        long total = 0L;
        for (int i = 0; i < nodes.size(); i++) {
            if (!nodes.get(i).isSymbol() && IntStream.of(grid[i]).sum() > 0) total += nodes.get(i).getVal();
        }
        return total;
    }

    private boolean isValidSymbol(char c) {
        return !((c >= '0' & c <= '9') || c == '.');
    }
}
