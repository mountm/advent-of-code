import base.AoCYear;
import year2023.SnowProduction;
import year2024.ChiefHistorian;

import static java.lang.Integer.parseInt;

public class AdventOfCode {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("expected input:  <year> <day>, e.g. 2021 1");
            return;
        }
        int year = parseInt(args[0]);
        int day = parseInt(args[1]);
        AoCYear[] aocYears = {
                new SnowProduction(year),
                new ChiefHistorian(year)
        };
        AoCYear aoCYear = aocYears[year - 2023];
        if (day > 0) {
            aoCYear.runOneDay(day);
        } else {
            aoCYear.runAllDays();
        }
    }
}
