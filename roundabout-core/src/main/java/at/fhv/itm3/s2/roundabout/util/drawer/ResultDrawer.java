package at.fhv.itm3.s2.roundabout.util.drawer;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ResultDrawer extends JFrame {
    private final LinkedList<Statistics> statistics;
    private final Image background;
    private Graphics2D grapics;

    public ResultDrawer(String title, String backgroundPicture, LinkedList<Statistics> statistics) {
        this.background = Toolkit.getDefaultToolkit().getImage(getClass().getResource(backgroundPicture));

        ImageIcon icon = new ImageIcon(background);
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        if (width > 1000 || height > 1000) {
            throw new IllegalStateException("background image maximum width and height is 1000px");
        }

        this.statistics = statistics;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, width, height);
        setTitle(title);
    }

    public void paint(Graphics g) {
        grapics = (Graphics2D) g;

        grapics.drawImage(background, 0, 0, this);

        int legendY = 40;
        for (Statistics stat : statistics) {
            grapics.setColor(Color.decode(stat.getColor()));
            grapics.drawString(stat.getTitle(), 20, legendY);
            legendY += 20;
            for (StatisticsValue val : stat.getValues()) {
                grapics.drawString(val.getValue(), val.getX(), val.getY());
            }
        }

        grapics.finalize();
    }

    public static void main(String[] args) throws InterruptedException {
        LinkedList<Statistics> statistics = new LinkedList<>();
        Statistics firstStatistic = new Statistics("first test", "#FF0000");
        statistics.add(firstStatistic);
        firstStatistic.addValue(new StatisticsValue("300x200", 300, 200));
        firstStatistic.addValue(new StatisticsValue("500x500", 500, 500));
        Statistics secondStatistic = new Statistics("second test", "#00FFFF");
        statistics.add(secondStatistic);
        secondStatistic.addValue(new StatisticsValue("200x300", 200, 300));
        secondStatistic.addValue(new StatisticsValue("400x400", 400, 400));
        secondStatistic.addValue(new StatisticsValue("600x200", 600, 200));
        Statistics thirdStatistic = new Statistics("third test", "#00FF00");
        statistics.add(thirdStatistic);
        Example value = new Example("old one");
        thirdStatistic.addValue(new StatisticsValue(value::getValue, 500, 300));
        value.setValue("500x300");


        ResultDrawer window = new ResultDrawer("Kreisverkehr Dornbirn Nord", "/dornbirn-nord.png", statistics);
        window.setVisible(true);


        window.repaint();
    }

    static private class Example {
        private String value;

        public Example(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
