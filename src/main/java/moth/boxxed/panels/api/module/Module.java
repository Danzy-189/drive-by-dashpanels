package moth.boxxed.panels.api.module;

import moth.boxxed.panels.api.panel.AbstractPanelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * COMPILE-ONLY API STUB of {@code moth.boxxed.panels.api.module.Module}
 * (Dashpanels, MIT). Excluded from the built jar.
 *
 * <p>Field and method signatures match the real class exactly, which is what
 * matters for runtime linkage.</p>
 */
public abstract class Module {
	public AbstractPanelBlockEntity parentBlockEntity;

	public String getName() {
		throw new AssertionError("API stub");
	}

	public void setName(final String string) {
		throw new AssertionError("API stub");
	}

	public BlockPos getParentPos() {
		throw new AssertionError("API stub");
	}

	public Level getLevel() {
		throw new AssertionError("API stub");
	}
}
