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
    private RouteGeneratorMock routeGeneratorMock;

    private int remainingCarsToGenerate;
    private RouteType type;

    /**
     * Constructs a new {@link CarGenerateEventMock}.
     *
     * @param model       the model this event belongs to.
     * @param name        this event's name.
     * @param showInTrace flag to indicate if this event shall produce output for the trace.
     */
    public CarGenerateEventMock(
            Model model,
            String name,
            boolean showInTrace,
            int remainingCarsToGenerate,
            RouteGeneratorMock routeGeneratorMock,
            RouteType type
    ) {
        super(model, name, showInTrace);
        this.model = (RoundaboutSimulationModel)model;
        this.remainingCarsToGenerate = remainingCarsToGenerate;
        this.routeGeneratorMock = routeGeneratorMock;
        this.type = type;

        initMockingComponents();
    }

    public CarGenerateEventMock(Model model, String name, boolean showInTrace, RouteGeneratorMock routeGeneratorMock) {
        super(model, name, showInTrace);
        this.model = (RoundaboutSimulationModel)model;
        this.remainingCarsToGenerate = 1;
        this.routeGeneratorMock = routeGeneratorMock;
        this.type = RouteType.TWO_STREETSECTIONS_TWO_CARS;

        initMockingComponents();
    }

    private void initMockingComponents() {

        this.routeController = Mockito.mock(RouteController.class);

        when(this.routeController.getRandomRoute((AbstractSource)notNull())).then(invocationOnMock -> routeGeneratorMock.getRoute(this.type));

        this.roundaboutEventFactory = Mockito.mock(RoundaboutEventFactory.class);

        when(this.roundaboutEventFactory.createCarGenerateEvent((RoundaboutSimulationModel) notNull())).then(
                invocationOnMock -> new CarGenerateEventMock(
                        this.model,
                        "",
                        false,
                        this.remainingCarsToGenerate - 1,
                        this.routeGeneratorMock,
                        this.type
                )
        );

        when(this.roundaboutEventFactory.createCarCouldLeaveSectionEvent((RoundaboutSimulationModel) notNull())).then(
                invocationOnMock -> new CarCouldLeaveSectionEventMock(
                        this.model,
                        "",
                        false
                )
        );

    }

    public AbstractSource getSource() {
        return routeGeneratorMock.getRoute(this.type).getSource();
    }

    public IConsumer getSink() {
        return routeGeneratorMock.getRoute(this.type).getSink();
    }

    @Override
    public void eventRoutine(AbstractSource source) {
        if (remainingCarsToGenerate > 0) {
            super.eventRoutine(source);
        }
    }
}
