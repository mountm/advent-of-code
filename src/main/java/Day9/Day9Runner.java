package Day9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day9Runner {
    public static List<Long> longList = new ArrayList<>();
    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        boolean isFile = true;
        long fileId = 0;
        try {
            input = new BufferedReader(new FileReader("inputs/disk-map.txt"));
            while ((currentLine = input.readLine()) != null) {
                for (char val : currentLine.toCharArray()) {
                    long count = Long.parseLong(String.valueOf(val), 10);
                    if (isFile) {
                        for (int i = 0; i < count; i++) {
                            longList.add(fileId);
                        }
                        fileId++;
                    } else {
                        for (int i = 0; i < count; i++) {
                            longList.add(null);
                        }
                    }
                    isFile = !isFile;
                }
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
        fileId--;
        int firstNullIdx = longList.indexOf(null);
        int firstFileIdx = longList.indexOf(fileId);
        while(fileId > 0) {
            System.out.println("Checking file id " + fileId);
            moveFile(longList, firstNullIdx, firstFileIdx);
            fileId--;
            firstNullIdx = longList.indexOf(null);
            firstFileIdx = longList.indexOf(fileId);
        }
        long sum = 0;
        for (int i = 0; i < longList.size(); i++) {
            if (longList.get(i) != null) {
                sum += longList.get(i) * i;
            }
        }

        System.out.println(sum);
    }

    private static void moveFile(List<Long> longList, int nullIdx, int firstFileIdx) {
        if (nullIdx > firstFileIdx) return;
        long fileVal = longList.get(firstFileIdx);
        long count = longList.stream().filter(e -> e != null && e == fileVal).count();
        boolean doesFitHere = true;
        int i = 1;
        while (i < count) {
            if (longList.get(nullIdx + i) != null) {
                doesFitHere = false;
                break;
            }
            i++;
        }
        if (doesFitHere) {
            for (i = 0; i < count; i++) {
                longList.set(nullIdx + i, fileVal);
                longList.set(firstFileIdx + i, null);
            }
            return;
        }
        int nextNullBlockIdx = longList.subList(nullIdx + i, longList.size()).indexOf(null) + nullIdx + i;
        if (nextNullBlockIdx < firstFileIdx) {
            moveFile(longList, nextNullBlockIdx, firstFileIdx);
        }
    }
}
