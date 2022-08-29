package earth.terrarium.techarium.block.entity.selfdeploying;

import earth.terrarium.techarium.registry.TechariumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A self-deploying block formed by a multiblock and convert back to it when destroyed.
 */
public abstract class SelfDeployingMultiblockBlockEntity extends SelfDeployingBlockEntity.WithContainer {

    Map<BlockPos, BlockState> states;

    public SelfDeployingMultiblockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void undeploy(boolean removeSelf, boolean restoreMultiblock, BlockState oldState, BlockPos initiator) {
        super.undeploy(removeSelf, restoreMultiblock, oldState, initiator);
        if (this.states != null && this.level != null) {
            Direction direction = oldState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            for (Map.Entry<BlockPos, BlockState> entry : this.states.entrySet()) {
                level.setBlock(entry.getKey(), entry.getValue(), 3);
                if (!restoreMultiblock) {
                    level.destroyBlock(entry.getKey(), true);
                }
            }
            level.setBlock(worldPosition, TechariumBlocks.MACHINE_CORE.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction), 3);
            if (!restoreMultiblock) {
                level.destroyBlock(worldPosition, true);
            }
            level.destroyBlock(initiator, true);
        }
    }

    /**
     * Set the blockstates used in the multiblock in case someone want to revert it.
     * The positions are assumed to be the real in-world position
     *
     * @param states a map of the blockstates at each positions of the multiblock
     */
    public void setMultiblockState(Map<BlockPos, BlockState> states) {
        this.states = states;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, BlockState> entry : states.entrySet()) {
            CompoundTag element = new CompoundTag();
            element.put("pos", NbtUtils.writeBlockPos(entry.getKey()));
            element.put("state", NbtUtils.writeBlockState(entry.getValue()));
            list.add(element);
        }
        tag.put("states", list);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        ListTag list = tag.getList("states", Tag.TAG_COMPOUND);
        int size = list.size();
        this.states = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            CompoundTag element = list.getCompound(i);
            states.put(NbtUtils.readBlockPos(element.getCompound("pos")), NbtUtils.readBlockState(element.getCompound("state")));
        }
    }

}
