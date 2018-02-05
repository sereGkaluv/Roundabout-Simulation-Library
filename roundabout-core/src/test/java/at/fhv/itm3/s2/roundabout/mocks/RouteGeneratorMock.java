package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm14.trafsim.model.ModelFactory;
import at.fhv.itm14.trafsim.model.entities.AbstractConsumer;
import at.fhv.itm14.trafsim.model.entities.AbstractProSumer;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.FixedCirculationController;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.entity.*;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static at.fhv.itm3.s2.roundabout.mocks.RouteType.*;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.when;

public class RouteGeneratorMock {

    private Map<RouteType, IRoute> routes;
    private RoundaboutSimulationModel model;

    public RouteGeneratorMock(RoundaboutSimulationModel model) {
        routes = new HashMap<>();
        this.model = model;

        initializeRouteWithTwoStreetSections(TWO_STREETSECTIONS_ONE_CAR);
        initializeRouteWithTwoStreetSections(TWO_STREETSECTIONS_TWO_CARS);
        initializeRouteWithIntersection(STREETSECTION_INTERSECTION_STREETSECTION_ONE_CAR);
        initializeRouteWithIntersection(STREETSECTION_INTERSECTION_STREETSECTION_ONE_CAR);
        initializeRouteWithIntersection(STREETSECTION_INTERSECTION_STREETSECTION_TWO_CARS);
        initializeRouteWithTwoStreetSectionsAndOneStreetSectionMock(TWO_STREETSECTIONS_ONE_STREETSECTIONMOCK_TWO_CARS);
        initializeRouteWithIntersectionAndStreetSectionMock(STREETSECTION_INTERSECTION_STREETSECTIONMOCK_TEN_CARS);
        initializeRouteWithTwoTracksAndTwoStreetSectionsPerTrack();
        initializeRouteWithTwoTracksAndTwoStreetSectionsPerTrackForStopCounting();
    }

    public RouteGeneratorMock(RoundaboutSimulationModel model, RoundaboutSink roundaboutSinkSpyMock) {
        routes = new HashMap<>();
        this.model = model;

        initializeRouteWithTwoStreetSections(TWO_STREETSECTIONS_ONE_CAR, roundaboutSinkSpyMock);
        initializeRouteWithTwoStreetSections(TWO_STREETSECTIONS_TWO_CARS, roundaboutSinkSpyMock);
        initializeRouteWithTwoTracksAndTwoStreetSectionsPerTrack();
    }

    public IRoute getRoute(RouteType type) {
        return routes.getOrDefault(type, null);
    }

    private void initializeRouteWithTwoStreetSections(RouteType routeType) {

        // INITIALIZE ROUTE WITH TWO STREETSECTIONS
        // initialize streets and sink
        Street street1_1 = new StreetSection(20.0, model, "", false, true);
        Street street1_2 = new StreetSection(20.0, model, "", false);
        RoundaboutSink roundaboutSink1 = new RoundaboutSink(model, "", false);

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
        nextStreetsForConnector1_2.add(roundaboutSink1);

        StreetConnector connector1_2 = new StreetConnector(prevStreetsForConnector1_2, nextStreetsForConnector1_2);
        street1_2.setNextStreetConnector(connector1_2);
        roundaboutSink1.setPreviousStreetConnector(connector1_2);
        connector1_2.initializeTrack(street1_2, ConsumerType.STREET_SECTION, roundaboutSink1, ConsumerType.STREET_SECTION);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(
                model,
                "",
                false,
                street1_1,
                routeType.getCarsToGenerate(),
                this,
                routeType
        );

        IRoute route = new Route();
        route.setSource(source1);
        route.addSection(street1_1);
        route.addSection(street1_2);
        route.addSection(roundaboutSink1);

        routes.put(routeType, route);
    }

