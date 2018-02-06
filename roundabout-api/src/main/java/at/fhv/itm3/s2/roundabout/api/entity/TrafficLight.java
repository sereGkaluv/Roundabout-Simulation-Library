package at.fhv.itm3.s2.roundabout.api.entity;

public class TrafficLight {
    private Boolean active;
    private boolean isFreeToGo;
    private boolean triggersByJam;
    private Long minGreenPhaseDuration;
    private Long greenPhaseDuration;
    private Long redPhaseDuration;

    public TrafficLight(boolean active, boolean isTrafficJam, Long minGreenPhaseDuration, Long redPhaseDuration) {
        this(active, true, minGreenPhaseDuration, null, redPhaseDuration);
    }

    public TrafficLight(
        boolean active,
        Long greenPhaseDuration,
        Long redPhaseDuration
    ) {
        this(active, true, null, greenPhaseDuration, redPhaseDuration);
    }

    public TrafficLight(
        boolean active,
        Long minGreenPhaseDuration,
        Long greenPhaseDuration,
        Long redPhaseDuration
    ) {
        this(active, true, minGreenPhaseDuration, greenPhaseDuration, redPhaseDuration);
    }


    public TrafficLight(
        Boolean active,
        boolean isFreeToGo,
        Long minGreenPhaseDuration,
        Long greenPhaseDuration,
        Long redPhaseDuration
    ) {

        this.active = active;
        this.isFreeToGo = isFreeToGo;

        if(greenPhaseDuration == null && active != null) {
            this.triggersByJam = true;
            if(minGreenPhaseDuration == null)
                throw new IllegalArgumentException("MinGreenPhaseDuration must not be null.");
        }
        else this.triggersByJam = false;
        if(redPhaseDuration == null) active = false;

        this.minGreenPhaseDuration = minGreenPhaseDuration;
        this.greenPhaseDuration = greenPhaseDuration;
        this.redPhaseDuration = redPhaseDuration;
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
    public long getGreenPhaseDuration() { return greenPhaseDuration; }
    public long getRedPhaseDuration() { return redPhaseDuration; }
    public double getMinGreenPhaseDuration() { return  minGreenPhaseDuration; }

}
