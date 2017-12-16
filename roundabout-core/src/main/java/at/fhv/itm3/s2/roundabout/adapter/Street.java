package at.fhv.itm3.s2.roundabout.adapter;

import at.fhv.itm3.s2.roundabout.api.entity.IStreet;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public abstract class Street extends Entity implements IStreet {

    protected int carCounter;

    public Street(Model model, String s, boolean b) {
        super(model, s, b);
    }
}
