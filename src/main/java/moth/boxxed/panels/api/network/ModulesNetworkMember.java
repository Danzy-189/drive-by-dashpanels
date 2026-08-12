package moth.boxxed.panels.api.network;

import moth.boxxed.panels.api.module.ModuleMap;
import moth.boxxed.panels.util.BaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

/**
 * COMPILE-ONLY API STUB of {@code moth.boxxed.panels.api.network.ModulesNetworkMember}
 * (Dashpanels, MIT). Excluded from the built jar.
 */
public abstract class ModulesNetworkMember extends BaseBlockEntity {
	public UUID network;

	public ModulesNetworkMember(final BlockEntityType<?> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	public ModulesNetwork getOrCreate() {
		throw new AssertionError("API stub");
	}

	public boolean hasNetwork() {
		throw new AssertionError("API stub");
	}

	public void networkUpdate(final ModulesNetwork modulesNetwork) {
		throw new AssertionError("API stub");
	}

	public ModuleMap getModules() {
		throw new AssertionError("API stub");
	}

	public void setNetwork(final UUID uuid) {
		throw new AssertionError("API stub");
	}
}
