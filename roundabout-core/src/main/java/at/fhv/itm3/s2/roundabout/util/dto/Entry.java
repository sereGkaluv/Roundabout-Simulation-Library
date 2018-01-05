package at.fhv.itm3.s2.roundabout.util.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class Entry {
    private String probability;
    private String trackId;
    private String connectorId;
    private String length;

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

    @XmlAttribute
    public String getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
    }

    @XmlAttribute
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
