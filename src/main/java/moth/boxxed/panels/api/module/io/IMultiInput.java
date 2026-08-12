package moth.boxxed.panels.api.module.io;

import java.util.function.BiConsumer;

/**
 * COMPILE-ONLY API STUB of {@code moth.boxxed.panels.api.module.io.IMultiInput}
 * (Dashpanels, MIT). Excluded from the built jar.
 */
public interface IMultiInput {
	void getValues(BiConsumer<String, AnalogResult> consumer);

	@FunctionalInterface
	interface AnalogResult {
		int getAnalog();
	}
}
