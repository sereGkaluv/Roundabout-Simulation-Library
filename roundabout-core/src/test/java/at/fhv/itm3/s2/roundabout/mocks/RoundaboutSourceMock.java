package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.RoundaboutSource;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.event.RoundaboutEventFactory;
import desmoj.core.simulator.Model;
import org.mockito.Mockito;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;

public class RoundaboutSourceMock extends RoundaboutSource {

    private RoundaboutSimulationModel model;
    private int remainingCarsToGenerate;
    private RouteGeneratorMock routeGeneratorMock;
    private RouteType type;

    public RoundaboutSourceMock(
            Model model,
            String description,
            boolean showInTrace,
            Street connectedStreet,
            int remainingCarsToGenerate,
            RouteGeneratorMock routeGeneratorMock,
            RouteType type
    ) {
        super(model, description, showInTrace, connectedStreet);
        this.model = (RoundaboutSimulationModel)model;
        this.remainingCarsToGenerate = remainingCarsToGenerate;
        this.routeGeneratorMock = routeGeneratorMock;
        this.type = type;

        initMockingComponents();
    }

    private void initMockingComponents() {

        this.roundaboutEventFactory = Mockito.mock(RoundaboutEventFactory.class);

        when(this.roundaboutEventFactory.createCarGenerateEvent((RoundaboutSimulationModel) notNull())).then(
                invocationOnMock -> new CarGenerateEventMock(
                        this.model,
                        "",
                        false,
                        this.remainingCarsToGenerate,
                        this.routeGeneratorMock,
                        this.type
                )
        );

    }
}
