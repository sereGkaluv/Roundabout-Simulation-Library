package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.api.event.ICarGenerateEvent;
import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;


public class CarGenerateEvent extends Event<Entity> implements ICarGenerateEvent {

    public CarGenerateEvent(Model model, String s, boolean b) {
        super(model, s, b);
    }

    public void eventRoutine(Entity entity) throws SuspendExecution {

    }
}
