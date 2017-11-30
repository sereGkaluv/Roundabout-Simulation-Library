package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;

import java.util.Set;

public class StreetConnector implements IStreetConnector {
    private Set<IStreetSection> nextSections;
    private Set<IStreetSection> previousSections;

    public StreetConnector(Set<IStreetSection> previousSections, Set<IStreetSection> nextSections){
        this.previousSections = previousSections;
        this.nextSections = nextSections;
    }

    @Override
    public Set<IStreetSection> getNextSections() {
        return nextSections;
    }

    @Override
    public Set<IStreetSection> getPreviousSections() {
        return previousSections;
    }
}
