package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.events.CarDepartureEvent;
import at.fhv.itm14.trafsim.persistence.model.DTO;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.event.CarGenerateEvent;
import at.fhv.itm3.s2.roundabout.event.RoundaboutEventFactory;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class RoundaboutSource extends AbstractSource {

    private Street connectedStreet;
    private RoundaboutSimulationModel model;
    protected RoundaboutEventFactory roundaboutEventFactory;

    public RoundaboutSource(Model model, String description, boolean showInTrace, Street connectedStreet) {
        super(model, description, showInTrace);
        this.connectedStreet = connectedStreet;
        this.roundaboutEventFactory = RoundaboutEventFactory.getInstance();

        if (model instanceof RoundaboutSimulationModel) {
            this.model = (RoundaboutSimulationModel)model;
        } else {
            throw new IllegalArgumentException("No suitable model for RoundaboutSource");
        }
    }

    public void startGeneratingCars(double afterModelTimeUnits) {
        CarGenerateEvent event = this.roundaboutEventFactory.createCarGenerateEvent(model);
        event.schedule(connectedStreet, new TimeSpan(afterModelTimeUnits));
    }

    public Street getConnectedStreet() {
        return connectedStreet;
    }

    @Override
    public void carDelivered(CarDepartureEvent carDepartureEvent, Car car, boolean b) {
    }

    @Override
    public DTO toDTO() {
        return null;
    }
}
