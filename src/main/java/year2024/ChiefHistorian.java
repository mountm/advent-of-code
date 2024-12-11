package year2024;

import base.AoCDay;
import base.AoCYear;
import year2024.Day1.Day1Runner;
import year2024.Day2.Day2Runner;
import year2024.Day3.Day3Runner;
import year2024.Day4.Day4Runner;
import year2024.Day5.Day5Runner;
import year2024.Day6.Day6Runner;
import year2024.Day7.Day7Runner;

public class ChiefHistorian extends AoCYear {
    AoCDay AOC_DAYS[] = {
            new Day1Runner(), new Day2Runner(), new Day3Runner(), new Day4Runner(),
            new Day5Runner(), new Day6Runner(), new Day7Runner(), new ResonantCollinearity(),
            new DiskFragmenter(), new HoofIt(), new PlutonianPebbles()
    };

    public ChiefHistorian(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        runOneDay(day, true);
    }

    @Override
    public void runOneDay(int day, boolean printStatements) {
        AoCDay aoCDay = AOC_DAYS[day-1];
        aoCDay.run(printStatements);
    }

    @Override
    public void getSummary(int day) {
        AOC_DAYS[day-1].printSummary(day);
    }

}
