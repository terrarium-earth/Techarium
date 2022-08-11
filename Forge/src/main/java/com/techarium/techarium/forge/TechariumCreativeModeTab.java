package com.techarium.techarium.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

// FIXME this shouldn't exist but the class processor has an oversight where it doesn't move inner classes.
public class TechariumCreativeModeTab extends CreativeModeTab {
    private final Supplier<ItemStack> icon;

    public TechariumCreativeModeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
        super(tab.getNamespace() + "." + tab.getPath());

        this.icon = icon;
    }

    @Override
    public ItemStack makeIcon() {
        return icon.get();
    }
}
