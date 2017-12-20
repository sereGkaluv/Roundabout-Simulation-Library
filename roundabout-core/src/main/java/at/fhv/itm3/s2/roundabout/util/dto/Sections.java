package at.fhv.itm3.s2.roundabout.util.dto;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

public class Sections {
    private String id;
    private String previous;
    private String next;
    private List<In> in;
    private Out out;
    private List<Section> section;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    @XmlAttribute
    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<In> getIn() {
        return in;
    }

    public void setIn(List<In> in) {
        this.in = in;
    }

    public Out getOut() {
        return out;
    }

    public void setOut(Out out) {
        this.out = out;
    }

    public List<Section> getSection() {
        return section;
    }

    public void setSection(List<Section> section) {
        this.section = section;
    }
}
