package at.fhv.itm3.s2.roundabout.util.drawer;

public class StatisticsValue {
    private String value;
    private int x;
    private int y;

    public StatisticsValue(String value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }

    public String getValue() {
        return value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
