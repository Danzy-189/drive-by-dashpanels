package moth.boxxed.panels.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * COMPILE-ONLY API STUB of {@code moth.boxxed.panels.util.BaseBlockEntity}
 * (Dashpanels, MIT, https://github.com/BoxxedDev/control-panels).
 *
 * <p>Only the members Drive by DashPanels actually uses are declared, with the
 * exact same signatures as the real class. This file is excluded from the built
 * jar (see build.gradle) - at runtime the real Dashpanels classes are used.</p>
 */
public abstract class BaseBlockEntity extends BlockEntity {
	public boolean chunkUnloaded;
	public boolean init;

	public BaseBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	public void remove() {
		throw new AssertionError("API stub");
	}

	public void init() {
		throw new AssertionError("API stub");
	}

	public void tick(final Level level, final BlockPos blockPos, final BlockState blockState) {
		throw new AssertionError("API stub");
	}

	public void blockChanged() {
		throw new AssertionError("API stub");
	}
}
