package at.fhv.itm3.s2.roundabout.api.entity;

public class TrafficLight {
    private boolean active;
    private boolean isFreeToGo;
    private boolean triggersByJam;
    private long greenCircleDuration;
    private long redCircleDuration;


    public TrafficLight(boolean active, long redPhaseDuration) {
        this.active = active;
        this.isFreeToGo = true;
        this.triggersByJam = true;
        this.greenCircleDuration = 0;
        this.redCircleDuration = redPhaseDuration;
    }

    public TrafficLight(boolean active,
                        long greenCircleDuration,
                        long redCircleDuration) {
        this.active = active;
        this.isFreeToGo = true;
        this.triggersByJam = false;
        this.greenCircleDuration = greenCircleDuration;
        this.redCircleDuration = redCircleDuration;
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

    public boolean isTriggersByJam() { return  triggersByJam; }
    public long getGreenCircleDuration() { return greenCircleDuration; }
    public long getRedCircleDuration() { return redCircleDuration; }

}
