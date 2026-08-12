package edn.stratodonut.drivebywire.wire;

import edn.stratodonut.drivebywire.wire.graph.WireNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Set;

/**
 * COMPILE-ONLY API STUB of {@code edn.stratodonut.drivebywire.wire.WireNetworkManager}
 * (Drive-By-Wire). Excluded from the built jar.
 *
 * <p>Only the members used by Drive by DashPanels are declared, with exactly the
 * signatures of the real class.</p>
 */
public final class WireNetworkManager {
	public static final String WORLD_CHANNEL = "world";

	private WireNetworkManager() {
	}

	public static WireNetworkManager get(final Level level) {
		throw new AssertionError("API stub");
	}

	public static void trySetSignalAt(final Level level, final BlockPos source, final String channel, final int value) {
		throw new AssertionError("API stub");
	}

	public Map<String, Integer> getSourceSignals(final BlockPos source) {
		throw new AssertionError("API stub");
	}

	public int getSignalAt(final BlockPos sinkPos, final Direction direction) {
		throw new AssertionError("API stub");
	}

	public Map<Long, Map<String, Set<WireNetworkNode.WireNetworkSink>>> getNetwork() {
		throw new AssertionError("API stub");
	}
}
