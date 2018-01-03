package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm14.trafsim.model.ModelFactory;
import at.fhv.itm14.trafsim.model.entities.*;
import at.fhv.itm14.trafsim.model.entities.intersection.FixedCirculationController;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.Sink;
import at.fhv.itm3.s2.roundabout.adapter.OneWayStreetAdapter;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutIntersection;
import at.fhv.itm3.s2.roundabout.entity.Route;
import at.fhv.itm3.s2.roundabout.entity.StreetConnector;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RouteGenerator {

    private Map<Integer, IRoute> routes;
    private RoundaboutSimulationModel model;

    public RouteGenerator(RoundaboutSimulationModel model) {
        routes = new HashMap<>();
        this.model = model;

        initializeRoutes();
        initializeRouteWithIntersection();
    }

    private IRoute getRouteWithTwoStreetSections() {
        return routes.get(0);
    }

    private IRoute getRouteWithOneWayStreetAndStreetSection() {
        return routes.get(1);
    }

    private IRoute getRouteWithOneStreetSectionAndOneOneWayStreet() { return routes.get(2); }

    private IRoute getRouteWithTwoOneWayStreets() { return routes.get(3); }

    private IRoute getRouteWithIntersectionAndTwoStreetSections() { return routes.get(4); }

    public IRoute getRoute(RouteType type) {
        switch (type) {
            case TWO_STREETSECTIONS:
                return getRouteWithTwoStreetSections();
            case ONE_ONEWAYSTREET_ONE_STREETSECTION:
                return getRouteWithOneWayStreetAndStreetSection();
            case ONE_STREETSECTION_ONE_ONEWAYSTREET:
                return getRouteWithOneStreetSectionAndOneOneWayStreet();
            case TWO_ONEWAYSTREETS:
                return getRouteWithTwoOneWayStreets();
            case STREETSECTION_INTERSECTION_STREETSECTION:
                return getRouteWithIntersectionAndTwoStreetSections();
        }
        return null;
    }

    private void initializeRoutes() {

        // INITIALIZE ROUTE WITH TWO STREETSECTIONS
        // initialize streets and sink
        Street street1_1 = new StreetSection(10.0, model, "", false);
        Street street1_2 = new StreetSection(10.0, model, "", false);
        Sink sink1 = new Sink(model, "", false);

        // initialize connectors
        Set<IProducer> prevStreetsForConnector1_1 = new HashSet<>();
        prevStreetsForConnector1_1.add(street1_1);

        Set<IConsumer> nextStreetsForConnector1_1 = new HashSet<>();
        nextStreetsForConnector1_1.add(street1_2);

        StreetConnector connector1_1 = new StreetConnector(prevStreetsForConnector1_1, nextStreetsForConnector1_1);
        street1_1.setNextStreetConnector(connector1_1);
        street1_2.setPreviousStreetConnector(connector1_1);

        Set<IProducer> prevStreetsForConnector1_2 = new HashSet<>();
        prevStreetsForConnector1_2.add(street1_2);

        Set<IConsumer> nextStreetsForConnector1_2 = new HashSet<>();
        nextStreetsForConnector1_2.add(sink1);

        StreetConnector connector1_2 = new StreetConnector(prevStreetsForConnector1_2, nextStreetsForConnector1_2);
        street1_2.setNextStreetConnector(connector1_2);
        sink1.setPreviousStreetConnector(connector1_2);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(model, "", false, street1_1, 2, this, RouteType.TWO_STREETSECTIONS);

        IRoute route1 = new Route();
        route1.addSource(source1);
        route1.addSection(street1_1);
        route1.addSection(street1_2);
        route1.addSection(sink1);

        routes.put(0, route1);


        // INITIALIZE ROUTE WITH ONEWAYSTREET AND STREETSECTION
        // initialize streets and sink
        Street street2_2 = new StreetSection(10.0, model, "", false);
        OneWayStreet oneWayStreet2_1 = new OneWayStreet(model, "", false, 2, street2_2.toConsumer(), 10);
        Street street2_1 = new OneWayStreetAdapter(oneWayStreet2_1, model, "", false);

        Sink sink2 = new Sink(model, "", false);

        // initialize connectors
        Set<IProducer> prevStreetsForConnector2_1 = new HashSet<>();
        prevStreetsForConnector2_1.add(street2_1);

        Set<IConsumer> nextStreetsForConnector2_1 = new HashSet<>();
        nextStreetsForConnector2_1.add(street2_2);

        StreetConnector connector2_1 = new StreetConnector(prevStreetsForConnector2_1, nextStreetsForConnector2_1);
        street2_1.setNextStreetConnector(connector2_1);
        street2_2.setPreviousStreetConnector(connector2_1);

        Set<IProducer> prevStreetsForConnector2_2 = new HashSet<>();
        prevStreetsForConnector2_2.add(street2_2);

        Set<IConsumer> nextStreetsForConnector2_2 = new HashSet<>();
        nextStreetsForConnector2_2.add(sink2);

        StreetConnector connector2_2 = new StreetConnector(prevStreetsForConnector2_2, nextStreetsForConnector2_2);
        street2_2.setNextStreetConnector(connector2_2);
        sink2.setPreviousStreetConnector(connector2_2);

        // initialize source and route
        AbstractSource source2 = new RoundaboutSourceMock(model, "", false, street2_1, 2, this, RouteType.ONE_ONEWAYSTREET_ONE_STREETSECTION);

        IRoute route2 = new Route();
        route2.addSource(source2);
        route2.addSection(street2_1);
        route2.addSection(street2_2);
        route2.addSection(sink2);

        routes.put(1, route2);


        // INITIALIZE ROUTE WITH STREETSECTION AND ONEWAYSTREET
        // initialize streets and sink
        Sink sink3 = new Sink(model, "", false);
        Street street3_1 = new StreetSection(10.0, model, "", false);
        OneWayStreet oneWayStreet3_2 = new OneWayStreet(model, "", false, 2, sink3.toConsumer(), 10);
        Street street3_2 = new OneWayStreetAdapter(oneWayStreet3_2, model, "", false);

        // initialize connectors
        Set<IProducer> prevStreetsForConnector3_1 = new HashSet<>();
        prevStreetsForConnector3_1.add(street3_1);

        Set<IConsumer> nextStreetsForConnector3_1 = new HashSet<>();
        nextStreetsForConnector3_1.add(street3_2);

        StreetConnector connector3_1 = new StreetConnector(prevStreetsForConnector3_1, nextStreetsForConnector3_1);
        street3_1.setNextStreetConnector(connector3_1);
        street3_2.setPreviousStreetConnector(connector3_1);

        Set<IProducer> prevStreetsForConnector3_2 = new HashSet<>();
        prevStreetsForConnector3_2.add(street3_2);

        Set<IConsumer> nextStreetsForConnector3_2 = new HashSet<>();
        nextStreetsForConnector3_2.add(sink3);

        StreetConnector connector3_2 = new StreetConnector(prevStreetsForConnector3_2, nextStreetsForConnector3_2);
        street3_2.setNextStreetConnector(connector3_2);
        sink3.setPreviousStreetConnector(connector3_2);

        // initialize source and route
        AbstractSource source3 = new RoundaboutSourceMock(model, "", false, street3_1, 2, this, RouteType.ONE_STREETSECTION_ONE_ONEWAYSTREET);

        IRoute route3 = new Route();
        route3.addSource(source3);
        route3.addSection(street3_1);
        route3.addSection(street3_2);
        route3.addSection(sink3);

        routes.put(2, route3);


        // INITIALIZE ROUTE WITH TWO ONEWAYSTREETS
        // initialize streets and sink
        Sink sink4 = new Sink(model, "", false);
        OneWayStreet oneWayStreet4_2 = new OneWayStreet(model, "", false, 2, sink4.toConsumer(), 10);
        Street street4_2 = new OneWayStreetAdapter(oneWayStreet4_2, model, "", false);
        OneWayStreet oneWayStreet4_1 = new OneWayStreet(model, "", false, 2, street4_2.toConsumer(), 10);
        Street street4_1 = new OneWayStreetAdapter(oneWayStreet4_1, model, "", false);

        // initialize connectors
        Set<IProducer> prevStreetsForConnector4_1 = new HashSet<>();
        prevStreetsForConnector4_1.add(street4_1);

        Set<IConsumer> nextStreetsForConnector4_1 = new HashSet<>();
        nextStreetsForConnector4_1.add(street4_2);

        StreetConnector connector4_1 = new StreetConnector(prevStreetsForConnector4_1, nextStreetsForConnector4_1);
        street4_1.setNextStreetConnector(connector4_1);
        street4_2.setPreviousStreetConnector(connector4_1);

        Set<IProducer> prevStreetsForConnector4_2 = new HashSet<>();
        prevStreetsForConnector4_2.add(street4_2);

        Set<IConsumer> nextStreetsForConnector4_2 = new HashSet<>();
        nextStreetsForConnector4_2.add(sink4);

        StreetConnector connector4_2 = new StreetConnector(prevStreetsForConnector4_2, nextStreetsForConnector4_2);
        street4_2.setNextStreetConnector(connector4_2);
        sink4.setPreviousStreetConnector(connector4_2);

        // initialize source and route
        AbstractSource source4 = new RoundaboutSourceMock(model, "", false, street4_1, 2, this, RouteType.TWO_ONEWAYSTREETS);

        IRoute route4 = new Route();
        route4.addSource(source4);
        route4.addSection(street4_1);
        route4.addSection(street4_2);
        route4.addSection(sink4);

        routes.put(3, route4);
    }

    private void initializeRouteWithIntersection() {
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

        // initialize sink
        Sink sink = new Sink(model, "", false);

        // initialize source
        AbstractSource source = new RoundaboutSourceMock(model, "", false, (StreetSection)street1, 2, this, RouteType.STREETSECTION_INTERSECTION_STREETSECTION);

        // connect streets with intersection
        IntersectionController.getInstance().setIntersectionInDirectionMapping(intersection, street1, inDirection);
        IntersectionController.getInstance().setIntersectionOutDirectionMapping(intersection, street2, outDirection);
//        setInDirection((StreetSectionMock)street1, inDirection);
//        setOutDirection((StreetSectionMock)street2, outDirection);
        intersection.attachProducer(inDirection, street1.toProducer());
        intersection.attachConsumer(outDirection, street2.toConsumer());
        intersection.createConnectionQueue(street1.toProducer(), new AbstractConsumer[]{street2.toConsumer()}, new double[]{intersectionTraverseTime}, new double[]{1.0});

        // initialize connectors
        Set<IProducer> prevStreetsForConnector1 = new HashSet<>();
        prevStreetsForConnector1.add(street2);

        Set<IConsumer> nextStreetsForConnector1 = new HashSet<>();
        nextStreetsForConnector1.add(sink);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        ((StreetSection)street2).setNextStreetConnector(connector1);
        sink.setPreviousStreetConnector(connector1);

        // initialize route
        IRoute route = new Route();
        route.addSource(source);
        route.addSection(street1);
        route.addSection(intersection);
        route.addSection(street2);
        route.addSection(sink);

        routes.put(4, route);
    }
}
