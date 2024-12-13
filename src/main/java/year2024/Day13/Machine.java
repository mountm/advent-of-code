package year2024.Day13;

import org.apache.commons.lang3.tuple.Pair;

public class Machine {
    private Pair<Integer, Integer> aBtn;
    private Pair<Integer, Integer> bBtn;
    private Pair<Long, Long> prize;

    public Machine(Pair<Integer, Integer> aBtn, Pair<Integer, Integer> bBtn, Pair<Long, Long> prize) {
        this.aBtn = aBtn;
        this.bBtn = bBtn;
        this.prize = prize;
    }

    public Machine() {
    }

    public Pair<Integer, Integer> getaBtn() {
        return aBtn;
    }

    public void setaBtn(Pair<Integer, Integer> aBtn) {
        this.aBtn = aBtn;
    }

    public Pair<Integer, Integer> getbBtn() {
        return bBtn;
    }

    public void setbBtn(Pair<Integer, Integer> bBtn) {
        this.bBtn = bBtn;
    }

    public Pair<Long, Long> getPrize() {
        return prize;
    }

    public void setPrize(Pair<Long, Long> prize) {
        this.prize = prize;
    }
}
