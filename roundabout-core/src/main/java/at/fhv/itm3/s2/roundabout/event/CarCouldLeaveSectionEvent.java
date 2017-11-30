package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.RoundaboutModel;
import at.fhv.itm3.s2.roundabout.api.event.ICarCouldLeaveSectionEvent;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;

public class CarCouldLeaveSectionEvent extends Event<StreetSection> implements ICarCouldLeaveSectionEvent {

    private RoundaboutModel myModel;

    public CarCouldLeaveSectionEvent(Model model, String s, boolean b) {
        super(model, s, b);
        myModel = (RoundaboutModel)model;
    }

    public void eventRoutine(StreetSection section) throws SuspendExecution {
        if(section.isFirstCarOnExitPoint()) {
            if(section.carCouldEnterNextSection()) {
                new CarCouldLeaveSectionEvent(myModel, "CarCouldLeaveSectionEvent", true).schedule((StreetSection)section.getNextSectionOfFirstCar(), new TimeSpan(section.getFirstCar().getTimeToTraverseSection(section.getNextSectionOfFirstCar()), TimeUnit.SECONDS));
                section.moveFirstCarToNextSection();

                if(!section.isEmpty()) {
                    section.updateAllCarsPositions();
                    new CarCouldLeaveSectionEvent(myModel, "CarCouldLeaveSectionEvent", true).schedule(section, new TimeSpan(section.getFirstCar().getTransitionTime(), TimeUnit.SECONDS));
                }

//                for (StreetSection prevSection : ) {
//                    new CarCouldLeaveSectionEvent(myModel, "CarCouldLeaveSectionEvent", true).schedule(prevSection, new TimeSpan(0, TimeUnit.SECONDS));
//                }
            }
        }
    }
}