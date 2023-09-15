package earth.terrarium.techarium.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.techarium.Techarium;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntityTypes {
    public static final ResourcefulRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Techarium.MOD_ID);
}
