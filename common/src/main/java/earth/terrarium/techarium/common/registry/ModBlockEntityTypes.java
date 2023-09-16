package earth.terrarium.techarium.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.botarium.common.registry.RegistryHelpers;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.blockentities.machines.BotariumBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntityTypes {
    public static final ResourcefulRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Techarium.MOD_ID);

    public static final RegistryEntry<BlockEntityType<BotariumBlockEntity>> BOTARIUM = BLOCK_ENTITY_TYPES.register(
        "botarium",
        () -> RegistryHelpers.createBlockEntityType(
            BotariumBlockEntity::new,
            ModBlocks.BOTARIUM.get()));
}
