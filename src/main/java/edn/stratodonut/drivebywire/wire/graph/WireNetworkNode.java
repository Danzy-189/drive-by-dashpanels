package edn.stratodonut.drivebywire.wire.graph;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/**
 * COMPILE-ONLY API STUB of {@code edn.stratodonut.drivebywire.wire.graph.WireNetworkNode}
 * (Drive-By-Wire). Excluded from the built jar.
 */
public class WireNetworkNode {
	/** A wired block face: {@code position} is {@link BlockPos#asLong()}, {@code direction} is {@link Direction#get3DDataValue()}. */
	public record WireNetworkSink(long position, int direction) {
		public static WireNetworkSink of(final BlockPos pos, final Direction direction) {
			return new WireNetworkSink(pos.asLong(), direction.get3DDataValue());
		}
	}

	/** A single incoming signal, keyed by source position and channel. */
	public record InputKey(long sourcePos, String channel) {
	}
}
