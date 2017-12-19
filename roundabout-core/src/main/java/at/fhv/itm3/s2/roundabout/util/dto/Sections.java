package at.fhv.itm3.s2.roundabout.util.dto;

import java.util.List;

public class Sections {
    private In in;
    private Out out;
    private List<Section> sections;

    public In getIn() {
        return in;
    }

    public void setIn(In in) {
        this.in = in;
    }

    public Out getOut() {
        return out;
    }

    public void setOut(Out out) {
        this.out = out;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
