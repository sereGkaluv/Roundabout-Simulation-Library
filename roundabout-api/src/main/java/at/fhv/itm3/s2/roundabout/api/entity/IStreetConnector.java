package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.IConsumer;

import java.util.List;

public interface IStreetConnector {

    /**
     * Gets the next connected {@link IConsumer}s, which are accessible via this connector.
     *
     * @return The further connected sections in form of {@link List< IConsumer >}.
     */
    List<IConsumer> getNextSections();

    /**
     * Gets the previous connected {@link IConsumer}s, which are accessible through this connector.
     *
     * @return The previous connected sections in form of {@link List< IConsumer >}.
     */
    List<IConsumer> getPreviousSections();

    /**
     * Gets the previous connected {@link IConsumer}s with the given type, which are accessible through this connector.
     *
     * @param streetType    the street type the returned {@link IConsumer}s have
     * @return              the previous connected sections with the given street type in form of {@link List<IConsumer>}
     */
    List<IConsumer> getPreviousSections(StreetType streetType);

    /**
     * Returns the previous sections fo the given street of the given type on its track
     *
     * @param street        a next street of the connector
     * @param streetType    the type the returned sections are of
     * @return              the next sections as {@link IConsumer}
     */
    List<IConsumer> getPreviousTrackSections(IConsumer street, StreetType streetType);

    /**
     * Checks if the nextStreet is on the same track as the currentStreet
     *
     * @param currentStreet     the street on the track, we're interested in
     * @param nextStreet        the next street
     * @return                  true, if the next street is on the same track as the current street, else false
     */
    boolean isNextStreetOnSameTrackAsCurrent(IConsumer currentStreet, IConsumer nextStreet);

    /**
     * Connects the two given streets to a track (used for precedence)
     *
     * @param firstStreet   the previous street of the connector
     * @param secondStreet  the next street of the connector
     */
    void initializeTrack(IConsumer firstStreet, StreetType streetTypeOfFirstStreet, IConsumer secondStreet, StreetType streetTypeOfSecondStreet);

    /**
     * Returns the type of a street connected to the {@link IStreetConnector}
     *
     * @param street    the street the type of should be returned
     * @return          the type as {@link IConsumer}
     */
    StreetType getTypeOfStreet(IConsumer street);
}
