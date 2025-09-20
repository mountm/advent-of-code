package year2023.Day3;

import java.util.Objects;

public class GridNode {

    boolean isSymbol;
    int i;
    int jStart;
    int jEnd;
    int val;

    public GridNode(boolean isSymbol, int i, int jStart, int jEnd, int val) {
        this.isSymbol = isSymbol;
        this.i = i;
        this.jStart = jStart;
        this.jEnd = jEnd;
        this.val = val;
    }

    public boolean isSymbol() {
        return isSymbol;
    }

    public void setSymbol(boolean symbol) {
        isSymbol = symbol;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getjStart() {
        return jStart;
    }

    public void setjStart(int jStart) {
        this.jStart = jStart;
    }

    public int getjEnd() {
        return jEnd;
    }

    public void setjEnd(int jEnd) {
        this.jEnd = jEnd;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GridNode gridNode = (GridNode) o;
        return getI() == gridNode.getI() && getjStart() == gridNode.getjStart() && getjEnd() == gridNode.getjEnd();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getI(), getjStart(), getjEnd());
    }
}
