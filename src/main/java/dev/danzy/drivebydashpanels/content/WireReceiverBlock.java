package dev.danzy.drivebydashpanels.content;

import moth.boxxed.panels.api.network.ModulesNetworkMemberBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Panel Wire Receiver: takes the signal arriving from a Drive-By-Wire connection and pushes it into
 * one output module of the attached panel network.
 *
 * <p>The target module is picked by name: place the block from an anvil renamed item, or right click
 * it with a renamed name tag. Use {@code module/entry} to address a single entry of a multi output
 * module.</p>
 */
public class WireReceiverBlock extends ModulesNetworkMemberBlock {

    public WireReceiverBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isConnecting(LevelReader level, BlockPos pos, BlockState state, Direction face) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WireReceiverBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        Component customName = stack.get(DataComponents.CUSTOM_NAME);
        if (customName != null && level.getBlockEntity(pos) instanceof WireReceiverBlockEntity receiver) {
            receiver.setTarget(customName.getString());
        }
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                           InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.NAME_TAG)) {
            Component customName = stack.get(DataComponents.CUSTOM_NAME);
            if (customName != null) {
                if (!level.isClientSide() && level.getBlockEntity(pos) instanceof WireReceiverBlockEntity receiver) {
                    receiver.setTarget(customName.getString());
                    player.displayClientMessage(Component.translatable(
                            "drivebydashpanels.message.target_set", receiver.getTarget()), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                            BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof WireReceiverBlockEntity receiver) {
            if (receiver.getTarget().isEmpty()) {
                player.displayClientMessage(Component.translatable("drivebydashpanels.message.no_target"), false);
            } else {
                player.displayClientMessage(Component.translatable(
                        "drivebydashpanels.message.target", receiver.getTarget(), receiver.getSignal()), false);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
