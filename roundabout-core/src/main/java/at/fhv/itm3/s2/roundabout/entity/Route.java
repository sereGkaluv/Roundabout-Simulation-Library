package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.ISource;
import at.fhv.itm3.s2.roundabout.api.entity.Street;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Route implements IRoute {

    private List<Street> route;
    private ISource source;

    public Route() {
        route = new ArrayList<>();
    }

    @Override
    public List<Street> getRoute() {
        return Collections.unmodifiableList(route);
    }

    @Override
    public Street getSectionAt(int index) {
        if (index >= route.size()) {
            throw new IllegalArgumentException("Index value for accessing a section in a route is too big.");
        }
        return route.get(index);
    }

    @Override
    public Street getStartSection() {
        return !isEmpty() ? route.get(0) : null;
    }

    @Override
    public Street getDestinationSection() {
        return !isEmpty() ? route.get(route.size() - 1) : null;
    }

    @Override
    public int getNumberOfSections() {
        return route.size();
    }

    @Override
    public void addSection(Street section) {
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

    public int getIndexOfSection(Street streetSection) {
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
    public Street getSink() {
        return route.get(route.size()-1);
    }
}
