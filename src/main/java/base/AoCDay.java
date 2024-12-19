package base;

import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                newGrid[c][r] = grid[r][c];
            }
        }
        return newGrid;
    }

    protected char[][] rotateCharGridCounterClockwise(char[][] grid) {
        int maxR = grid.length;
        int maxC = grid[0].length;
        char[][] newGrid = new char[maxR][maxC];
        for (int r = 0; r < maxR; r++) {
            for (int c = 0; c < maxC; c++) {
                newGrid[c][maxR-1-r] = grid[r][c];
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
}
