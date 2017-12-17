package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StreetConnector implements IStreetConnector {
    private Set<IStreetSection> nextSections;
    private Set<IStreetSection> previousSections;

    public StreetConnector() {
        nextSections = new HashSet<>();
        previousSections = new HashSet<>();
    }

    public StreetConnector(Set<IStreetSection> previousSections, Set<IStreetSection> nextSections){
        this.previousSections = new HashSet<>(previousSections);
        this.nextSections = new HashSet<>(nextSections);
    }

    @Override
    public void addNextSection(IStreetSection streetSection) {
        nextSections.add(streetSection);
    }

    @Override
    public void addPreviousSection(IStreetSection streetSection) {
        previousSections.add(streetSection);
    }

    @Override
    public Set<IStreetSection> getNextSections() {
        return Collections.unmodifiableSet(nextSections);
    }

    @Override
    public Set<IStreetSection> getPreviousSections() {
        return Collections.unmodifiableSet(previousSections);
    }
}
