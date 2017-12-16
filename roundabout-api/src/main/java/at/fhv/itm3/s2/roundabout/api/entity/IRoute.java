package at.fhv.itm3.s2.roundabout.api.entity;

import javax.xml.transform.Source;
import java.util.List;

public interface IRoute {

    /**
     * Returns the route as unmodifiable list of {@link IStreet}.
     *
     * @return the route as unmodifiable list of {@link IStreet}.
     */
    List<IStreet> getRoute();

    /**
     * Returns an {@link IStreet} at the given index of the route.
     *
     * @param index the index the {@link IStreet} should be returned from the route.
     * @return an {@link IStreet} located at given index.
     */
    IStreet getSectionAt(int index);

    /**
     * Returns a start {@link IStreet} of the route.
     *
     * @return start {@link IStreet} of the route if present, otherwise null.
     */
    IStreet getStartSection();

    /**
     * Returns a destination {@link IStreet} of the route.
     *
     * @return start {@link IStreet} of the route if present, otherwise null.
     */
    IStreet getDestinationSection();

    /**
     * Returns the number of {@link IStreet} in the route.
     *
     * @return the number of {@link IStreet} in the route as int.
     */
    int getNumberOfSections();

    /**
     * Adds a new {@link IStreet} to the route.
     *
     * @param section the section that is added to the route at the end.
     */
    void addSection(IStreet section);

    /**
     * Checks if there are {@link IStreet}s in the route defined.
     *
     * @return true if there are {@link IStreet} in the route, otherwise false.
     */
    boolean isEmpty();

    /**
     * Checks if the {@link IStreet} #A is behind the {@link IStreet} #B in the route.
     *
     * @param sectionA bla.
     * @param sectionB bla.
     * @return bla (method is deleted later anyway).
     */
    boolean isSectionABehindSectionB(IStreet sectionA, IStreet sectionB);

    /**
     * Returns the index in the route of given {@link IStreet}
     *
     * @param streetSection is the {@link IStreet} from which the index should be returned
     * @return the index of streetSection in the route
     */
     int getIndexOfSection(IStreet streetSection);

    /**
     * Returns the {@link ISource} source of the route
     *
     * @return  the source of the route as {@link ISource}
     */
    ISource getSource();

    /**
     * Returns the {@link IStreet} sink of the route
     *
     * @return  the sink of the route as {@link IStreet}
     */
    IStreet getSink();

    void addSource(ISource source);
}
