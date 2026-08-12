package moth.boxxed.panels.api.network;

import moth.boxxed.panels.api.module.ModuleMap;

import java.util.Set;
import java.util.UUID;

/**
 * COMPILE-ONLY API STUB of {@code moth.boxxed.panels.api.network.ModulesNetwork}
 * (Dashpanels, MIT). Excluded from the built jar.
 */
public class ModulesNetwork {
	public UUID id;
	public Set<ModulesNetworkMember> members;
	public ModuleMap compiledModules;

	public void compileModules() {
		throw new AssertionError("API stub");
	}

	public ModuleMap getCompiledModules() {
		throw new AssertionError("API stub");
	}

	public boolean hasModule(final String module) {
		throw new AssertionError("API stub");
	}

	public boolean hasMember(final ModulesNetworkMember member) {
		throw new AssertionError("API stub");
	}
}
