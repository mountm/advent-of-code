package base;

import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

public class AoCDay {

    public Long[] timeMarkers = new Long[]{0L,0L,0L,0L};
    public Object part1Answer;
    public Object part2Answer;
    public void solve() {
        System.out.println("Not yet implemented");
    }

    public void run(boolean printStatements) {
        solve();
        if (printStatements) {
            System.out.println("---------" + getClass().getName() + "------------");
            System.out.printf("%20s|%20s |%20s |%20s |%n", "Elapsed Times:", "Parsing Time(ms)", "Part 1 Time(ms)", "Part 2 Time(ms)");
            System.out.printf("%20s|%20d |%20d |%20d |%n", "", timeMarkers[1] - timeMarkers[0], timeMarkers[2] - timeMarkers[1], timeMarkers[3] - timeMarkers[2]);
            System.out.println("Part 1 Answer: " + part1Answer);
            System.out.println("Part 2 Answer: " + part2Answer);
        }

    }

    public void printSummary(int i) {
        String className = getClass().getName();
        String[] splitNames = className.split("\\.");
        String name = splitNames[splitNames.length-1];
        if (part1Answer instanceof String && ((String) part1Answer).length() > 17) part1Answer = String.valueOf(part1Answer).substring(0, 17) + "...";
        if (part2Answer instanceof String && ((String) part2Answer).length() > 17) part2Answer = String.valueOf(part2Answer).substring(0, 17) + "...";
        String pt1FormatStr = part1Answer instanceof String ? "%20s" : "%20d";
        String pt2FormatStr = part2Answer instanceof String ? "%20s" : "%20d";
        System.out.printf("%4d |%25s |%20d |%20d |%20d |"+pt1FormatStr + " |"+pt2FormatStr+" |%n",i,name,timeMarkers[1]-timeMarkers[0],timeMarkers[2]-timeMarkers[1],timeMarkers[3]-timeMarkers[2], part1Answer, part2Answer);
    }

