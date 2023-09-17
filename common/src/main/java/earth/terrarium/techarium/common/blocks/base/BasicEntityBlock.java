package earth.terrarium.techarium.common.blocks.base;

import com.teamresourceful.resourcefullib.common.caches.CacheableFunction;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.techarium.common.registry.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BasicEntityBlock extends BaseEntityBlock {
    private static final CacheableFunction<Block, BlockEntityType<?>> BLOCK_TO_ENTITY = new CacheableFunction<>(block ->
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES
            .stream()
            .filter(type -> !(type.equals(ModBlockEntityTypes.MACHINE_EXTENSION)))
            .map(RegistryEntry::get)
            .filter(type -> type.isValid(block.defaultBlockState()))
            .findFirst()
            .orElse(null)
    );
    private BlockEntityType<?> entity;

    public BasicEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return entity(state).create(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public BlockEntityType<?> entity(BlockState state) {
        if (this.entity == null) {
            this.entity = BLOCK_TO_ENTITY.apply(state.getBlock());
        }
        return this.entity;
    }
}
