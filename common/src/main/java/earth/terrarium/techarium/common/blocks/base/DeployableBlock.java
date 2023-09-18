package earth.terrarium.techarium.common.blocks.base;

import earth.terrarium.techarium.common.blocks.base.DoubleMachineBlock;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.Locale;

public abstract class DeployableBlock extends DoubleMachineBlock {
    public static final EnumProperty<DeployState> DEPLOYED = EnumProperty.create("deployed", DeployState.class);

    public DeployableBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(POWERED, false)
            .setValue(LIT, false)
            .setValue(HALF, DoubleBlockHalf.LOWER)
            .setValue(DEPLOYED, DeployState.NOT_DEPLOYED));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DEPLOYED);
    }

    public enum DeployState implements StringRepresentable {
        NOT_DEPLOYED,
        PARTIALLY_DEPLOYED,
        DEPLOYED,
        ;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public boolean isDeployed() {
            return this == DEPLOYED;
        }

        public boolean isPartiallyDeployed() {
            return this == PARTIALLY_DEPLOYED || this == DEPLOYED;
        }
    }
}
