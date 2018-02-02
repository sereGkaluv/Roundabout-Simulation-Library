package at.fhv.itm3.s2.roundabout.util.drawer;

import java.util.LinkedList;
import java.util.List;

public class Statistics {
    private String title;
    private List<StatisticsValue> values;
    private String color;

    public Statistics(String title, String color) {
        this.title = title;
        this.color = color;
        this.values = new LinkedList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<StatisticsValue> getValues() {
        return values;
    }

    public String getColor() {
        return color;
    }

    public void addValue(StatisticsValue value) {
        values.add(value);
    }
}
