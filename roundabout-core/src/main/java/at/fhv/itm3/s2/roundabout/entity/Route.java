package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Route implements IRoute {

    private List<IStreetSection> route;

    public Route() {
        route = new LinkedList<>();
    }


    public void addSection(IStreetSection section) {
        route.add(section);
    }

    public List<IStreetSection> getRoute() {
        return Collections.unmodifiableList(route);
    }

    public IStreetSection getSectionAt(int index) {
        if (index >= route.size()) {
            throw new IllegalArgumentException("Index for accessing a section in a route is too high");
        }
        return route.get(index);
    }

    public boolean isEmpty() {
        return route.isEmpty();
    }

    @Override
    public int getNumberOfSections() {
        return route.size();
    }

    @Override
    public boolean isSectionABehindSectionB(IStreetSection sectionA, IStreetSection sectionB) {
        if (!route.contains(sectionA) || ! route.contains(sectionB)) {
            throw new IllegalArgumentException("Both sections must be part of the route");
        }

        return (route.indexOf(sectionA) > route.indexOf(sectionB));
    }

    public int getIndexOfSection(IStreetSection streetSection) {
        if (!route.contains(streetSection)) {
            throw new IllegalArgumentException("Section must be part of the route");
        }
        return route.indexOf(streetSection);
    }
}
