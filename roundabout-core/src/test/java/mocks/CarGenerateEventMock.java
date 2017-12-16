package mocks;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.Sink;
import at.fhv.itm3.s2.roundabout.Source;
import at.fhv.itm3.s2.roundabout.adapter.Street;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.ISource;
import at.fhv.itm3.s2.roundabout.api.entity.IStreet;
import at.fhv.itm3.s2.roundabout.controller.RouteController;
import at.fhv.itm3.s2.roundabout.entity.Route;
import at.fhv.itm3.s2.roundabout.entity.StreetConnector;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.event.CarGenerateEvent;
import at.fhv.itm3.s2.roundabout.event.RoundaboutEventFactory;
import desmoj.core.simulator.Model;
import org.mockito.Mockito;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;

public class CarGenerateEventMock extends CarGenerateEvent {

    private RoundaboutSimulationModel model;
    private RouteGenerator routeGenerator;

    private int remainingCarsToGenerate;

    /**
     * Constructs a new {@link CarGenerateEventMock}.
     *
     * @param model       the model this event belongs to.
     * @param name        this event's name.
     * @param showInTrace flag to indicate if this event shall produce output for the trace.
     */
    public CarGenerateEventMock(Model model, String name, boolean showInTrace, int remainigCarsToGenerate, RouteGenerator routeGenerator) {
        super(model, name, showInTrace);
        this.model = (RoundaboutSimulationModel)model;
        this.remainingCarsToGenerate = remainigCarsToGenerate;
        this.routeGenerator = routeGenerator;

        initMockingComponents();
    }

    private void initMockingComponents() {

        this.routeController = Mockito.mock(RouteController.class);

        when(this.routeController.generateNewRoute()).then(invocationOnMock -> routeGenerator.getRouteByNumberOfStreets(2));

        this.roundaboutEventFactory = Mockito.mock(RoundaboutEventFactory.class);

        when(this.roundaboutEventFactory.createCarGenerateEvent((RoundaboutSimulationModel)notNull())).then(invocationOnMock -> {
            return new CarGenerateEventMock(this.model, "", false, this.remainingCarsToGenerate - 1, this.routeGenerator);
        });

        when(this.roundaboutEventFactory.createCarCouldLeaveSectionEvent((RoundaboutSimulationModel)notNull())).then(invocationOnMock -> {
            return new CarCouldLeaveSectionEventMock(this.model, "", false);
        });

    }

    public ISource getSource() {
        return routeGenerator.getRouteByNumberOfStreets(2).getSource();
    }

    public IStreet getSink() {
        return routeGenerator.getRouteByNumberOfStreets(2).getSink();
    }

    @Override
    public void eventRoutine(Street section) {
        if (remainingCarsToGenerate > 0) {
            super.eventRoutine(section);
        }
    }
}
