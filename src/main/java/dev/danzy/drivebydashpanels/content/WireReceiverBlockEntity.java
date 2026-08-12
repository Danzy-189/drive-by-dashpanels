package dev.danzy.drivebydashpanels.content;

import java.util.HashMap;
import java.util.Map;

import dev.danzy.drivebydashpanels.DriveByDashPanels;
import dev.danzy.drivebydashpanels.wire.WireBridge;

import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.ModuleMap;
import moth.boxxed.panels.api.module.io.IMultiOutput;
import moth.boxxed.panels.api.module.io.IOutput;
import moth.boxxed.panels.api.network.ModulesNetwork;
import moth.boxxed.panels.api.network.ModulesNetworkMember;
import moth.boxxed.panels.api.panel.AbstractPanelBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Applies the wire signal arriving on any of its faces to a single named output module.
 */
public class WireReceiverBlockEntity extends ModulesNetworkMember {

    private static final String TARGET_KEY = "Target";
    private static final String SIGNAL_KEY = "Signal";
    private static final int REFRESH_INTERVAL = 40;

    private String target = "";
    private int signal;
    private int appliedSignal = -1;
    private int refreshCooldown;

    public WireReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(DriveByDashPanels.WIRE_RECEIVER_ENTITY.get(), pos, state);
    }

    public String getTarget() {
        return target;
    }

    public int getSignal() {
        return signal;
    }

    public void setTarget(String newTarget) {
        String cleaned = newTarget == null ? "" : newTarget.trim();
        if (cleaned.equals(this.target)) {
            return;
        }
        this.target = cleaned;
        this.appliedSignal = -1;
        this.setChanged();
        this.blockChanged();
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        super.tick(level, blockPos, blockState);
        if (level.isClientSide() || !WireBridge.isAvailable()) {
            return;
        }

        int received = 0;
        for (Direction side : Direction.values()) {
            received = Math.max(received, WireBridge.getSignal(level, blockPos, side));
        }

        if (received != this.signal) {
            this.signal = received;
            this.setChanged();
            this.blockChanged();
        }

        boolean refresh = --refreshCooldown <= 0;
        if (refresh) {
            refreshCooldown = REFRESH_INTERVAL;
        }
        if (received != appliedSignal || refresh) {
            if (applySignal(received)) {
                appliedSignal = received;
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString(TARGET_KEY, target);
        tag.putInt(SIGNAL_KEY, signal);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.target = tag.getString(TARGET_KEY);
        this.signal = WireBridge.clamp(tag.getInt(SIGNAL_KEY));
        this.appliedSignal = -1;
    }

    private boolean applySignal(int value) {
        if (target.isEmpty()) {
            return false;
        }

        ModulesNetwork network = this.getOrCreate();
        if (network == null) {
            return false;
        }
        if (network.getCompiledModules() == null) {
            network.compileModules();
        }
        ModuleMap modules = network.getCompiledModules();
        if (modules == null) {
            return false;
        }

        int separator = target.lastIndexOf(WireTransmitterBlockEntity.ENTRY_SEPARATOR);
        String moduleName = separator < 0 ? target : target.substring(0, separator);
        String extension = separator < 0 ? null : target.substring(separator + 1);

        Module module = modules.normalGet(moduleName);
        if (module == null) {
            return false;
        }

        boolean applied = false;
        if (extension == null) {
            if (module instanceof IOutput output) {
                output.setAnalog(value);
                applied = true;
            }
        } else if (module instanceof IMultiOutput multiOutput) {
            Map<String, IMultiOutput.AnalogRunnable> entries = new HashMap<>();
            multiOutput.setValues(entries::put);
            IMultiOutput.AnalogRunnable entry = entries.get(extension);
            if (entry != null) {
                entry.setAnalog(value);
                applied = true;
            }
        }

        if (!applied) {
            return false;
        }

        AbstractPanelBlockEntity owner = module.parentBlockEntity;
        if (owner != null) {
            owner.setChanged();
            ModulesNetwork ownerNetwork = owner.getOrCreate();
            if (ownerNetwork != null) {
                owner.networkUpdate(ownerNetwork);
            }
        }
        return true;
    }
}
