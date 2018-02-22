package at.fhv.itm3.s2.roundabout.entity;

public class RouteSegmentsId {
    private String componentId;
    private String streetId;

    public RouteSegmentsId(String componentId, String streetId) {
        this.componentId = componentId;
        this.streetId = streetId;
    }

    public String getComponentId() {
        return componentId;
    }

    public String getStreetId() {
        return streetId;
    }

    public boolean contains(String componentId, String streetId) {
        return this.componentId.equals(componentId) && this.streetId.equals(streetId);
    }
}