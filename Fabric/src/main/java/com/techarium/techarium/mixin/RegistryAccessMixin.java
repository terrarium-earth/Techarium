package com.techarium.techarium.mixin;

import com.google.common.collect.ImmutableMap;
import com.techarium.techarium.multiblock.MultiBlockStructure;
import com.techarium.techarium.platform.FabricRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RegistryAccess.class)
public interface RegistryAccessMixin {

	@ModifyVariable(method = "method_30531()Lcom/google/common/collect/ImmutableMap;", at = @At("STORE"), ordinal = 0)
	private static ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> swapMapV2(ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> builder) {
		// add our own datapack registries to the RegistryAccess.REGISTRIES in the lamda.
		builder.put(FabricRegistryHelper.MULTIBLOCK_STRUCTURES.key(), new RegistryAccess.RegistryData<>(FabricRegistryHelper.MULTIBLOCK_STRUCTURES.key(), MultiBlockStructure.CODEC, null));
		return builder;
	}

}
