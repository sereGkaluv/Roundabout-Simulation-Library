package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import java.util.List;

public class Sections implements IDTO {
    private List<Section> sectionList;

    public List<Section> getSection() {
        return sectionList;
    }

    public void setSection(List<Section> sectionList) {
        this.sectionList = sectionList;
    }
}
