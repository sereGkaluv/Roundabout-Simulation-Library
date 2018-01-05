package at.fhv.itm3.s2.roundabout.util.dto;

public class Roundabout {
    private String name;
    private Parameters parameters;
    private Sections sections;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Sections getSections() {
        return sections;
    }

    public void setSections(Sections sections) {
        this.sections = sections;
    }
}
