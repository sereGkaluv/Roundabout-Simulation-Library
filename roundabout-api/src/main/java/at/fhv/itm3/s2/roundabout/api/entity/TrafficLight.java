package at.fhv.itm3.s2.roundabout.api.entity;

public class TrafficLight {
    private Boolean active;
    private Boolean isFreeToGo;
    private Boolean triggersByJam;
    private Long minGreenPhaseDuration;
    private Long greenPhaseDuration;
    private Long redPhaseDuration;

    public TrafficLight(Boolean active, Boolean isTrafficJam, Long minGreenPhaseDuration, Long redPhaseDuration) {
        this(active, true, minGreenPhaseDuration, null, redPhaseDuration);
    }

    public TrafficLight(
            Boolean active,
            Long greenPhaseDuration,
            Long redPhaseDuration
    ) {
        this(active, true, null, greenPhaseDuration, redPhaseDuration);
    }

    public TrafficLight(
            Boolean active,
            Long minGreenPhaseDuration,
            Long greenPhaseDuration,
            Long redPhaseDuration
    ) {
        this(active, true, minGreenPhaseDuration, greenPhaseDuration, redPhaseDuration);
    }


    public TrafficLight(
            Boolean active,
            Boolean isFreeToGo,
            Long minGreenPhaseDuration,
            Long greenPhaseDuration,
            Long redPhaseDuration
    ) {

        this.active = active;
        this.isFreeToGo = isFreeToGo;

        if (greenPhaseDuration == null && active) {
            this.triggersByJam = true;
        } else {
            this.triggersByJam = false;
        }

        if (redPhaseDuration == null) {
            active = false;
        }

        this.minGreenPhaseDuration = minGreenPhaseDuration;
        this.greenPhaseDuration = greenPhaseDuration;
        this.redPhaseDuration = redPhaseDuration;
    }

    public boolean isActive() {
        return active != null ? active : false;
    }

    public boolean isFreeToGo() {
        return isFreeToGo != null ? isFreeToGo : true;
    }

    public void setFreeToGo(boolean isFreeToGo) {
        if (!this.isActive()) {
            throw new IllegalStateException("cannot set state of inactive traffic light");
        }

        this.isFreeToGo = isFreeToGo;
    }

    public boolean isTriggersByJam() {
        return triggersByJam != null ? triggersByJam : false;
    }

    public long getGreenPhaseDuration() {
        return greenPhaseDuration != null ? greenPhaseDuration : 0;
    }

    public long getRedPhaseDuration() {
        return redPhaseDuration != null ? redPhaseDuration : 0;
    }

    public double getMinGreenPhaseDuration() {
        return minGreenPhaseDuration != null ? minGreenPhaseDuration : 0;
    }
}
