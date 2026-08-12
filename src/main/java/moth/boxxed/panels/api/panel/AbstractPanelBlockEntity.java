package moth.boxxed.panels.api.panel;

import moth.boxxed.panels.api.network.ModulesNetworkMember;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * COMPILE-ONLY API STUB of {@code moth.boxxed.panels.api.panel.AbstractPanelBlockEntity}
 * (Dashpanels, MIT). Excluded from the built jar.
 */
public abstract class AbstractPanelBlockEntity extends ModulesNetworkMember {
	public AbstractPanelBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}
}
