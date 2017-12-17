package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.Street;

import java.util.Set;

public class StreetConnector implements IStreetConnector {
    private Set<Street> nextSections;
    private Set<Street> previousSections;

    public StreetConnector(Set<Street> previousSections, Set<Street> nextSections){
        this.previousSections = previousSections;
        this.nextSections = nextSections;
    }

    @Override
    public Set<Street> getNextSections() {
        return nextSections;
    }

    @Override
    public Set<Street> getPreviousSections() {
        return previousSections;
    }
}
