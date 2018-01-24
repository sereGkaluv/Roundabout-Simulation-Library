package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;
import at.fhv.itm3.s2.roundabout.entity.*;

import java.util.LinkedList;
import java.util.List;

public class Test {

    private static void test() {
        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false);
        RoundaboutStructure structure = new RoundaboutStructure(model);

        StreetSection streetSection1_t1 = new StreetSection(10, model, "s1_t1", false);
        StreetSection streetSection1_t2 = new StreetSection(10, model, "s1_t2", false);

        StreetSection streetSection2_t1 = new StreetSection(10, model, "s2_t1", false);
        StreetSection streetSection2_t2 = new StreetSection(10, model, "s2_t2", false);

        StreetSection streetSectionO1_t1 = new StreetSection(10, model, "s3_t1", false);

        StreetSection streetSectionI1_t1 = new StreetSection(10, model, "s4_t1", false);
        StreetSection streetSectionI1_t2 = new StreetSection(10, model, "s4_t2", false);


        List<IConsumer> previousSections = new LinkedList<>();
        previousSections.add(streetSection1_t1);
        previousSections.add(streetSection1_t2);
        previousSections.add(streetSectionI1_t1);
        previousSections.add(streetSectionI1_t2);

        List<IConsumer> nextSections = new LinkedList<>();
        nextSections.add(streetSection2_t1);
        nextSections.add(streetSection2_t2);
        nextSections.add(streetSectionO1_t1);

        StreetConnector streetConnector = new StreetConnector(previousSections, nextSections);
        streetSection1_t1.setNextStreetConnector(streetConnector);
        streetSection1_t2.setNextStreetConnector(streetConnector);
        streetSectionI1_t1.setNextStreetConnector(streetConnector);
        streetSectionI1_t2.setNextStreetConnector(streetConnector);

        streetSection2_t1.setPreviousStreetConnector(streetConnector);
        streetSection2_t2.setPreviousStreetConnector(streetConnector);
        streetSectionO1_t1.setPreviousStreetConnector(streetConnector);

        streetConnector.initializeTrack(streetSection1_t1, ConsumerType.ROUNDABOUT_SECTION, streetSection2_t1, ConsumerType.ROUNDABOUT_SECTION);
        streetConnector.initializeTrack(streetSection1_t2, ConsumerType.ROUNDABOUT_SECTION, streetSection2_t2, ConsumerType.ROUNDABOUT_SECTION);

        streetConnector.initializeTrack(streetSection1_t1, ConsumerType.ROUNDABOUT_SECTION, streetSectionO1_t1, ConsumerType.ROUNDABOUT_EXIT);

        streetConnector.initializeTrack(streetSectionI1_t1, ConsumerType.ROUNDABOUT_INLET, streetSection2_t1, ConsumerType.ROUNDABOUT_SECTION);
        streetConnector.initializeTrack(streetSectionI1_t2, ConsumerType.ROUNDABOUT_INLET, streetSection2_t2, ConsumerType.ROUNDABOUT_SECTION);

        // sink

        RoundaboutSink sink1 = new RoundaboutSink(model, "SK1", false);

        List<IConsumer> sinkNext = new LinkedList<>();
        sinkNext.add(sink1);

        List<IConsumer> sinkPrev = new LinkedList<>();
        sinkPrev.add(streetSectionO1_t1);

        StreetConnector streetConnector2 = new StreetConnector(sinkPrev, sinkNext);
        sink1.setPreviousStreetConnector(streetConnector2);

        streetSectionO1_t1.setNextStreetConnector(streetConnector2);

        streetConnector2.initializeTrack(streetSectionO1_t1, ConsumerType.ROUNDABOUT_EXIT, sink1, ConsumerType.STREET_SECTION);

        // source (pro track)
        RoundaboutSource source1 = new RoundaboutSource(model, "so1", false, streetSectionI1_t1);
        RoundaboutSource source2 = new RoundaboutSource(model, "so2", false, streetSectionI1_t2);



        //Intersection

    }
}
