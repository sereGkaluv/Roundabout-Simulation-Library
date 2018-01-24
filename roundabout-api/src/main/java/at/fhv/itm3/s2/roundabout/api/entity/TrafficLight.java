package at.fhv.itm3.s2.roundabout.api.entity;

public class TrafficLight {
    private boolean active;
    private boolean isFreeToGo;

    public TrafficLight(boolean active) {
        this.active = active;
        this.isFreeToGo = true;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isFreeToGo() {
        return isFreeToGo;
    }

    public void setFreeToGo(boolean isFreeToGo) {
        if (!this.isActive()) {
            throw new IllegalStateException("cannot set state of inactive traffic light");
        }

        this.isFreeToGo = isFreeToGo;
    }

}
