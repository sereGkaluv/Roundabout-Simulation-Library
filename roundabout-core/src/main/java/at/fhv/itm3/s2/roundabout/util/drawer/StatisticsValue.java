package at.fhv.itm3.s2.roundabout.util.drawer;

import java.util.function.Supplier;

public class StatisticsValue {
    private Supplier<String> method;
    private String value;
    private int x;
    private int y;

    public StatisticsValue(String value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }

    public StatisticsValue(Supplier<String> method, int x, int y) {
        this.method = method;
        this.x = x;
        this.y = y;
    }

    public String getValue() {
        if (value == null && method != null) {
            return method.get();
        }
        return value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
