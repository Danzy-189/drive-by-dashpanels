package dev.danzy.drivebydashpanels.wire;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import dev.danzy.drivebydashpanels.DriveByDashPanels;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

/**
 * Thin, late-bound bridge to {@code edn.stratodonut.drivebywire.wire.WireNetworkManager}.
 *
 * <p>Drive-By-Wire's manager class carries Sable and Create types in unrelated method signatures,
 * so it is bound reflectively at runtime instead of at compile time. That keeps the addon's compile
 * classpath tiny and, more importantly, turns any future Drive-By-Wire API rename into a logged
 * warning plus idle bridge blocks rather than a crash on world load.</p>
 */
public final class WireBridge {

    /** Drive-By-Wire's implicit channel that mirrors vanilla redstone at the source position. */
    public static final String WORLD_CHANNEL = "world";

    /** Highest analog value a wire channel can carry. */
    public static final int MAX_SIGNAL = 15;

    private static final String MANAGER_CLASS = "edn.stratodonut.drivebywire.wire.WireNetworkManager";

    private static boolean initialized;
    private static boolean available;
    private static boolean errorLogged;

    private static MethodHandle trySetSignalAt;
    private static MethodHandle getManager;
    private static MethodHandle getSignalAt;

    private WireBridge() {
    }

    public static synchronized boolean isAvailable() {
        if (!initialized) {
            initialized = true;
            bind();
        }
        return available;
    }

    private static void bind() {
        try {
            Class<?> manager = Class.forName(MANAGER_CLASS, false, WireBridge.class.getClassLoader());
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();

            trySetSignalAt = lookup.findStatic(manager, "trySetSignalAt",
                    MethodType.methodType(void.class, Level.class, BlockPos.class, String.class, int.class));
            getManager = lookup.findStatic(manager, "get",
                    MethodType.methodType(manager, Level.class));
            getSignalAt = lookup.findVirtual(manager, "getSignalAt",
                    MethodType.methodType(int.class, BlockPos.class, Direction.class));

            available = true;
        } catch (Throwable throwable) {
            available = false;
            DriveByDashPanels.LOGGER.error(
                    "Could not bind to the Drive-By-Wire network API ({}). The bridge blocks will stay idle.",
                    MANAGER_CLASS, throwable);
        }
    }

    /**
     * Publishes {@code value} on {@code channel} for the wire source at {@code pos}.
     * Server side only.
     */
    public static void setSignal(Level level, BlockPos pos, String channel, int value) {
        if (!isAvailable() || level == null || level.isClientSide() || channel == null || channel.isEmpty()) {
            return;
        }
        try {
            trySetSignalAt.invoke(level, pos, channel, clamp(value));
        } catch (Throwable throwable) {
            disable(throwable);
        }
    }

    /**
     * @return the signal delivered by wire connections that end on the {@code side} face of {@code pos}.
     */
    public static int getSignal(Level level, BlockPos pos, Direction side) {
        if (!isAvailable() || level == null) {
            return 0;
        }
        try {
            Object manager = getManager.invoke(level);
            if (manager == null) {
                return 0;
            }
            return clamp((int) getSignalAt.invoke(manager, pos, side));
        } catch (Throwable throwable) {
            disable(throwable);
            return 0;
        }
    }

    public static int clamp(int value) {
        return Math.max(0, Math.min(MAX_SIGNAL, value));
    }

    private static synchronized void disable(Throwable throwable) {
        available = false;
        if (!errorLogged) {
            errorLogged = true;
            DriveByDashPanels.LOGGER.error("Drive-By-Wire network call failed, disabling the bridge.", throwable);
        }
    }
}