    private void initializeRouteWithTwoStreetSections(RouteType routeType, RoundaboutSink roundaboutSinkSpyMock) {

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
        nextStreetsForConnector1_2.add(roundaboutSinkSpyMock);

        StreetConnector connector1_2 = new StreetConnector(prevStreetsForConnector1_2, nextStreetsForConnector1_2);
        street1_2.setNextStreetConnector(connector1_2);
        roundaboutSinkSpyMock.setPreviousStreetConnector(connector1_2);
        connector1_2.initializeTrack(street1_2, ConsumerType.STREET_SECTION, roundaboutSinkSpyMock, ConsumerType.STREET_SECTION);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(
                model,
                "",
                false,
                street1_1,
                routeType.getCarsToGenerate(),
                this,
                routeType
        );

        IRoute route = new Route();
        route.setSource(source1);
        route.addSection(street1_1);
        route.addSection(street1_2);
        route.addSection(roundaboutSinkSpyMock);

        routes.put(routeType, route);
    }

    private void initializeRouteWithIntersection(RouteType routeType) {
        float turnaroundTime = 60;
        float[] phaseShiftTimes = new float[]{0.0F, 10.0F, 20.0F};
        double intersectionTraverseTime = 5.0;
        double accelerationTime = 2.0;
        double yellowDuration = turnaroundTime / 8;
        double greenDuration = turnaroundTime / 2 - yellowDuration;
        int outDirection = 1;
        int inDirection = 0;

        // initialize intersection
        RoundaboutIntersection intersection = new RoundaboutIntersection(model, "", false, 2);
        intersection.setServiceDelay(accelerationTime);
        FixedCirculationController ic = ModelFactory.getInstance(model).createOneWayController(
                intersection,
                greenDuration,
                yellowDuration,
                phaseShiftTimes[0]
        );
        intersection.attachController(ic);

        // initialize streets
        AbstractProSumer street1 = new StreetSection(10.0, model, "", false);
        AbstractProSumer street2 = new StreetSection(10.0, model, "", false);

        // initialize roundaboutSink
        RoundaboutSink roundaboutSink = new RoundaboutSink(model, "", false);

        // initialize source
        AbstractSource source = new RoundaboutSourceMock(
                model,
                "",
                false,
                (StreetSection) street1,
                routeType.getCarsToGenerate(),
                this,
                routeType
        );

        // connect streets with intersection
        IntersectionController.getInstance().setIntersectionInDirectionMapping(intersection, street1, inDirection);
        IntersectionController.getInstance().setIntersectionOutDirectionMapping(intersection, street2, outDirection);
        intersection.attachProducer(inDirection, street1.toProducer());
        intersection.attachConsumer(outDirection, street2.toConsumer());
        intersection.createConnectionQueue(
                street1.toProducer(),
                new AbstractConsumer[]{street2.toConsumer()},
                new double[]{intersectionTraverseTime},
                new double[]{1.0}
        );

        // initialize connectors
        List<IConsumer> prevStreetsForConnector1 = new LinkedList<>();
        prevStreetsForConnector1.add(street1);

        List<IConsumer> nextStreetsForConnector1 = new LinkedList<>();
        nextStreetsForConnector1.add(intersection);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        ((StreetSection)street1).setNextStreetConnector(connector1);
        connector1.initializeTrack(street1, ConsumerType.STREET_SECTION, intersection, ConsumerType.INTERSECTION);

        List<IConsumer> prevStreetsForConnector2 = new LinkedList<>();
        prevStreetsForConnector2.add(street2);

        List<IConsumer> nextStreetsForConnector2 = new LinkedList<>();
        nextStreetsForConnector2.add(roundaboutSink);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        ((StreetSection)street2).setNextStreetConnector(connector2);
        roundaboutSink.setPreviousStreetConnector(connector2);
        connector2.initializeTrack(street2, ConsumerType.STREET_SECTION, roundaboutSink, ConsumerType.STREET_SECTION);

        // initialize route
        IRoute route = new Route();
        route.setSource(source);
        route.addSection(street1);
        route.addSection(intersection);
        route.addSection(street2);
        route.addSection(roundaboutSink);

        routes.put(routeType, route);
    }

