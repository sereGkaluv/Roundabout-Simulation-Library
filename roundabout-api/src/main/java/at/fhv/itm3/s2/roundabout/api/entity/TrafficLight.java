package at.fhv.itm3.s2.roundabout.api.entity;

public class TrafficLight {
    private boolean active;
    private boolean isFreeToGo;
    private boolean triggersByJam;
    private Long greenCircleDuration;
    private Long redCircleDuration;

    public TrafficLight(boolean active, Long redPhaseDuration) {
        this(active, true, null, redPhaseDuration);
    }

    public TrafficLight(
        boolean active,
        Long greenCircleDuration,
        Long redCircleDuration
    ) {
        this(active, true, greenCircleDuration, redCircleDuration);
    }

    public TrafficLight(
        boolean active,
        boolean isFreeToGo,
        Long greenCircleDuration,
        Long redCircleDuration
    ) {

        if(greenCircleDuration == null) this.triggersByJam = true;
        else this.triggersByJam = false;
        if(redCircleDuration == null) active = false;

        this.active = active;
        this.isFreeToGo = isFreeToGo;

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
