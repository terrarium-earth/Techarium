package earth.terrarium.techarium.block.singleblock;

import earth.terrarium.techarium.block.entity.singleblock.GravMagnetBlockEntity;
import earth.terrarium.techarium.registry.TechariumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class GravMagnetBlock extends Block implements EntityBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public GravMagnetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.FACING, Direction.NORTH));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.POWERED, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GravMagnetBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
        builder.add(BlockStateProperties.POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() == null){
            return TechariumBlocks.GRAVMAGNET.get().defaultBlockState();
        }
        Direction dir = context.getPlayer().isShiftKeyDown() ? context.getNearestLookingDirection() : context.getNearestLookingDirection().getOpposite();
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(BlockStateProperties.FACING, dir).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch(state.getValue(BlockStateProperties.FACING)){
            case SOUTH -> Shapes.box(0, 0, 0, 1, 1, 0.9);
            case NORTH -> Shapes.box(0, 0, 0.1, 1, 1, 1);
            case EAST -> Shapes.box(0, 0, 0, 0.9, 1, 1);
            case WEST -> Shapes.box(0.1, 0, 0, 1, 1, 1);
            case UP -> Shapes.box(0, 0, 0, 1, 0.9, 1);
            case DOWN -> Shapes.box(0, 0.1, 0, 1, 1, 1);
        };
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (pLevel1, pPos, pState1, pBlockEntity) -> ((GravMagnetBlockEntity) pBlockEntity).tick();
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
}
