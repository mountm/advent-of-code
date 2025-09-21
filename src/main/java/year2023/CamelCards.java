package year2023;

import base.AoCDay;
import year2023.Day7.Hand;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CamelCards extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 7, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        List<Hand> hands = makeHands(lines);
        part1Answer = totalWinnings(hands);
        timeMarkers[2] = Instant.now().toEpochMilli();
        addJokers(hands);
        Collections.sort(hands);
        part2Answer = totalWinnings(hands);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private void addJokers(List<Hand> hands) {
        for (Hand hand : hands) {
            if (hand.getCards().contains("C")) {
                hand.setCards(hand.getCards().replace("C", "1"));
            }
        }
    }

    private long totalWinnings(List<Hand> hands) {
        long runningTotal = 0L;
        for (int i = 0; i < hands.size(); i++) {
            runningTotal += ((long) (i + 1) * hands.get(i).getBid());
        }
        return runningTotal;
    }

    private List<Hand> makeHands(List<String> lines) {
        List<Hand> hands = new LinkedList<>();
        for (String line : lines) {
            hands.add(new Hand(changeChars(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1], 10)));
        }
        Collections.sort(hands);
        return hands;
    }

    private String changeChars(String s) {
        return s.replace("T", "B")
                .replace("J", "C")
                .replace("Q", "D")
                .replace("K", "E")
                .replace("A", "F");
    }
}