    private void initializeRouteWithTwoTracksAndTwoStreetSectionsPerTrack() {

        // initialize streets and sink
        Street street1_1 = new StreetSection(100.0, model, "", false);
        Street street1_2 = new StreetSection(100.0, model, "", false);
        RoundaboutSink sink1 = new RoundaboutSinkMock(model, "", false);

        Street street2_1 = new StreetSection(100.0, model, "", false);
        Street street2_2 = new StreetSection(100.0, model, "", false);
        RoundaboutSink sink2 = new RoundaboutSinkMock(model, "", false);

        // initialize connectors
        List<IConsumer> prevStreetsForConnector1 = new LinkedList<>();
        prevStreetsForConnector1.add(street1_1);
        prevStreetsForConnector1.add(street2_1);

        List<IConsumer> nextStreetsForConnector1 = new LinkedList<>();
        nextStreetsForConnector1.add(street1_2);
        nextStreetsForConnector1.add(street2_2);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        street1_1.setNextStreetConnector(connector1);
        street1_2.setPreviousStreetConnector(connector1);
        street2_1.setNextStreetConnector(connector1);
        street2_2.setPreviousStreetConnector(connector1);
        connector1.initializeTrack(street1_1, ConsumerType.STREET_SECTION, street1_2, ConsumerType.STREET_SECTION);
        connector1.initializeTrack(street2_1, ConsumerType.STREET_SECTION, street2_2, ConsumerType.STREET_SECTION);

        List<IConsumer> prevStreetsForConnector2 = new LinkedList<>();
        prevStreetsForConnector2.add(street1_2);
        prevStreetsForConnector2.add(street2_2);

        List<IConsumer> nextStreetsForConnector2 = new LinkedList<>();
        nextStreetsForConnector2.add(sink1);
        nextStreetsForConnector2.add(sink2);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        street1_2.setNextStreetConnector(connector2);
        sink1.setPreviousStreetConnector(connector2);
        street2_2.setNextStreetConnector(connector2);
        sink2.setPreviousStreetConnector(connector2);
        connector2.initializeTrack(street1_2, ConsumerType.STREET_SECTION, sink1, ConsumerType.STREET_SECTION);
        connector2.initializeTrack(street2_2, ConsumerType.STREET_SECTION, sink2, ConsumerType.STREET_SECTION);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(model, "", false, street1_1, 1, this, RouteType.ONE_CAR_STAYS_ON_TRACK);
        AbstractSource source2 = new RoundaboutSourceMock(model, "", false, street2_1, 1, this, RouteType.ONE_CAR_CHANGES_TRACK);

        IRoute route1 = new Route();
        route1.setSource(source1);
        route1.addSection(street1_1);
        route1.addSection(street1_2);
        route1.addSection(sink1);

        IRoute route2 = new Route();
        route2.setSource(source2);
        route2.addSection(street2_1);
        route2.addSection(street1_2);
        route2.addSection(sink1);

        routes.put(ONE_CAR_STAYS_ON_TRACK, route1);
        routes.put(ONE_CAR_CHANGES_TRACK, route2);
    }

    private void initializeRouteWithTwoStreetSectionsAndOneStreetSectionMock(RouteType routeType) {

        // INITIALIZE ROUTE WITH TWO STREETSECTIONS
        // initialize streets and sink
        Street street1 = new StreetSection(10.0, model, "", false);
        Street street2 = new StreetSection(10.0, model, "", false);
        Street street3 = Mockito.mock(StreetSection.class);
        RoundaboutSink roundaboutSink = new RoundaboutSink(model, "", false);

        when(street3.isEnoughSpace(anyDouble())).thenReturn(false);

        // initialize connectors
        List<IConsumer> prevStreetsForConnector1 = new LinkedList<>();
        prevStreetsForConnector1.add(street1);

        List<IConsumer> nextStreetsForConnector1 = new LinkedList<>();
        nextStreetsForConnector1.add(street2);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        street1.setNextStreetConnector(connector1);
        street2.setPreviousStreetConnector(connector1);
        connector1.initializeTrack(street1, ConsumerType.STREET_SECTION, street2, ConsumerType.STREET_SECTION);

        List<IConsumer> prevStreetsForConnector2 = new LinkedList<>();
        prevStreetsForConnector2.add(street2);

        List<IConsumer> nextStreetsForConnector2 = new LinkedList<>();
        nextStreetsForConnector2.add(street3);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        street2.setNextStreetConnector(connector2);
        street3.setPreviousStreetConnector(connector2);
        connector2.initializeTrack(street2, ConsumerType.STREET_SECTION, street3, ConsumerType.STREET_SECTION);

        List<IConsumer> prevStreetsForConnector3 = new LinkedList<>();
        prevStreetsForConnector3.add(street3);

        List<IConsumer> nextStreetsForConnector3 = new LinkedList<>();
        nextStreetsForConnector3.add(roundaboutSink);

        StreetConnector connector3 = new StreetConnector(prevStreetsForConnector3, nextStreetsForConnector3);
        street3.setNextStreetConnector(connector3);
        roundaboutSink.setPreviousStreetConnector(connector3);
        connector3.initializeTrack(street3, ConsumerType.STREET_SECTION, roundaboutSink, ConsumerType.STREET_SECTION);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(
                model,
                "",
                false,
                street1,
                routeType.getCarsToGenerate(),
                this,
                routeType
        );

        IRoute route = new Route();
        route.setSource(source1);
        route.addSection(street1);
        route.addSection(street2);
        route.addSection(street3);
        route.addSection(roundaboutSink);

        routes.put(routeType, route);
    }

