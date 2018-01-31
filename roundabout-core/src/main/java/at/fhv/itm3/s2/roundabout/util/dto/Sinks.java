package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import java.util.List;

public class Sinks implements IDTO {
    private List<Sink> sinkList;

    public List<Sink> getSink() {
        return sinkList;
    }

    public void setSink(List<Sink> sinkList) {
        this.sinkList = sinkList;
    }
}
