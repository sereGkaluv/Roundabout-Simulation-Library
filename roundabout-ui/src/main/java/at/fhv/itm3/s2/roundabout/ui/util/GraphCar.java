package at.fhv.itm3.s2.roundabout.ui.util;

import javafx.scene.shape.Circle;

public class GraphCar<T> {
    private final Circle circle;
    private Link<T> currentLink;

    public GraphCar(Circle circle, Link<T> initialLink) {
        this.circle = circle;
        this.currentLink = initialLink;
    }

    public Circle getCircle() {
        return circle;
    }

    public Link<T> getCurrentLink() {
        return currentLink;
    }

    public void setCurrentLink(Link<T> currentLink) {
        this.currentLink = currentLink;
    }
}
