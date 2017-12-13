package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Route implements IRoute {

    private List<IStreetSection> route;

    public Route() {
        route = new ArrayList<>();
    }

    @Override
    public List<IStreetSection> getRoute() {
        return Collections.unmodifiableList(route);
    }

    @Override
    public IStreetSection getSectionAt(int index) {
        if (index >= route.size()) {
            throw new IllegalArgumentException("Index value for accessing a section in a route is too big.");
        }
        return route.get(index);
    }

    @Override
    public IStreetSection getStartSection() {
        return !isEmpty() ? route.get(0) : null;
    }

    @Override
    public IStreetSection getDestinationSection() {
        return !isEmpty() ? route.get(route.size() - 1) : null;
    }

    @Override
    public int getNumberOfSections() {
        return route.size();
    }

    @Override
    public void addSection(IStreetSection section) {
        // Adds as a last element to list.
        route.add(section);
    }

    @Override
    public boolean isEmpty() {
        return route.isEmpty();
    }

    @Override
    public boolean isSectionABehindSectionB(IStreetSection sectionA, IStreetSection sectionB) {
        if (!route.contains(sectionA) || ! route.contains(sectionB)) {
            throw new IllegalArgumentException("Both sections must be part of the route");
        }

        return (route.indexOf(sectionA) > route.indexOf(sectionB));
    }
}
