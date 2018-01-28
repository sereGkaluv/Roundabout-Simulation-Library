package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.IConsumer;

import java.util.Collection;
import java.util.List;

public interface IStreetConnector {

    /**
     * Gets the next connected {@link IConsumer}s, which are accessible via this connector.
     *
     * @return The further connected {@link IConsumer}s as {@link Collection} of {@link IConsumer}.
     */
    Collection<IConsumer> getNextConsumers();

    /**
     * Gets the previous connected {@link IConsumer}s, which are accessible through this connector.
     *
     * @return The previous connected {@link IConsumer}s as {@link Collection} of {@link IConsumer}.
     */
    Collection<IConsumer> getPreviousConsumers();

    /**
     * Gets the previous connected {@link IConsumer}s with the given type, which are accessible through this connector.
     *
     * @param consumerType    the street type the returned {@link IConsumer}s have
     * @return the previous connected {@link IConsumer}s with the given street type as {@link List} of {@link IConsumer}
     */
    List<IConsumer> getPreviousConsumers(ConsumerType consumerType);

    /**
     * Returns the previous connected {@link IConsumer}s fo the given {@link IConsumer} of the given {@link ConsumerType} on its track
     *
     * @param consumer      a next {@link IConsumer} of the connector
     * @param consumerType    the {@link ConsumerType} the returned {@link IConsumer}s are of
     * @return              the next consumers as {@link IConsumer}
     */
    List<IConsumer> getPreviousTrackConsumers(IConsumer consumer, ConsumerType consumerType);

    /**
     * Checks if the nextConsumer is on the same track as the currentConsumer
     *
     * @param currentConsumer     the {@link IConsumer} on the track, we're interested in
     * @param nextConsumer        the next {@link IConsumer}
     * @return                      true, if the next {@link IConsumer} is on the same track as the current, else false
     */
    boolean isNextConsumerOnSameTrackAsCurrent(IConsumer currentConsumer, IConsumer nextConsumer);

    /**
     * Connects the two given {@link IConsumer}s to a track (used for precedence)
     *
     * @param firstConsumer   a previous {@link IConsumer} of the connector
     * @param secondConsumer  a next {@link IConsumer} of the connector
     */
    void initializeTrack(IConsumer firstConsumer, ConsumerType consumerTypeOfFirstConsumer, IConsumer secondConsumer, ConsumerType consumerTypeOfSecondConsumer);

    /**
     * Returns the type of a {@link IConsumer} connected to the {@link IStreetConnector}
     *
     * @param consumer  the {@link IConsumer} the type of should be returned
     * @return          the type as {@link IConsumer}
     */
    ConsumerType getTypeOfConsumer(IConsumer consumer);
}
