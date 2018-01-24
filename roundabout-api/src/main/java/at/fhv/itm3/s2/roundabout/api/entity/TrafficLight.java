package at.fhv.itm3.s2.roundabout.api.entity;

public class TrafficLight {
    boolean active;
    boolean isFreeToGo;

    public TrafficLight(boolean active) {
        this.active = active;
        this.isFreeToGo = false;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isFreeToGo() {
        return isFreeToGo;
    }

    public void setFreeToGo(boolean isFreeToGo) {
        this.isFreeToGo = isFreeToGo;
    }

}
