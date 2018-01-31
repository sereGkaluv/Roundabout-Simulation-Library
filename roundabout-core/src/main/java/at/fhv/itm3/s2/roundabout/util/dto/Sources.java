package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import java.util.List;

public class Sources implements IDTO {
    private List<Source> sourceList;

    public List<Source> getSource() {
        return sourceList;
    }

    public void setSource(List<Source> sourceList) {
        this.sourceList = sourceList;
    }
}
