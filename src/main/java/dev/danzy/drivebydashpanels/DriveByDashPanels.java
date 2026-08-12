package dev.danzy.drivebydashpanels;

import com.mojang.logging.LogUtils;

import dev.danzy.drivebydashpanels.content.WireReceiverBlock;
import dev.danzy.drivebydashpanels.content.WireReceiverBlockEntity;
import dev.danzy.drivebydashpanels.content.WireTransmitterBlock;
import dev.danzy.drivebydashpanels.content.WireTransmitterBlockEntity;
import dev.danzy.drivebydashpanels.wire.WireBridge;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import org.slf4j.Logger;

/**
 * Drive by DashPanels: turns every Dashpanels module into a Drive-By-Wire channel and back.
 */
@Mod(DriveByDashPanels.MOD_ID)
public final class DriveByDashPanels {

    public static final String MOD_ID = "drivebydashpanels";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredBlock<WireTransmitterBlock> WIRE_TRANSMITTER = BLOCKS.register(
            "wire_transmitter",
            () -> new WireTransmitterBlock(bridgeProperties(MapColor.COLOR_LIGHT_GRAY)));

    public static final DeferredBlock<WireReceiverBlock> WIRE_RECEIVER = BLOCKS.register(
            "wire_receiver",
            () -> new WireReceiverBlock(bridgeProperties(MapColor.COLOR_BLUE)));

    public static final DeferredItem<BlockItem> WIRE_TRANSMITTER_ITEM =
            ITEMS.registerSimpleBlockItem(WIRE_TRANSMITTER);
    public static final DeferredItem<BlockItem> WIRE_RECEIVER_ITEM =
            ITEMS.registerSimpleBlockItem(WIRE_RECEIVER);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WireTransmitterBlockEntity>>
            WIRE_TRANSMITTER_ENTITY = BLOCK_ENTITIES.register(
            "wire_transmitter",
            () -> BlockEntityType.Builder.of(WireTransmitterBlockEntity::new, WIRE_TRANSMITTER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WireReceiverBlockEntity>>
            WIRE_RECEIVER_ENTITY = BLOCK_ENTITIES.register(
            "wire_receiver",
            () -> BlockEntityType.Builder.of(WireReceiverBlockEntity::new, WIRE_RECEIVER.get()).build(null));

    @SuppressWarnings("unused")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register(
            "main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MOD_ID))
                    .icon(() -> WIRE_TRANSMITTER_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(WIRE_TRANSMITTER_ITEM.get());
                        output.accept(WIRE_RECEIVER_ITEM.get());
                    })
                    .build());

    public DriveByDashPanels(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> LOGGER.info(
                "Drive by DashPanels loaded. Drive-By-Wire network API bound: {}", WireBridge.isAvailable()));
    }

    private static BlockBehaviour.Properties bridgeProperties(MapColor mapColor) {
        return BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .strength(2.0F, 4.0F)
                .sound(SoundType.COPPER)
                .requiresCorrectToolForDrops();
    }
}
