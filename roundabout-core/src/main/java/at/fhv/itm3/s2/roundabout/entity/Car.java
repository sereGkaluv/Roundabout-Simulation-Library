package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;

public class Car implements ICar {

    @Override
    public double getTimeToTraverseSection() {
        return 0;
    }

    @Override
    public double getTimeToTraverseSection(IStreetSection section) {
        return 0;
    }

    @Override
    public double getTransitionTime() {
        return 0;
    }

    @Override
    public IStreetSection getNextSection() {
        return null;
    }
}
