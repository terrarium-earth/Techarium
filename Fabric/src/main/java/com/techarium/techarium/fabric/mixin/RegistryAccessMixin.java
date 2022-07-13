package com.techarium.techarium.fabric.mixin;

import com.google.common.collect.ImmutableMap;
import com.techarium.techarium.multiblock.MultiBlockStructure;
import com.techarium.techarium.fabric.platform.FabricRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RegistryAccess.class)
public interface RegistryAccessMixin {

	@Inject(method = "method_30531", at = @At(target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;", value = "INVOKE"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void beforeMapBuild(CallbackInfoReturnable<ImmutableMap<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>>> cir, ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> builder) {
		builder.put(FabricRegistryHelper.MULTIBLOCK_STRUCTURES.key(), new RegistryAccess.RegistryData<>(FabricRegistryHelper.MULTIBLOCK_STRUCTURES.key(), MultiBlockStructure.CODEC, null));
	}

}
