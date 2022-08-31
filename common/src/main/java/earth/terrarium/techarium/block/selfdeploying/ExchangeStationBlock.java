package earth.terrarium.techarium.block.selfdeploying;

import earth.terrarium.techarium.block.entity.selfdeploying.ExchangeStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Material;

public class ExchangeStationBlock extends SelfDeployingBlock {

    public ExchangeStationBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExchangeStationBlockEntity(pos, state);
    }

//	@Nullable
//	@Override
//	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
//		// implement tick if useful
//		return null;
//	}

    @Override
    public BoundingBox getDeployedSize() {
        // TODO @Ashley: 07/06/2022 replace 3 by 2 when the tests (obstruction overlays) are ok
        return new BoundingBox(0, 0, 0, 1, 3, 1);
    }

}
