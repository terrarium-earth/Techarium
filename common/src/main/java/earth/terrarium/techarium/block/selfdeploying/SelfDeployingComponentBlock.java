package earth.terrarium.techarium.block.selfdeploying;

import earth.terrarium.techarium.block.entity.selfdeploying.SelfDeployingComponentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A simple black used to fill the air and proxy its call to its controller.
 */
public class SelfDeployingComponentBlock extends Block implements EntityBlock {

    public SelfDeployingComponentBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SelfDeployingComponentBlockEntity selfDeployingComponentBlockEntity) {
            return selfDeployingComponentBlockEntity.onUse(state, level, pos, player, hand, hit);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, blockEntity, stack);
        if (blockEntity instanceof SelfDeployingComponentBlockEntity selfDeployingComponentBlockEntity) {
            selfDeployingComponentBlockEntity.playerDestroy(level, player, pos, state, blockEntity, stack);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SelfDeployingComponentBlockEntity(pos, state);
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        // we don't want particles
    }

}
