package year2024;

import base.AoCDay;
import base.AoCYear;

public class ChiefHistorian extends AoCYear {
    AoCDay[] AOC_DAYS = {
            new HistorianHysteria(), new RedNosedReports(), new MullItOver(), new CeresSearch(), new PrintQueue(),
            new GuardGallivant(), new BridgeRepair(), new ResonantCollinearity(), new DiskFragmenter(),
            new HoofIt(), new PlutonianPebbles(), new GardenGroups(), new ClawContraption(), new RestroomRedoubt(),
            new WarehouseWoes(), new ReindeerMaze(), new ChronospatialComputer()
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