    public InputStream getFileFromResourceStream(Integer year, Integer day, boolean test, Integer testSuffix) {
        String separator = FileSystems.getDefault().getSeparator();
        String filename = test ? "testcase" : "input";
        filename += (testSuffix > 0) ? String.format("%d",testSuffix) : "";
        filename += ".txt";
        String[] directories = new String[]{String.format("year%d",year),String.format("day%2d",day).replace(" ","0"),filename};
        String resourceFile = String.join(separator, directories);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceFile);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + resourceFile);
        }
        return inputStream;
    }

    public List<String> readResourceFile(Integer year, Integer day, boolean test, Integer testSuffix) {
        InputStream is = getFileFromResourceStream(year,day,test,testSuffix);
        List<String> lines = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Unable to read file" + e);
        }
        return lines;
    }

    public List<Integer> convertStringsToInts(List<String> lines) {
        return lines.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    protected String[][] convertToGrid(List<String> lines) {
        String[][] grid = new String[lines.size()][lines.get(0).length()];
        int i = 0;
        for(String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.substring(j,j+1);
            }
            i++;
        }
        return grid;
    }

    protected char[][] convertToCharGrid(List<String> lines) {
        char[][] grid = new char[lines.size()][lines.get(0).length()];
        int i = 0;
        for(String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.charAt(j);
            }
            i++;
        }
        return grid;
    }

    protected int[][] convertToIntGrid(List<String> lines) {
        // all ints must be single digits
        int[][] grid = new int[lines.size()][lines.get(0).length()];
        int i = 0;
        for (String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.charAt(j) - '0';
            }
            i++;
        }
        return grid;
    }

    protected Pair<Integer, Integer> findCharInGrid(char[][] grid, char element) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == element) {
                    return Pair.of(i, j);
                }
            }
        }
        return Pair.of(-1, -1);
    }

    protected char[][] rotateCharGridClockwise(char[][] grid) {
        char[][] newGrid = new char[grid[0].length][grid.length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                newGrid[c][grid.length-1-r] = grid[r][c];
            }
        }
        return newGrid;
    }

    protected int[][] rotateIntGridClockwise(int[][] grid) {
        int[][] newGrid = new int[grid[0].length][grid.length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                newGrid[c][grid.length-1-r] = grid[r][c];
            }
        }
        return newGrid;
    }

    protected String[][] copy2DArray(String [][] source) {
        return Arrays.stream(source)
                .map(String[]::clone)
                .toArray(String[][]::new);
    }


    protected static void printGrid(String[][] grid) {
        StringBuffer sb = new StringBuffer("\n");
        for (String[] strings : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                sb.append(strings[j]);
            }
            sb.append("\n");
        }
        sb.append("\n");
        System.out.println(sb);
    }

    protected void printGrid(char[][] grid) {
        StringBuffer sb = new StringBuffer("\n");
        for (char[] characters : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                sb.append(characters[j]);
            }
            sb.append("\n");
        }
        sb.append("\n");
        System.out.println(sb);
    }
    protected void printGrid(int[][] grid) {
        StringBuffer sb = new StringBuffer("\n");
        for (int[] characters : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                sb.append(characters[j]);
            }
            sb.append("\n");
        }
        sb.append("\n");
        System.out.println(sb);
    }


    protected List<String> convertStringToList(String input, String regex) {
        return Arrays.stream(input.split(regex)).collect(Collectors.toList());
    }

    protected <T> List<List<T>> copyNestedList(List<List<T>> inputList) {
        List<List<T>> result = new ArrayList<>();
        for (List<T> list : inputList) {
            result.add(new ArrayList<>(list));
        }
        return result;
    }

    protected boolean isSafeCoord(int i, int j, int size) {
        return (i >= 0 && i < size && j >= 0 && j < size);
    }

    protected int getManhattanDistance(Pair<Integer, Integer> point1, Pair<Integer, Integer> point2) {
        return Math.abs(point1.getLeft() - point2.getLeft()) + Math.abs(point1.getRight() - point2.getRight());
    }

    protected Pair<Integer, Integer> moveInDirection(Pair<Integer, Integer> startPos, AocUtils.Direction direction) {
        return Pair.of(startPos.getLeft() + direction.getiStep(), startPos.getRight() + direction.getjStep());
    }

    protected Pair<Integer, Map<Pair<Integer, Integer>, Pair<Integer, Integer>>> aStarSearch(char[][] grid, Pair<Integer, Integer> startPos, Pair<Integer, Integer> endPos) {
        Map<Pair<Integer, Integer>, Integer> nodeCosts = new HashMap<>();
        nodeCosts.put(startPos, 0);
        PriorityQueue<Pair<Integer, Integer>> frontier = new PriorityQueue<>(Comparator.comparingInt(o -> nodeCosts.get(o) + getManhattanDistance(o, endPos)));
        frontier.add(startPos);
        Map<Pair<Integer, Integer>, Pair<Integer, Integer>> prevNode = new HashMap<>();

        while (!frontier.isEmpty()) {
            Pair<Integer, Integer> current = frontier.poll();

            if (current.equals(endPos)) return Pair.of(nodeCosts.get(current), prevNode);
            for (Pair<Integer, Integer> next : getNeighbors(current, grid)) {
                int newCost = nodeCosts.get(current) + 1;
                if (!nodeCosts.containsKey(next) || newCost < nodeCosts.get(next)) {
                    nodeCosts.put(next, newCost);
                    frontier.add(next);
                    prevNode.put(next, current);
                }
            }
        }
        return Pair.of(-1, Map.of());
    }

    protected Set<Pair<Integer, Integer>> getNeighbors(Pair<Integer, Integer> current, char[][] grid) {
        return EnumSet.allOf(AocUtils.Direction.class).stream().map(d -> moveInDirection(current, d)).filter(point -> isSafeCoord(point.getLeft(), point.getRight(), grid.length) && grid[point.getLeft()][point.getRight()] != '#').collect(Collectors.toSet());
    }

    protected List<Pair<Integer, Integer>> getCellLocations(Map<Pair<Integer, Integer>, Pair<Integer, Integer>> map, Pair<Integer, Integer> endingCell) {
        List<Pair<Integer, Integer>> cellLocations = new LinkedList<>();
        cellLocations.add(endingCell);
        Queue<Pair<Integer, Integer>> cellsToCheck = new LinkedList<>();
        cellsToCheck.add(map.get(endingCell));
        while(!cellsToCheck.isEmpty()) {
            Pair<Integer, Integer> nextCell = cellsToCheck.poll();
            cellLocations.add(nextCell);
            if (map.containsKey(nextCell)) cellsToCheck.add(map.get(nextCell));
        }
        return cellLocations;
    }
}
