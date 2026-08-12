package dev.danzy.drivebydashpanels.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.danzy.drivebydashpanels.DriveByDashPanels;
import dev.danzy.drivebydashpanels.wire.WireBridge;

import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.ModuleMap;
import moth.boxxed.panels.api.module.io.IInput;
import moth.boxxed.panels.api.module.io.IMultiInput;
import moth.boxxed.panels.api.network.ModulesNetwork;
import moth.boxxed.panels.api.network.ModulesNetworkMember;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Reads every input module of the attached panel network and publishes it on the wire network,
 * one channel per module. Multi input modules (joysticks, steering wheels, ...) expose one channel
 * per axis, named {@code module/axis}.
 */
public class WireTransmitterBlockEntity extends ModulesNetworkMember {

    /** Separator between a module name and a multi input/output entry. */
    public static final char ENTRY_SEPARATOR = '/';

    private static final String CHANNELS_KEY = "Channels";
    private static final int RECOMPILE_INTERVAL = 20;

    private final Map<String, Integer> publishedSignals = new LinkedHashMap<>();
    private List<String> channels = List.of();
    private int recompileCooldown;

    public WireTransmitterBlockEntity(BlockPos pos, BlockState state) {
        super(DriveByDashPanels.WIRE_TRANSMITTER_ENTITY.get(), pos, state);
    }

    /** Channel names this transmitter currently offers, synced to the client for the wire tool. */
    public List<String> getChannels() {
        return channels;
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        super.tick(level, blockPos, blockState);
        if (level.isClientSide() || !WireBridge.isAvailable()) {
            return;
        }

        ModulesNetwork network = this.getOrCreate();
        if (network == null) {
            return;
        }

        if (--recompileCooldown <= 0 || network.getCompiledModules() == null) {
            recompileCooldown = RECOMPILE_INTERVAL;
            network.compileModules();
        }

        ModuleMap modules = network.getCompiledModules();
        if (modules == null) {
            return;
        }

        Map<String, Integer> signals = collectInputSignals(modules);
        publish(level, blockPos, signals);
        updateChannels(signals.keySet());
    }

    @Override
    public void remove() {
        Level level = this.getLevel();
        if (level != null && !level.isClientSide()) {
            for (String channel : publishedSignals.keySet()) {
                WireBridge.setSignal(level, this.getBlockPos(), channel, 0);
            }
            publishedSignals.clear();
        }
        super.remove();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag list = new ListTag();
        for (String channel : channels) {
            list.add(StringTag.valueOf(channel));
        }
        tag.put(CHANNELS_KEY, list);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        List<String> loaded = new ArrayList<>();
        for (Tag entry : tag.getList(CHANNELS_KEY, Tag.TAG_STRING)) {
            String channel = entry.getAsString();
            if (!channel.isEmpty()) {
                loaded.add(channel);
            }
        }
        this.channels = List.copyOf(loaded);
    }

    private static Map<String, Integer> collectInputSignals(ModuleMap modules) {
        Map<String, Integer> signals = new LinkedHashMap<>();
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            String name = entry.getKey();
            Module module = entry.getValue();
            if (name == null || name.isEmpty() || module == null) {
                continue;
            }

            if (module instanceof IInput input) {
                signals.put(name, WireBridge.clamp(input.getAnalog()));
            }
            if (module instanceof IMultiInput multiInput) {
                multiInput.getValues((extension, result) -> {
                    if (extension == null || extension.isEmpty() || result == null) {
                        return;
                    }
                    signals.put(name + ENTRY_SEPARATOR + extension, WireBridge.clamp(result.getAnalog()));
                });
            }
        }
        return signals;
    }

    private void publish(Level level, BlockPos pos, Map<String, Integer> signals) {
        for (Map.Entry<String, Integer> entry : signals.entrySet()) {
            Integer previous = publishedSignals.get(entry.getKey());
            if (previous == null || !previous.equals(entry.getValue())) {
                WireBridge.setSignal(level, pos, entry.getKey(), entry.getValue());
                publishedSignals.put(entry.getKey(), entry.getValue());
            }
        }

        Iterator<Map.Entry<String, Integer>> stale = publishedSignals.entrySet().iterator();
        while (stale.hasNext()) {
            Map.Entry<String, Integer> entry = stale.next();
            if (!signals.containsKey(entry.getKey())) {
                WireBridge.setSignal(level, pos, entry.getKey(), 0);
                stale.remove();
            }
        }
    }

    private void updateChannels(Set<String> names) {
        List<String> updated = List.copyOf(names);
        if (updated.equals(this.channels)) {
            return;
        }
        this.channels = updated;
        this.setChanged();
        this.blockChanged();
    }
}
