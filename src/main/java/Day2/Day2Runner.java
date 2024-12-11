package Day2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day2Runner {

    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        Set<List<Integer>> reports = new HashSet<>();

        try {
            input = new BufferedReader(new FileReader("inputs/reactors.txt"));
            while ((currentLine = input.readLine()) != null) {
                // System.out.println("Raw line: " + currentLine);
                String[] vals = currentLine.split(" ");
                reports.add(List.of(vals).stream().map(Integer::parseInt).collect(Collectors.toList()));
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
        AtomicInteger count = new AtomicInteger();

        reports.forEach(report -> {
            System.out.println(report);
            List<Integer> diffs = new ArrayList<>();
            for (int i = 0; i < report.size() - 1; i++) {
                diffs.add(report.get(i+1) - report.get(i));
            }
            if (diffs.stream().allMatch(e -> (e >= 1 && e <= 3)) || diffs.stream().allMatch(e -> (e >= -3 && e<= -1))) {
                System.out.println("Safe as is");
                count.getAndIncrement();
            } else {
                for (int i = 0; i < report.size(); i++) {
                    List<Integer> reportCopy = new ArrayList<>(report);
                    reportCopy.remove(i);
                    diffs = new ArrayList<>();
                    for (int j = 0; j < reportCopy.size() - 1; j++) {
                        diffs.add(reportCopy.get(j+1) - reportCopy.get(j));
                    }
                    if (diffs.stream().allMatch(e -> (e >= 1 && e <= 3)) || diffs.stream().allMatch(e -> (e >= -3 && e<= -1))) {
                        System.out.println("Safe after removing element " + i);
                        count.getAndIncrement();
                        return;
                    }
                }
                System.out.println("Unsafe");
            }
        });

        System.out.println(count.get());
    }
}