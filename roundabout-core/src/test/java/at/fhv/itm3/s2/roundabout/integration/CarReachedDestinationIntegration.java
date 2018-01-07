package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.IProducer;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.*;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.Route;
import at.fhv.itm3.s2.roundabout.entity.StreetConnector;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.mocks.RoundaboutSourceMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteGeneratorMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class CarReachedDestinationIntegration {

    private RoundaboutSimulationModel model;
    private Experiment exp;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);

    }

    @Test
    public void destinationReached() {
        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));
        ArgumentCaptor<ICar> varArgs = ArgumentCaptor.forClass(ICar.class);

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);
        RoundaboutSink roundaboutSinkMock = spy(new RoundaboutSink(model, "", false));
        IRoute route = generateDestinationRoute(RouteType.TWO_STREETSECTIONS_ONE_CAR,
                roundaboutSinkMock, routeGeneratorMock, model);

        verify(roundaboutSinkMock, times(1)).addCar(varArgs.capture());

        AbstractSource source = route.getSource();
        AbstractSink sink = route.getSink();
        IConsumer destination = route.getDestinationSection();
        source.startGeneratingCars();

        exp.start();
        exp.finish();

        if(!sink.isEmpty()){
            Assert.assertEquals("car never reached destination.",
                    destination.equals(varArgs.getValue().getDestination()));
        } else {
            Assert.fail();
        }
    }

    private IRoute generateDestinationRoute(RouteType routeType,
                                            RoundaboutSink roundaboutSink,
                                            RouteGeneratorMock routeGeneratorMock,
                                            RoundaboutSimulationModel model) {
        // INITIALIZE ROUTE WITH TWO STREETSECTIONS
        // initialize streets and sink
        Street street1_1 = new StreetSection(10.0, model, "", false);
        Street street1_2 = new StreetSection(10.0, model, "", false);

        // initialize connectors
        List<IConsumer> prevStreetsForConnector1_1 = new LinkedList<>();
        prevStreetsForConnector1_1.add(street1_1);

        List<IConsumer> nextStreetsForConnector1_1 = new LinkedList<>();
        nextStreetsForConnector1_1.add(street1_2);

        StreetConnector connector1_1 = new StreetConnector(prevStreetsForConnector1_1, nextStreetsForConnector1_1);
        street1_1.setNextStreetConnector(connector1_1);
        street1_2.setPreviousStreetConnector(connector1_1);
        connector1_1.initializeTrack(street1_1, ConsumerType.STREET_SECTION, street1_2, ConsumerType.STREET_SECTION);

        List<IConsumer> prevStreetsForConnector1_2 = new LinkedList<>();
        prevStreetsForConnector1_2.add(street1_2);

        List<IConsumer> nextStreetsForConnector1_2 = new LinkedList<>();
        nextStreetsForConnector1_2.add(roundaboutSink);

        StreetConnector connector1_2 = new StreetConnector(prevStreetsForConnector1_2, nextStreetsForConnector1_2);
        street1_2.setNextStreetConnector(connector1_2);
        roundaboutSink.setPreviousStreetConnector(connector1_2);
        connector1_2.initializeTrack(street1_2, ConsumerType.STREET_SECTION, roundaboutSink, ConsumerType.STREET_SECTION);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(
            model,
            "",
            false,
            street1_1,
            routeType.getCarsToGenerate(),
            routeGeneratorMock,
            routeType
        );

        IRoute route = new Route();
        route.addSource(source1);
        route.addSection(street1_1);
        route.addSection(street1_2);
        route.addSection(roundaboutSink);

        return route;
    }
}
