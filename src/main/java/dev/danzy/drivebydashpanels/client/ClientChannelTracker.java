package dev.danzy.drivebydashpanels.client;

import dev.danzy.drivebydashpanels.DriveByDashPanels;
import dev.danzy.drivebydashpanels.content.WireTransmitterBlockEntity;
import dev.danzy.drivebydashpanels.wire.ChannelSelection;

import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

/**
 * Remembers which transmitter the player last clicked so the block can answer Drive-By-Wire's
 * channel cycling with that transmitter's module list.
 */
@EventBusSubscriber(modid = DriveByDashPanels.MOD_ID, value = Dist.CLIENT)
public final class ClientChannelTracker {

    private ClientChannelTracker() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (!level.isClientSide()) {
            return;
        }

        if (level.getBlockEntity(event.getPos()) instanceof WireTransmitterBlockEntity transmitter) {
            ChannelSelection.set(transmitter.getChannels());
        } else {
            ChannelSelection.clear();
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        ChannelSelection.clear();
    }
}
