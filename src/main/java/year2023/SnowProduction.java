package year2023;

import base.AoCDay;
import base.AoCYear;

public class SnowProduction extends AoCYear {
    AoCDay[] AOC_DAYS = {
            new Trebuchet(), new CubeConundrum(), new GearRatios(), new Scratchcards(), new Fertilizer(),
            new WaitForIt(), new CamelCards(), new HauntedWasteland(), new MirageMaintenance(), new PipeMaze(),
            new CosmicExpansion()
    };

    public SnowProduction(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day, boolean printStatements) {
        AoCDay aoCDay = AOC_DAYS[day - 1];
        aoCDay.run(printStatements);
    }

    @Override
    public void getSummary(int day) {
        AOC_DAYS[day - 1].printSummary(day);
    }

    @Override
    public int getNumDays() {
        return AOC_DAYS.length;
    }

}
