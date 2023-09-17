package earth.terrarium.techarium.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class BeamBlock extends Block {
    public static final EnumProperty<BeamType> TYPE = EnumProperty.create("type", BeamType.class);

    public static final VoxelShape SINGLE = Shapes.join(
        Shapes.join(Block.box(2, 15, 2, 14, 16, 14),
            Block.box(4, 1, 4, 12, 15, 12), BooleanOp.OR),
        Block.box(2, 0, 2, 14, 1, 14), BooleanOp.OR);

    public static final VoxelShape UPPER = Stream.of(
        Block.box(5, 0, 5, 11, 7, 11),
        Block.box(2, 15, 2, 14, 16, 14),
        Block.box(4, 7, 4, 12, 15, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static final VoxelShape MIDDLE = Block.box(5, 0, 5, 11, 16, 11);

    public static final VoxelShape LOWER = Stream.of(
        Block.box(5, 9, 5, 11, 16, 11),
        Block.box(2, 0, 2, 14, 1, 14),
        Block.box(4, 1, 4, 12, 9, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public BeamBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(TYPE, BeamType.MIDDLE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(TYPE)) {
            case SINGLE -> SINGLE;
            case UPPER -> UPPER;
            case MIDDLE -> MIDDLE;
            case LOWER -> LOWER;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return update(context.getLevel(), defaultBlockState(), context.getClickedPos());
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        level.setBlockAndUpdate(pos, update(level, state, pos));
    }

    public BlockState update(Level level, BlockState state, BlockPos pos) {
        boolean above = level.getBlockState(pos.above()).getBlock() instanceof BeamBlock;
        boolean below = level.getBlockState(pos.below()).getBlock() instanceof BeamBlock;

        BlockState blockState = state;
        if (above && below) blockState = blockState.setValue(TYPE, BeamType.MIDDLE);
        else if (above) blockState = blockState.setValue(TYPE, BeamType.LOWER);
        else if (below) blockState = blockState.setValue(TYPE, BeamType.UPPER);
        else blockState = blockState.setValue(TYPE, BeamType.SINGLE);

        return blockState;
    }

    public enum BeamType implements StringRepresentable {
        SINGLE,
        UPPER,
        MIDDLE,
        LOWER,
        ;

        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}