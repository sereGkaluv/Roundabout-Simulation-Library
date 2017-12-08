package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.Set;

public interface IStreetConnector {

    /**
     * Gets the next connected {@link IStreetSection}s, which are accessible via this connector.
     *
     * @return The further connected sections in form of {@link Set<IStreetSection>}.
     */
    Set<IStreetSection> getNextSections();

    /**
     * Gets the previous connected {@link IStreetSection}s, which are accessible through this connector.
     *
     * @return The previous connected sections in form of {@link Set<IStreetSection>}.
     */
    Set<IStreetSection> getPreviousSections();
}
