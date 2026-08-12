package moth.boxxed.panels.api.module;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * COMPILE-ONLY API STUB of {@code moth.boxxed.panels.api.module.ModuleMap}
 * (Dashpanels, MIT). Excluded from the built jar.
 */
public class ModuleMap extends LinkedHashMap<String, Module> implements Iterable<Map.Entry<String, Module>> {
	public static ModuleMap empty() {
		throw new AssertionError("API stub");
	}

	public List<Map.Entry<String, Module>> asEntryList() {
		throw new AssertionError("API stub");
	}

	public Module normalGet(final Object key) {
		throw new AssertionError("API stub");
	}

	public boolean normalContainsKey(final Object key) {
		throw new AssertionError("API stub");
	}

	@Override
	public Iterator<Map.Entry<String, Module>> iterator() {
		throw new AssertionError("API stub");
	}
}
