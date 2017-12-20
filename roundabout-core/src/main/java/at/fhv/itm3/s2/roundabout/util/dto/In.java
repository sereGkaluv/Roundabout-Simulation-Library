package at.fhv.itm3.s2.roundabout.util.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class In {
    private String probability;
    private String trackId;

    @XmlAttribute
    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    @XmlAttribute
    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
}
