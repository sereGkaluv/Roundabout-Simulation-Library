package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.events.CarDepartureEvent;
import at.fhv.itm14.trafsim.persistence.model.DTO;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.event.CarGenerateEvent;
import at.fhv.itm3.s2.roundabout.event.RoundaboutEventFactory;
import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.UUID;

public class RoundaboutSource extends AbstractSource {

    private Street connectedStreet;
    private RoundaboutSimulationModel model;
    protected RoundaboutEventFactory roundaboutEventFactory;
    private double generateRatio;

    public RoundaboutSource(Model model, String description, boolean showInTrace, Street connectedStreet) {
        this(UUID.randomUUID().toString(), null, model, description, showInTrace, connectedStreet);
    }

    public RoundaboutSource(String id, Double generatorExpectation, Model model, String description, boolean showInTrace, Street connectedStreet) {
        super(id, generatorExpectation, model, description, showInTrace);
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
        event.schedule(this, new TimeSpan(afterModelTimeUnits));
    }

    public Street getConnectedStreet() {
        return connectedStreet;
    }

    public double getGenerateRatio() {
        return Double.compare(generateRatio, 0.0) == 0 ? 1.0 : generateRatio;
    }

    public void addGenerateRatio(Double ratio) {
        this.generateRatio += ratio;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void carDelivered(CarDepartureEvent carDepartureEvent, Car car, boolean b) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DTO toDTO() {
        return null;
    }
}
