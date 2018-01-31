package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

public class Connector implements IDTO {
    private String id;
    private List<Track> trackList;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Track> getTrack() {
        return trackList;
    }

    public void setTrack(List<Track> trackList) {
        this.trackList = trackList;
    }
}
