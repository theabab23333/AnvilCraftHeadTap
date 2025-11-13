package me.theabab2333.headtap.event;


import me.theabab2333.headtap.init.block.ModBlockEntities;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.api.itemhandler.ResinCauldronWrapper;
import me.theabab2333.headtap.block.entity.BuilderBlockEntity;
import me.theabab2333.headtap.block.entity.DistributorBlockEntity;
import me.theabab2333.headtap.block.entity.PassiveRoyalAnvilBlockEntity;
import me.theabab2333.headtap.block.entity.PassiveRoyalGrindstoneBlockEntity;
import me.theabab2333.headtap.block.entity.PassiveRoyalSmithingTableBlockEntity;
import me.theabab2333.headtap.block.entity.PrinterBlockEntity;
import me.theabab2333.headtap.block.entity.ResinExtractorBlockEntity;
import me.theabab2333.headtap.block.entity.StoneGeneratorBlockEntity;
import me.theabab2333.headtap.block.entity.VariableFluidTankBlockEntity;
import me.theabab2333.headtap.init.block.ModBlocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;

@EventBusSubscriber(modid = HeadTap.MOD_ID)
public class CapabilitiesEventListener {

    // 从Create那边学的 个人感觉好看

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        StoneGeneratorBlockEntity.registerCapabilities(event);
        ResinExtractorBlockEntity.registerCapabilities(event);
        VariableFluidTankBlockEntity.registerCapabilities(event);
        PassiveRoyalGrindstoneBlockEntity.registerCapabilities(event);
        PassiveRoyalAnvilBlockEntity.registerCapabilities(event);
        PassiveRoyalSmithingTableBlockEntity.registerCapabilities(event);
        PrinterBlockEntity.registerCapabilities(event);
        BuilderBlockEntity.registerCapabilities(event);
        DistributorBlockEntity.registerCapabilities(event);

        List.of(
            dev.dubhe.anvilcraft.init.block.ModBlockEntities.CRAB_TRAP.get(),
            ModBlockEntities.ARTIFICIAL_HIGH_TEMPERATURE_DEVICE.get()
        ).forEach(type -> event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            type,
            (be, side) -> be.getItemHandler())
        );

        // Other
        event.registerBlock(
            Capabilities.ItemHandler.BLOCK,
            ((level, pos, state, blockEntity, side) -> new ResinCauldronWrapper(level, pos)),
            ModBlocks.RESIN_FLUID_CAULDRON.get());
    }
}
