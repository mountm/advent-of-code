package year2023;

import base.AoCDay;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trebuchet extends AoCDay {

    String STRING_PATTERN_REGEX = "one|two|three|four|five|six|seven|eight|nine";
    Pattern FORWARDS_PATTERN = Pattern.compile("[0-9]|" + STRING_PATTERN_REGEX);
    Pattern REVERSE_PATTERN = Pattern.compile("[0-9]|" + StringUtils.reverse(STRING_PATTERN_REGEX));
    Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 1, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = lines.stream().mapToInt(l -> getCalibrationValue(l, false)).sum();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = lines.stream().mapToInt(l -> getCalibrationValue(l, true)).sum();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int getCalibrationValue(String line, boolean interpretStrings) {
        if (!interpretStrings) {
            return getFirstDigit(line) * 10 + getFirstDigit(StringUtils.reverse(line));
        }
        return getFirstDigitWithStringInterpretation(line, false) * 10 + getFirstDigitWithStringInterpretation(StringUtils.reverse(line), true);
    }

    private int getFirstDigit(String line) {
        Matcher m = DIGIT_PATTERN.matcher(line);
        m.find();
        return Integer.parseInt(m.group(), 10);
    }

    private int getFirstDigitWithStringInterpretation(String line, boolean reverse) {
        Matcher m = reverse ? REVERSE_PATTERN.matcher(line) : FORWARDS_PATTERN.matcher(line);
        m.find();
        int val;
        if (m.group().length() == 1) {
            val = Integer.parseInt(m.group(), 10);
        } else {
            String group = reverse ? StringUtils.reverse(m.group()) : m.group();
            switch(group) {
                case "one":
                    val = 1;
                    break;
                case "two":
                    val = 2;
                    break;
                case "three":
                    val = 3;
                    break;
                case "four":
                    val = 4;
                    break;
                case "five":
                    val = 5;
                    break;
                case "six":
                    val = 6;
                    break;
                case "seven":
                    val = 7;
                    break;
                case "eight":
                    val = 8;
                    break;
                default:
                    val = 9;
            }
        }
        return val;
    }
}
