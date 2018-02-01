package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

public class Route implements IDTO {
    private Double ratio;
    private Source source;
    private Sink sink;
    private List<Section> sectionList;

    @XmlAttribute
    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Sink getSink() {
        return sink;
    }

    public void setSink(Sink sink) {
        this.sink = sink;
    }

    public List<Section> getSection() {
        return sectionList;
    }

    public void setSection(List<Section> sectionList) {
        this.sectionList = sectionList;
    }
}
