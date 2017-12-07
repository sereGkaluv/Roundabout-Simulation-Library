package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.List;

public interface IRoute {

    /**
     * Adds a new section to the route
     *
     * @param section   the section that is added to the route at the end
     */
    void addSection(IStreetSection section);

    /**
     * Returns the route as list of IStreetSection
     *
     * @return  the route as list of IStreetSection
     */
    List<IStreetSection> getRoute();

    /**
     * Returns an IStreetSection at the given index of the route
     *
     * @param index     the index the IStreetSection should be returned from the route
     * @return          an IStreetSection
     */
    IStreetSection getSectionAt(int index);

    /**
     * Checks if there are sections in the route defined
     *
     * @return  true if there are sections in the route, false if there are no sections in the route
     */
    boolean isEmpty();

    /**
     * Returns the number of sections in the route
     *
     * @return  the number of sections in the route as int
     */
    int getNumberOfSections();

    /**
     * Checks if the newSection is behind the oldSection in the route
     *
     * @param sectionA    bla
     * @param sectionB    bla
     * @return            bla (method is deleted later anyway)
     */
    boolean isSectionABehindSectionB(IStreetSection sectionA, IStreetSection sectionB);
}
