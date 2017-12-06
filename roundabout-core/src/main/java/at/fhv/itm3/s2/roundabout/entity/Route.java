package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;

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
        return route;
    }

    public IStreetSection getSection(int index) {
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
    public boolean isNewSectionBehindOldSection(IStreetSection newSection, IStreetSection oldSection) {
        if (!route.contains(newSection) || ! route.contains(oldSection)) {
            throw new IllegalArgumentException("Both sections must be part of the route");
        }

        return (route.indexOf(newSection) > route.indexOf(oldSection));
    }
}
