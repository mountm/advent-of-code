package year2023.Day7;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Hand implements Comparable<Hand> {

    private String cards;
    private int bid;
    private HandStrength strength;

    public Hand(String cards, int bid) {
        this.cards = cards;
        this.bid = bid;
        strength = calculateHandStrength(cards);
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
        strength = calculateHandStrength(cards);
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    @Override
    public int compareTo(Hand o) {
       if (strength.compareTo(o.strength) != 0) return strength.compareTo(o.strength);
       return cards.compareTo(o.cards);
    }

    private static HandStrength calculateHandStrength(String cards) {
        Map<Character, Integer> cardCounts = new HashMap<>();
        for (char c : cards.toCharArray()) {
            cardCounts.merge(c, 1, Integer::sum);
        }
        if (cardCounts.containsKey('1')) {
            Character mostCommonNonJoker = cardCounts.entrySet().stream().filter(e -> e.getKey() != '1').max(Map.Entry.comparingByValue()).orElse(Map.entry('A', 0)).getKey();
            cardCounts.merge(mostCommonNonJoker, cardCounts.remove('1'), Integer::sum);
        }
        if (cardCounts.keySet().size() == 1) return HandStrength.FIVE_OF_A_KIND;
        if (cardCounts.keySet().size() == 2) {
            if (cardCounts.values().stream().anyMatch(v -> v == 4)) return HandStrength.FOUR_OF_A_KIND;
            return HandStrength.FULL_HOUSE;
        }
        if (cardCounts.keySet().size() == 3) {
            if (cardCounts.values().stream().anyMatch(v -> v == 3)) return HandStrength.THREE_OF_A_KIND;
            return HandStrength.TWO_PAIR;

        }
        if (cardCounts.keySet().size() == 4) return HandStrength.ONE_PAIR;
        return HandStrength.HIGH_CARD;

    }
}