    private void initializeRouteWithIntersectionAndStreetSectionMock(RouteType routeType) {
        float turnaroundTime = 60;
        float[] phaseShiftTimes = new float[]{0.0F, 10.0F, 20.0F};
        double intersectionTraverseTime = 5.0;
        double accelerationTime = 2.0;
        double yellowDuration = turnaroundTime / 8;
        double greenDuration = turnaroundTime / 2 - yellowDuration;
        int outDirection = 1;
        int inDirection = 0;

        // initialize intersection
        RoundaboutIntersection intersection = new RoundaboutIntersection(model, "", false, 2);
        intersection.setServiceDelay(accelerationTime);
        FixedCirculationController ic = ModelFactory.getInstance(model).createOneWayController(
                intersection,
                greenDuration,
                yellowDuration,
                phaseShiftTimes[0]
        );
        intersection.attachController(ic);

        // initialize streets
        AbstractProSumer street1 = new StreetSection(10.0, model, "", false);
        AbstractProSumer street2 = new StreetSection(10.0, model, "", false);
        AbstractProSumer street3 = Mockito.mock(StreetSection.class);

        when(((StreetSection)street3).isEnoughSpace(anyDouble())).thenReturn(false);

        // initialize roundaboutSink
        RoundaboutSink roundaboutSink = new RoundaboutSink(model, "", false);

        // initialize source
        AbstractSource source = new RoundaboutSourceMock(
                model,
                "",
                false,
                (StreetSection) street1,
                routeType.getCarsToGenerate(),
                this,
                routeType
        );

        // connect streets with intersection
        IntersectionController.getInstance().setIntersectionInDirectionMapping(intersection, street1, inDirection);
        IntersectionController.getInstance().setIntersectionOutDirectionMapping(intersection, street2, outDirection);
        intersection.attachProducer(inDirection, street1.toProducer());
        intersection.attachConsumer(outDirection, street2.toConsumer());
        intersection.createConnectionQueue(
                street1.toProducer(),
                new AbstractConsumer[]{street2.toConsumer()},
                new double[]{intersectionTraverseTime},
                new double[]{1.0}
        );

        // initialize connectors
        List<IConsumer> prevStreetsForConnector1 = new LinkedList<>();
        prevStreetsForConnector1.add(street1);

        List<IConsumer> nextStreetsForConnector1 = new LinkedList<>();
        nextStreetsForConnector1.add(intersection);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        ((StreetSection)street1).setNextStreetConnector(connector1);
        connector1.initializeTrack(street1, ConsumerType.STREET_SECTION, intersection, ConsumerType.INTERSECTION);

        List<IConsumer> prevStreetsForConnector2 = new LinkedList<>();
        prevStreetsForConnector2.add(street2);

        List<IConsumer> nextStreetsForConnector2 = new LinkedList<>();
        nextStreetsForConnector2.add(street3);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        ((StreetSection)street2).setNextStreetConnector(connector2);
        ((StreetSection)street3).setPreviousStreetConnector(connector2);
        connector2.initializeTrack(street2, ConsumerType.STREET_SECTION, street3, ConsumerType.STREET_SECTION);

        List<IConsumer> prevStreetsForConnector3 = new LinkedList<>();
        prevStreetsForConnector3.add(street3);

        List<IConsumer> nextStreetsForConnector3 = new LinkedList<>();
        nextStreetsForConnector3.add(roundaboutSink);

        StreetConnector connector3 = new StreetConnector(prevStreetsForConnector3, nextStreetsForConnector3);
        ((StreetSection)street3).setNextStreetConnector(connector3);
        roundaboutSink.setPreviousStreetConnector(connector3);
        connector3.initializeTrack(street3, ConsumerType.STREET_SECTION, roundaboutSink, ConsumerType.STREET_SECTION);

        // initialize route
        IRoute route = new Route();
        route.setSource(source);
        route.addSection(street1);
        route.addSection(intersection);
        route.addSection(street2);
        route.addSection(street3);
        route.addSection(roundaboutSink);

        routes.put(routeType, route);
    }

