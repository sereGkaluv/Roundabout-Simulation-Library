package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSink;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.mocks.RouteGeneratorMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;


public class CarReachedDestinationIntegration {

    private RoundaboutSimulationModel model;
    private Experiment exp;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false, 3.5, 10.0, 6.0, 2.0);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
    }

    @Test
    public void destinationReached() {
        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));
        ArgumentCaptor<ICar> varArgs = ArgumentCaptor.forClass(ICar.class);

        RoundaboutSink roundaboutSinkSpyMock = spy(new RoundaboutSink(model, "", false));
        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model, roundaboutSinkSpyMock);

        IRoute route = routeGeneratorMock.getRoute(RouteType.TWO_STREETSECTIONS_ONE_CAR);

        //when( roundaboutSinkMock.addCar(varArgs.capture())).thenReturn(true);
        // doAnswer(       ).when(roundaboutSinkMock).addCar(varArgs.capture());

        AbstractSource source = route.getSource();
        AbstractSink sink = route.getSink();
        IConsumer destination = route.getDestinationSection();

        source.startGeneratingCars(0.0);

        exp.start();
        exp.finish();

        verify(roundaboutSinkSpyMock, times(1)).addCar(varArgs.capture());

        if(!sink.isEmpty()){
            Assert.assertEquals("Car never reached destination.",destination, varArgs.getValue().getDestination());
        } else {
            Assert.fail("Car does not reach destination.");
        }
    }
}
