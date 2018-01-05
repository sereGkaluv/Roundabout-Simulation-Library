package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSink;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Route implements IRoute {

    private List<IConsumer> route;
    private AbstractSource source;

    public Route() {
        route = new ArrayList<>();
    }

    @Override
    public List<IConsumer> getRoute() {
        return Collections.unmodifiableList(route);
    }

    @Override
    public IConsumer getSectionAt(int index) {
        if (index >= route.size()) {
            throw new IllegalArgumentException("Index value for accessing a section in a route is too big.");
        }
        return route.get(index);
    }

    @Override
    public IConsumer getStartSection() {
        return !isEmpty() ? route.get(0) : null;
    }

    @Override
    public IConsumer getDestinationSection() {
        return !isEmpty() ? route.get(route.size() - 1) : null;
    }

    @Override
    public int getNumberOfSections() {
        return route.size();
    }

    @Override
    public void addSection(IConsumer section) {
        // Adds as a last element to list.
        route.add(section);
    }

    @Override
    public void addSource(AbstractSource source) {
        this.source = source;
    }

    @Override
    public AbstractSource getSource() {
        return this.source;
    }

    @Override
    public AbstractSink getSink() {
        final IConsumer destinationSection = this.getDestinationSection();
        if (destinationSection instanceof AbstractSink) {
            return (AbstractSink) destinationSection;
        } else {
            throw new IllegalArgumentException("Destination section is not an instance of Sink.");
        }
    }

    @Override
    public boolean isEmpty() {
        return route.isEmpty();
    }

    public int getIndexOfSection(IConsumer streetSection) {
        if (!route.contains(streetSection)) {
            throw new IllegalArgumentException("Section must be part of the route");
        }
        return route.indexOf(streetSection);
    }
}
