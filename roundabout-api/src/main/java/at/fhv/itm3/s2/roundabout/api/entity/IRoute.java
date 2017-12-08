package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.List;

public interface IRoute {

    /**
     * Adds a new {@link IStreetSection} to the route.
     *
     * @param section the section that is added to the route at the end.
     */
    void addSection(IStreetSection section);

    /**
     * Returns the route as unmodifiable list of {@link IStreetSection}.
     *
     * @return the route as unmodifiable list of {@link IStreetSection}.
     */
    List<IStreetSection> getRoute();

    /**
     * Returns an {@link IStreetSection} at the given index of the route.
     *
     * @param index the index the {@link IStreetSection} should be returned from the route.
     * @return an {@link IStreetSection} located at given index.
     */
    IStreetSection getSectionAt(int index);

    /**
     * Checks if there are {@link IStreetSection}s in the route defined.
     *
     * @return true if there are {@link IStreetSection} in the route, otherwise false.
     */
    boolean isEmpty();

    /**
     * Returns the number of {@link IStreetSection} in the route.
     *
     * @return the number of {@link IStreetSection} in the route as int.
     */
    int getNumberOfSections();

    /**
     * Checks if the {@link IStreetSection} #A is behind the {@link IStreetSection} #B in the route.
     *
     * @param sectionA bla.
     * @param sectionB bla.
     * @return bla (method is deleted later anyway).
     */
    boolean isSectionABehindSectionB(IStreetSection sectionA, IStreetSection sectionB);
}
