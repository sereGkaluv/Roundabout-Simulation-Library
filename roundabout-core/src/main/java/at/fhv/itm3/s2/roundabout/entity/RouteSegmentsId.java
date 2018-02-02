package at.fhv.itm3.s2.roundabout.entity;

public class RouteSegmentsId {
    private String componentId;
    private String streetId;

    public RouteSegmentsId(String componentId, String streetId) {
        this.componentId = componentId;
        this.streetId = streetId;
    }

    public String getComponentId() {return this.componentId;}
    public String getStreetId() {return this.streetId;}

    public boolean contains(String componentId, String streetId) {
        if(this.componentId.equals(componentId) && this.streetId.equals(streetId)) return true;
        return false;
    }
}