package at.fhv.itm3.s2.roundabout;

import at.fhv.itm3.s2.roundabout.api.entity.ISource;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.event.CarGenerateEvent;

public class Source implements ISource {

    private Street connectedStreet;
    private RoundaboutSimulationModel model;

    public Source(Street connectedStreet, RoundaboutSimulationModel model) {
        this.connectedStreet = connectedStreet;
        this.model = model;
    }

    public void startGeneratingCars() {
        new CarGenerateEvent(model, "CarGenerateEvent", false).schedule(connectedStreet);
    }

    public Street getConnectedStreet() {
        return connectedStreet;
    }
}
