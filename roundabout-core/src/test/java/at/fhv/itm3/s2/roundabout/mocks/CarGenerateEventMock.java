package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.RouteController;
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
    private RouteType type;

    /**
     * Constructs a new {@link CarGenerateEventMock}.
     *
     * @param model       the model this event belongs to.
     * @param name        this event's name.
     * @param showInTrace flag to indicate if this event shall produce output for the trace.
     */
    public CarGenerateEventMock(Model model, String name, boolean showInTrace, int remainigCarsToGenerate, RouteGenerator routeGenerator, RouteType type) {
        super(model, name, showInTrace);
        this.model = (RoundaboutSimulationModel)model;
        this.remainingCarsToGenerate = remainigCarsToGenerate;
        this.routeGenerator = routeGenerator;
        this.type = type;

        initMockingComponents();
    }

    public CarGenerateEventMock(Model model, String name, boolean showInTrace, RouteGenerator routeGenerator) {
        super(model, name, showInTrace);
        this.model = (RoundaboutSimulationModel)model;
        this.remainingCarsToGenerate = 1;
        this.routeGenerator = routeGenerator;
        this.type = RouteType.TWO_STREETSECTIONS;

        initMockingComponents();
    }



    private void initMockingComponents() {

        this.routeController = Mockito.mock(RouteController.class);

        when(this.routeController.getRandomRoute()).then(invocationOnMock -> routeGenerator.getRoute(this.type));

        this.roundaboutEventFactory = Mockito.mock(RoundaboutEventFactory.class);

        when(this.roundaboutEventFactory.createCarGenerateEvent((RoundaboutSimulationModel)notNull())).then(invocationOnMock -> {
            return new CarGenerateEventMock(this.model, "", false, this.remainingCarsToGenerate - 1, this.routeGenerator, this.type);
        });

        when(this.roundaboutEventFactory.createCarCouldLeaveSectionEvent((RoundaboutSimulationModel)notNull())).then(invocationOnMock -> {
            return new CarCouldLeaveSectionEventMock(this.model, "", false);
        });

    }

    public AbstractSource getSource() {
        return routeGenerator.getRoute(this.type).getSource();
    }

    public IConsumer getSink() {
        return routeGenerator.getRoute(this.type).getSink();
    }

    @Override
    public void eventRoutine(Street section) {
        if (remainingCarsToGenerate > 0) {
            super.eventRoutine(section);
        }
    }
}
