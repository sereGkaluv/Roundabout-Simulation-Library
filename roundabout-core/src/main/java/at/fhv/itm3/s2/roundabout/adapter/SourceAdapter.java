package at.fhv.itm3.s2.roundabout.adapter;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.Source;
import at.fhv.itm14.trafsim.model.events.CarDepartureEvent;
import at.fhv.itm14.trafsim.persistence.model.DTO;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import desmoj.core.simulator.Model;

public class SourceAdapter extends AbstractSource {

    private Source source;
    private Street connectedStreet;
    private RoundaboutSimulationModel model;

    public SourceAdapter(Model model, String modelDescription, boolean showInTrace, Street connectedStreet) {
        super(model, modelDescription, showInTrace);
        if (model instanceof RoundaboutSimulationModel) {
            this.model = (RoundaboutSimulationModel)model;
        } else {
            throw new IllegalArgumentException("No suitable model for SourceAdapter");
        }

        this.source = new Source(model, modelDescription, showInTrace, this.model.getTimeBetweenCarArrivalsOnOneWayStreets(), connectedStreet);
        this.connectedStreet = connectedStreet;
    }

    @Override
    public void startGeneratingCars() {
        this.source.start();
    }

    @Override
    public Street getConnectedStreet() {
        return this.connectedStreet;
    }

    @Override
    public void carDelivered(CarDepartureEvent carDepartureEvent, Car car, boolean b) {
        this.source.carDelivered(carDepartureEvent, car, b);
    }

    @Override
    public DTO toDTO() {
        return null;
    }
}
