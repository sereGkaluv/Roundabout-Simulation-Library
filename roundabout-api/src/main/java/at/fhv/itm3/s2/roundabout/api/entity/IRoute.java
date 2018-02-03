package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.IConsumer;

import java.util.Collection;
import java.util.List;

public interface IRoute {

    /**
     * Returns the route as unmodifiable list of {@link Street}.
     *
     * @return the route as unmodifiable list of {@link Street}.
     */
    Collection<IConsumer> getRoute();

    /**
     * Returns an {@link Street} at the given index of the route.
     *
     * @param index the index the {@link Street} should be returned from the route.
     * @return an {@link Street} located at given index.
     */
    IConsumer getSectionAt(int index);

    /**
     * Returns a start {@link Street} of the route.
     *
     * @return start {@link Street} of the route if present, otherwise null.
     */
    IConsumer getStartSection();

    /**
     * Returns a destination {@link Street} of the route.
     *
     * @return start {@link Street} of the route if present, otherwise null.
     */
    IConsumer getDestinationSection();

    /**
     * Returns the number of {@link Street} in the route.
     *
     * @return the number of {@link Street} in the route as int.
     */
    int getNumberOfSections();

    /**
     * Adds a new {@link Street} to the route.
     *
     * @param section the section that is added to the route at the end.
     */
    void addSection(IConsumer section);

    /**
     * Checks if there are {@link Street}s in the route defined.
     *
     * @return true if there are {@link Street} in the route, otherwise false.
     */
    boolean isEmpty();

    /**
     * Returns the index in the route of given {@link Street}
     *
     * @param streetSection is the {@link Street} from which the index should be returned
     * @return the index of streetSection in the route
     */
     int getIndexOfSection(IConsumer streetSection);

    /**
     * Returns the {@link AbstractSource} source of the route
     *
     * @return  the source of the route as {@link AbstractSource}
     */
    AbstractSource getSource();

    void setSource(AbstractSource source);

    /**
     * Returns the {@link Street} sink of the route
     *
     * @return  the sink of the route as {@link Street}
     */
    AbstractSink getSink();

    /**
     * Returns the ratio of the traffic flow compared to the main traffic flow.
     *
     * @return ratio of traffic flow
     */
    Double getRatio();

    /**
    * Checks if there is a specific {@link Street}s already in the route.
    *
    * @return true if there is a specific {@link Street} in the route, otherwise false.
    */
    boolean contains(IConsumer section);
}
