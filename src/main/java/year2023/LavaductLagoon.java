package year2023;

import base.AoCDay;
import base.AocUtils.Direction;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class LavaductLagoon extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 18, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = getLagoonArea(getInstructions(lines));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = getLagoonArea(getHexInstructions(lines));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private List<Pair<Direction, Long>> getHexInstructions(List<String> lines) {
        List<Pair<Direction, Long>> result = new ArrayList<>();
        for (String line : lines) {
            String fullHex = line.substring(line.indexOf('#') + 1, line.length()-2);
            Direction d = switch(line.charAt(line.length()-2)) {
                case '0' -> Direction.RIGHT;
                case '1' -> Direction.DOWN;
                case '2' -> Direction.LEFT;
                default -> Direction.UP;
            };
            result.add(Pair.of(d, Long.parseLong(fullHex, 16)));
        }
        return result;
    }

    private List<Pair<Direction, Long>> getInstructions(List<String> lines) {
        return lines.stream().map( l -> Pair.of(findDirection(l.charAt(0)), Long.parseLong(l.split(" +")[1], 10))).toList();
    }

    private Direction findDirection(char c) {
        return switch (c) {
            case 'D' -> Direction.DOWN;
            case 'U' -> Direction.UP;
            case 'L' -> Direction.LEFT;
            default -> Direction.RIGHT;
        };
    }

    private long getLagoonArea(List<Pair<Direction, Long>> instructions) {
        // shoelace formula + pick's theorem just like the pipe maze

        List<Pair<Long, Long>> points = new ArrayList<>();
        long perimeter = 0L;
        Pair<Long, Long> currentPoint = Pair.of(0L, 0L);
        points.add(currentPoint);
        for (Pair<Direction, Long> instruction : instructions) {
            perimeter += instruction.getRight();
            currentPoint = Pair.of(
                    currentPoint.getLeft() + instruction.getRight() * instruction.getLeft().getiStep(),
                    currentPoint.getRight() + instruction.getRight() * instruction.getLeft().getjStep());
            points.add(currentPoint);
        }

        long doubleArea = 0;
        for (int i = 0; i < points.size()-1; i++) {
            doubleArea += (points.get(i).getRight() + points.get(i+1).getRight()) * (points.get(i).getLeft() - points.get(i+1).getLeft());
        }
        assert doubleArea % 2 == 0;
        assert perimeter % 2 == 0;
        long area = Math.abs(doubleArea / 2);

        return area + 1 + perimeter / 2;
    }
}
