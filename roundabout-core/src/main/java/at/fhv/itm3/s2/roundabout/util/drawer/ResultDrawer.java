package at.fhv.itm3.s2.roundabout.util.drawer;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ResultDrawer extends JFrame {
    private final LinkedList<Statistics> statistics;
    private final Image background;

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
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(background, 0, 0, this);

        int legendY = 40;
        for (Statistics stat : statistics) {
            g2.setColor(Color.decode(stat.getColor()));
            g2.drawString(stat.getTitle(), 20, legendY);
            legendY += 20;
            for (StatisticsValue val : stat.getValues()) {
                g2.drawString(val.getValue(), val.getX(), val.getY());
            }
        }

        g2.finalize();
    }

    public static void main(String[] args) {
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

        ResultDrawer window = new ResultDrawer("Kreisverkehr Dornbirn Nord", "/dornbirn-nord.png", statistics);
        window.setVisible(true);
    }
}
