package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.RoundaboutModel;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.event.ICarGenerateEvent;
import at.fhv.itm3.s2.roundabout.entity.Car;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;


public class CarGenerateEvent extends Event<StreetSection> implements ICarGenerateEvent {

    private RoundaboutModel myModel;

    public CarGenerateEvent(Model model, String s, boolean b) {
        super(model, s, b);
        myModel = (RoundaboutModel)model;
    }

    public void eventRoutine(StreetSection section) throws SuspendExecution {
        ICar car = new Car();
        section.addCar(car);
        new CarCouldLeaveSectionEvent(this.getModel(), "CarCouldLeaveSectionEvent", true).schedule(section, new TimeSpan(car.getTimeToTraverseSection(), TimeUnit.SECONDS));
        new CarGenerateEvent(this.getModel(), "CarGenerateEvent", true).schedule(section, new TimeSpan(myModel.getTimeBetweenCarArrivals(), TimeUnit.SECONDS));
    }
}