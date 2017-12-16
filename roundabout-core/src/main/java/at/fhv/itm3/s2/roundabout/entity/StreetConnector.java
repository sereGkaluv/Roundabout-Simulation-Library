package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreet;

import java.util.Set;

public class StreetConnector implements IStreetConnector {
    private Set<IStreet> nextSections;
    private Set<IStreet> previousSections;

    public StreetConnector(Set<IStreet> previousSections, Set<IStreet> nextSections){
        this.previousSections = previousSections;
        this.nextSections = nextSections;
    }

    @Override
    public Set<IStreet> getNextSections() {
        return nextSections;
    }

    @Override
    public Set<IStreet> getPreviousSections() {
        return previousSections;
    }
}
