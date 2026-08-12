package dev.danzy.drivebydashpanels.wire;

import java.util.List;

/**
 * Client side snapshot of the channels offered by the transmitter the player is currently wiring.
 *
 * <p>Drive-By-Wire asks the {@code Block} instance (not the block entity) for the next channel while
 * the player scrolls, so the position dependent channel list has to be handed over out of band.
 * The snapshot is filled in {@code ClientChannelTracker} when the player clicks a transmitter with
 * the wire tool.</p>
 */
public final class ChannelSelection {

    private static volatile List<String> channels = List.of();

    private ChannelSelection() {
    }

    public static void set(List<String> newChannels) {
        channels = newChannels == null || newChannels.isEmpty() ? List.of() : List.copyOf(newChannels);
    }

    public static void clear() {
        channels = List.of();
    }

    public static List<String> get() {
        return channels;
    }
}
