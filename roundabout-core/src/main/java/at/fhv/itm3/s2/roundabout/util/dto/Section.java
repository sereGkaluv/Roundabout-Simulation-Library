package at.fhv.itm3.s2.roundabout.util.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class Section {
    private String length;
    private String trackId;

    @XmlAttribute
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @XmlAttribute
    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
}
