package base;

import java.time.Instant;

public class AoCYear {
    public int year;
    public String[] summaries;
    public AoCYear(int year) {
        this.year = year;
        this.summaries = new String[25];
    }

    public void runAllDays() {
        long startTime = Instant.now().toEpochMilli();
        System.out.printf("%4s|%25s |%20s |%20s |%20s |%20s |%20s |%n",
                "Day #", "Puzzle name", "Parsing Time","Part 1 Time(ms)", "Part 2 Time(ms)",
                "Part 1 Answer", "Part 2 Answer");

        for (int i = 1; i <= 25; i++) {
            runOneDay(i, false);
            getSummary(i);
        }
        long diffTime = Instant.now().toEpochMilli() - startTime;
        System.out.println("Time to run all programs: " + diffTime + " ms.");
    }


    public void runOneDay(int day) {
        runOneDay(day, true);
    }

    public void runOneDay(int day, boolean printStatements) {
        System.out.printf("Day %d for year %d has not been written%n", day, year);

    }

    public void getSummary(int day) {
        System.out.println("Not yet implemented");
    }
}
