package earth.terrarium.techarium.block.singleblock;

import earth.terrarium.techarium.block.entity.singleblock.GravMagnetBlockEntity;
import earth.terrarium.techarium.block.entity.singleblock.MagneticAcceleratorBlockEntity;
import earth.terrarium.techarium.registry.TechariumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class MagneticAcceleratorBlock extends Block implements EntityBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public MagneticAcceleratorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.FACING, Direction.NORTH));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.POWERED, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MagneticAcceleratorBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
        builder.add(BlockStateProperties.POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() == null){
            return TechariumBlocks.MAGNETIC_ACCELERATOR.get().defaultBlockState();
        }
        Direction dir = context.getPlayer().isShiftKeyDown() ? context.getNearestLookingDirection() : context.getNearestLookingDirection().getOpposite();
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(BlockStateProperties.FACING, dir).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    VoxelShape NORTH = Stream.of(
            Block.box(1, 15, 0, 15, 16, 16),
            Block.box(0, 0, 0, 1, 16, 16),
            Block.box(15, 0, 0, 16, 16, 16),
            Block.box(1, 0, 0, 15, 1, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    VoxelShape EAST = Stream.of(
            Block.box(0, 15, 1, 16, 16, 15),
            Block.box(0, 0, 0, 16, 16, 1),
            Block.box(0, 0, 15, 16, 16, 16),
            Block.box(0, 0, 1, 16, 1, 15)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    VoxelShape UP = Stream.of(
            Block.box(0, 0, 1, 1, 16, 15),
            Block.box(0, 0, 0, 16, 16, 1),
            Block.box(0, 0, 15, 16, 16, 16),
            Block.box(15, 0, 1, 16, 16, 15)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getVoxelShape(state.getValue(BlockStateProperties.FACING));
    }

    VoxelShape getVoxelShape(Direction facing) {
        return switch (facing) {
            case SOUTH, NORTH -> NORTH;
            case WEST, EAST -> EAST;
            case DOWN, UP -> UP;
        };
    }

    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public void neighborChanged(@NotNull BlockState state, Level world, @NotNull BlockPos pos1, @NotNull Block block, @NotNull BlockPos pos2, boolean f) {
        if (!world.isClientSide()) {
            Boolean flag = world.hasNeighborSignal(pos1);
            world.setBlockAndUpdate(pos1, state.setValue(POWERED, flag));
        }
    }

    @Override
    public void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        entity.setSwimming(true);
        ((MagneticAcceleratorBlockEntity) Objects.requireNonNull(level.getBlockEntity(pos))).pushPullEntities(entity);
    }
}
