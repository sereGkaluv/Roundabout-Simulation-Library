package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.api.event.ICarCouldLeaveSectionEvent;
import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;

public class CarCouldLeaveSectionEvent extends Event<Entity> implements ICarCouldLeaveSectionEvent {

    public CarCouldLeaveSectionEvent(Model model, String s, boolean b) {
        super(model, s, b);
    }

    public void eventRoutine(Entity entity) throws SuspendExecution {

    }
}