    private void initializeRouteWithTwoTracksAndTwoStreetSectionsPerTrackForStopCounting() {

        // initialize streets and sink
        Street street1_1 = new StreetSection(200.0, model, "", false);
        Street street1_2 = new StreetSection(200.0, model, "", false);
        RoundaboutSink sink1 = new RoundaboutSinkMock(model, "", false);

        Street street2_1 = new StreetSection(200.0, model, "", false);
        Street street2_2 = new StreetSection(200.0, model, "", false);
        RoundaboutSink sink2 = new RoundaboutSinkMock(model, "", false);

        // initialize connectors
        List<IConsumer> prevStreetsForConnector1 = new LinkedList<>();
        prevStreetsForConnector1.add(street1_1);
        prevStreetsForConnector1.add(street2_1);

        List<IConsumer> nextStreetsForConnector1 = new LinkedList<>();
        nextStreetsForConnector1.add(street1_2);
        nextStreetsForConnector1.add(street2_2);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        street1_1.setNextStreetConnector(connector1);
        street1_2.setPreviousStreetConnector(connector1);
        street2_1.setNextStreetConnector(connector1);
        street2_2.setPreviousStreetConnector(connector1);
        connector1.initializeTrack(street1_1, ConsumerType.STREET_SECTION, street1_2, ConsumerType.STREET_SECTION);
        connector1.initializeTrack(street2_1, ConsumerType.STREET_SECTION, street2_2, ConsumerType.STREET_SECTION);

        List<IConsumer> prevStreetsForConnector2 = new LinkedList<>();
        prevStreetsForConnector2.add(street1_2);
        prevStreetsForConnector2.add(street2_2);

        List<IConsumer> nextStreetsForConnector2 = new LinkedList<>();
        nextStreetsForConnector2.add(sink1);
        nextStreetsForConnector2.add(sink2);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        street1_2.setNextStreetConnector(connector2);
        sink1.setPreviousStreetConnector(connector2);
        street2_2.setNextStreetConnector(connector2);
        sink2.setPreviousStreetConnector(connector2);
        connector2.initializeTrack(street1_2, ConsumerType.STREET_SECTION, sink1, ConsumerType.STREET_SECTION);
        connector2.initializeTrack(street2_2, ConsumerType.STREET_SECTION, sink2, ConsumerType.STREET_SECTION);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(model, "", false, street2_1, 4, this, RouteType.STOPS_FOUR_CARS_CHANGE_TRACK);
        AbstractSource source2 = new RoundaboutSourceMock(model, "", false, street1_1, 4, this, RouteType.STOPS_FOUR_CARS_STAY_ON_TRACK);

        IRoute route1 = new Route();
        route1.setSource(source1);
        route1.addSection(street2_1);
        route1.addSection(street1_2);
        route1.addSection(sink1);

        IRoute route2 = new Route();
        route2.setSource(source2);
        route2.addSection(street1_1);
        route2.addSection(street1_2);
        route2.addSection(sink1);

        routes.put(STOPS_FOUR_CARS_CHANGE_TRACK, route1);
        routes.put(STOPS_FOUR_CARS_STAY_ON_TRACK, route2);
    }
}
