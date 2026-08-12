package dev.danzy.drivebydashpanels.content;

import java.util.List;

import dev.danzy.drivebydashpanels.wire.ChannelSelection;
import dev.danzy.drivebydashpanels.wire.WireBridge;

import edn.stratodonut.drivebywire.wire.MultiChannelWireSource;

import moth.boxxed.panels.api.network.ModulesNetworkMemberBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Panel Wire Transmitter: a Dashpanels network member that publishes every panel input as a
 * Drive-By-Wire channel. Aim at it with the wire, scroll to pick the module, then click the target.
 */
public class WireTransmitterBlock extends ModulesNetworkMemberBlock implements MultiChannelWireSource {

    public WireTransmitterBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isConnecting(LevelReader level, BlockPos pos, BlockState state, Direction face) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WireTransmitterBlockEntity(pos, state);
    }

    @Override
    public List<String> wire$getChannels() {
        List<String> channels = ChannelSelection.get();
        return channels.isEmpty() ? List.of(WireBridge.WORLD_CHANNEL) : channels;
    }

    @Override
    public String wire$nextChannel(String current, boolean forward) {
        List<String> channels = wire$getChannels();
        int index = channels.indexOf(current);
        if (index < 0) {
            return channels.get(0);
        }
        return channels.get(Math.floorMod(index + (forward ? 1 : -1), channels.size()));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                            BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof WireTransmitterBlockEntity transmitter) {
            List<String> channels = transmitter.getChannels();
            if (channels.isEmpty()) {
                player.displayClientMessage(Component.translatable("drivebydashpanels.message.no_channels"), false);
            } else {
                player.displayClientMessage(Component.translatable(
                        "drivebydashpanels.message.channels", channels.size(), String.join(", ", channels)), false);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
