package com.techarium.techarium;

import com.techarium.techarium.platform.CommonServices;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumBlocks;
import com.techarium.techarium.registry.TechariumItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class Techarium {

    public static final String MOD_ID = "techarium";
    public static final String MOD_NAME = "Techarium";
    public static CreativeModeTab TAB = CommonServices.REGISTRY.registerCreativeTab(new ResourceLocation(MOD_ID, "tab"), () -> new ItemStack(Blocks.DIAMOND_BLOCK));

    public static void init() {
        TechariumBlocks.register();
        TechariumItems.register();
        TechariumBlockEntities.register();
    }

}