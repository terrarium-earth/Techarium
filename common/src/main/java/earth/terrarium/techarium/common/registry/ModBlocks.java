package earth.terrarium.techarium.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.techarium.Techarium;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public class ModBlocks {
    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, Techarium.MOD_ID);

    public static final ResourcefulRegistry<Block> MACHINES = ResourcefulRegistries.create(BLOCKS);
    public static final ResourcefulRegistry<Block> FLUIDS = ResourcefulRegistries.create(BLOCKS);
    public static final ResourcefulRegistry<Block> CUBES = ResourcefulRegistries.create(BLOCKS);
}
