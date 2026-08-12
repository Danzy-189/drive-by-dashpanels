package edn.stratodonut.drivebywire.wire;

import java.util.List;

/**
 * COMPILE-ONLY API STUB of {@code edn.stratodonut.drivebywire.wire.MultiChannelWireSource}
 * (Drive-By-Wire). Excluded from the built jar.
 *
 * <p>Implemented by wire source blocks that offer more than the default
 * {@code world} channel.</p>
 */
public interface MultiChannelWireSource {
	List<String> wire$getChannels();

	String wire$nextChannel(String current, boolean forward);
}
