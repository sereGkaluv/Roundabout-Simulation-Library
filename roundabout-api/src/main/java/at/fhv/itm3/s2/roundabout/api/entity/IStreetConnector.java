package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.Set;

public interface IStreetConnector {

    /**
     * Gets the next connected sections, which are accessible via this connector.
     * @return The further connected sections.
     */
    Set<IStreetSection> getNextSections();

    /**
     * Gets the previous connected sections, which are accessible through this connector.
     * @return The previous connected sections.
     */
    Set<IStreetSection> getPreviousSections();
}
