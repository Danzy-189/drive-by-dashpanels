package moth.boxxed.panels.api.module.io;

import java.util.function.BiConsumer;

/**
 * COMPILE-ONLY API STUB of {@code moth.boxxed.panels.api.module.io.IMultiOutput}
 * (Dashpanels, MIT). Excluded from the built jar.
 */
public interface IMultiOutput {
	void setValues(BiConsumer<String, AnalogRunnable> consumer);

	@FunctionalInterface
	interface AnalogRunnable {
		void setAnalog(int value);
	}
}
