package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSink;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Route implements IRoute {

    private List<IConsumer> route;
    private AbstractSource source;
    private Double ratio;

    public Route() {
        this(null, new ArrayList<>(), 1.0);
    }

    public Route(AbstractSource source, List<IConsumer> route, Double ratio) {
        this.route = route;
        this.source = source;
        this.ratio = ratio;
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
    public void setSource(AbstractSource source) {
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
    public Double getRatio() {
        return ratio;
    }

    @Override
    public boolean isEmpty() {
        return route.isEmpty();
    }

    public int getIndexOfSection(IConsumer streetSection) {
        if (!route.contains(streetSection)) {
            throw new IllegalArgumentException("Track must be part of the route");
        }
        return route.indexOf(streetSection);
    }

    @Override
    public boolean contains (IConsumer section) {
        if (!(section instanceof Street)) {
           throw new IllegalStateException("All previous IConsumer should be of type Street");
        }
        return IntStream.range(0, route.size()).anyMatch(i -> route.get(i).equals(section));
    }
}
