package Day3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3Runner {

    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        Pattern pattern = Pattern.compile("don't\\(\\)|do\\(\\)|mul\\([0-9]{1,3},[0-9]{1,3}\\)");
        int total = 0;
        boolean doMult = true;

        try {
            input = new BufferedReader(new FileReader("inputs/memory.txt"));
            while ((currentLine = input.readLine()) != null) {
                Matcher matcher = pattern.matcher(currentLine);
                while (matcher.find()) {
                    System.out.println("Found match: " + matcher.group());
                    if (matcher.group().equals("don't()")) {
                        doMult = false;
                    } else if (matcher.group().equals("do()")) {
                        doMult = true;
                    } else if (doMult) {
                        System.out.println(matcher.group().substring(4, matcher.group().length() - 1));
                        String[] nums = matcher.group().substring(4, matcher.group().length() - 1).split(",");
                        total += Integer.parseInt(nums[0], 10) * Integer.parseInt(nums[1], 10);
                    }
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

        System.out.println(total);
    }
}
