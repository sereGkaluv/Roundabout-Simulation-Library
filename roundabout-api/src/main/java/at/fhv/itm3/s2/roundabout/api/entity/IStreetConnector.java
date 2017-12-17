package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.Set;

public interface IStreetConnector {

    /**
     * Gets the next connected {@link Street}s, which are accessible via this connector.
     *
     * @return The further connected sections in form of {@link Set< Street >}.
     */
    Set<Street> getNextSections();

    /**
     * Gets the previous connected {@link Street}s, which are accessible through this connector.
     *
     * @return The previous connected sections in form of {@link Set< Street >}.
     */
    Set<Street> getPreviousSections();
}
