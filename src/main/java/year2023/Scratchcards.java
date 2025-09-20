package year2023;

import base.AoCDay;
import org.javatuples.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Scratchcards extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 4, false, 0);
        List<Pair<Set<Integer>, Set<Integer>>> cards = generateCards(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = addCardValues(cards);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = countCards(cards);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long countCards(List<Pair<Set<Integer>, Set<Integer>>> cards) {
        Map<Long, Long> cardCounts = new HashMap<>();
        for (int i = 0; i < cards.size(); i++) {
            cardCounts.merge((long) i, 1L, Long::sum);
            int winningNumbers = countWinningNumbers(cards.get(i));
            long numOfThisCard = cardCounts.get((long) i);
            for (long j = 1; j <= winningNumbers; j++) {
                cardCounts.merge(j + i, numOfThisCard, Long::sum);
            }
        }
        return cardCounts.values().stream().mapToLong(Long::valueOf).sum();
    }

    private long addCardValues(List<Pair<Set<Integer>, Set<Integer>>> cards) {
        return cards.stream().mapToLong(this::getCardValue).sum();
    }

    private long getCardValue(Pair<Set<Integer>, Set<Integer>> card) {
        int winningNumbers = countWinningNumbers(card);
        if (winningNumbers == 0) return 0;
        return 1L << (winningNumbers - 1);
    }

    private int countWinningNumbers(Pair<Set<Integer>, Set<Integer>> card) {
        Set<Integer> winningNumbers = card.getValue0();
        winningNumbers.retainAll(card.getValue1());
        return winningNumbers.size();
    }

    private List<Pair<Set<Integer>, Set<Integer>>> generateCards(List<String> lines) {
        List<Pair<Set<Integer>, Set<Integer>>> cards = new ArrayList<>();
        for (String line : lines) {
            Pair<Set<Integer>, Set<Integer>> pair = Pair.with(
                    Arrays.stream(line.split(": +")[1].split(" +\\| +")[0].split(" +")).map(Integer::parseInt).collect(Collectors.toSet()),
                    Arrays.stream(line.split(": +")[1].split(" +\\| +")[1].split(" +")).map(Integer::parseInt).collect(Collectors.toSet())
            );
            cards.add(pair);
        }
        return cards;
    }
}
