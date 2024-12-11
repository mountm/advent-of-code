package Day1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day1Runner {

    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        BufferedReader input = null;
        String currentLine;

        try {
            input = new BufferedReader(new FileReader("inputs/locations.txt"));
            while ((currentLine = input.readLine()) != null) {
                System.out.println("Raw line: " + currentLine);
                String[] vals = currentLine.split("   ");
                list1.add(Integer.parseInt(vals[0], 10));
                list2.add(Integer.parseInt(vals[1], 10));
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
        Map<Integer, Long> list2Counts = list2.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        int sum = 0;
        for(int i : list1) {
            sum += i * list2Counts.getOrDefault(i, 0L);
        };

        System.out.println(sum);
    }
}
