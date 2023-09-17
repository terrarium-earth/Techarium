package earth.terrarium.techarium.common.blocks.machines;

import earth.terrarium.techarium.common.blockentities.base.DeployableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class BotariumBlock extends DeployableBlock {

    public static final VoxelShape BOTTOM_SHAPE = Stream.of(
        Block.box(1, 0, 1, 15, 4, 15),
        Shapes.join(Block.box(2, 4, 2, 14, 7, 14),
            Block.box(3, 7, 3, 13, 10, 13), BooleanOp.OR),
        Block.box(3.25, 10, 3.25, 12.75, 16, 12.75)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static final VoxelShape TOP_SHAPE = Shapes.join(
        Shapes.join(Block.box(2, 11, 2, 14, 13, 14),
            Block.box(1, 13, 1, 15, 16, 15), BooleanOp.OR),
        Block.box(3.25, 0, 3.25, 12.75, 11, 12.75), BooleanOp.OR);

    public BotariumBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return BOTTOM_SHAPE;
        } else {
            BlockState belowState = level.getBlockState(pos.below());
            if (belowState.getBlock() instanceof BotariumBlock && belowState.getValue(DEPLOYED).isPartiallyDeployed()) {
                return TOP_SHAPE;
            }
            return Shapes.empty();
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
