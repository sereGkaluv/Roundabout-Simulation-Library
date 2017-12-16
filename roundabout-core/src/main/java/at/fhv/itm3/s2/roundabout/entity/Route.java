package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.ISource;
import at.fhv.itm3.s2.roundabout.api.entity.IStreet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Route implements IRoute {

    private List<IStreet> route;
    private ISource source;

    public Route() {
        route = new ArrayList<>();
    }

    @Override
    public List<IStreet> getRoute() {
        return Collections.unmodifiableList(route);
    }

    @Override
    public IStreet getSectionAt(int index) {
        if (index >= route.size()) {
            throw new IllegalArgumentException("Index value for accessing a section in a route is too big.");
        }
        return route.get(index);
    }

    @Override
    public IStreet getStartSection() {
        return !isEmpty() ? route.get(0) : null;
    }

    @Override
    public IStreet getDestinationSection() {
        return !isEmpty() ? route.get(route.size() - 1) : null;
    }

    @Override
    public int getNumberOfSections() {
        return route.size();
    }

    @Override
    public void addSection(IStreet section) {
        // Adds as a last element to list.
        route.add(section);
    }

    @Override
    public void addSource(ISource source) {
        this.source = source;
    }

    @Override
    public boolean isEmpty() {
        return route.isEmpty();
    }

    @Override
    public boolean isSectionABehindSectionB(IStreet sectionA, IStreet sectionB) {
        if (!route.contains(sectionA) || ! route.contains(sectionB)) {
            throw new IllegalArgumentException("Both sections must be part of the route");
        }

        return (route.indexOf(sectionA) > route.indexOf(sectionB));
    }

    public int getIndexOfSection(IStreet streetSection) {
        if (!route.contains(streetSection)) {
            throw new IllegalArgumentException("Section must be part of the route");
        }
        return route.indexOf(streetSection);
    }

    @Override
    public ISource getSource() {
        return this.source;
    }

    @Override
    public IStreet getSink() {
        return route.get(route.size()-1);
    }
}
