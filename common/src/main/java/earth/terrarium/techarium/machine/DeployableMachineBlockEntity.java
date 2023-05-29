package earth.terrarium.techarium.machine;

import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.SimpleUpdatingEnergyContainer;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import earth.terrarium.botarium.api.fluid.FluidHoldingBlock;
import earth.terrarium.botarium.api.fluid.UpdatingFluidContainer;
import earth.terrarium.botarium.api.item.ItemContainerBlock;
import earth.terrarium.botarium.api.item.SerializableContainer;
import earth.terrarium.botarium.api.item.SimpleItemContainer;
import earth.terrarium.techarium.block.entity.selfdeploying.SelfDeployingComponentBlockEntity;
import earth.terrarium.techarium.block.selfdeploying.SelfDeployingComponentBlock;
import earth.terrarium.techarium.inventory.ExtraDataMenuProvider;
import earth.terrarium.techarium.machine.definition.MachineDefinition;
import earth.terrarium.techarium.machine.definition.MachineModuleDefinition;
import earth.terrarium.techarium.machine.module.ModuleEnergyContainer;
import earth.terrarium.techarium.machine.module.ModuleFluidContainer;
import earth.terrarium.techarium.registry.RegistryHelper;
import earth.terrarium.techarium.registry.TechariumBlockEntities;
import earth.terrarium.techarium.registry.TechariumBlocks;
import earth.terrarium.techarium.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeployableMachineBlockEntity extends BlockEntity implements ExtraDataMenuProvider/*, ItemContainerBlock, FluidHoldingBlock, EnergyBlock*/ {

    private List<BlockPos> components = new ArrayList<>();
//    private SimpleUpdatingEnergyContainer energyContainer = new SimpleUpdatingEnergyContainer(this, 0);
//    private SerializableContainer itemContainer = new SimpleItemContainer(this, 0);
//    private ModuleFluidContainer fluidContainer = new ModuleFluidContainer(this, 0);
    private MachineDefinition machineDefinition;
    private ResourceLocation machineId;

    public DeployableMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TechariumBlockEntities.DEPLOYABLE_MACHINE.get(), blockPos, blockState);
        this.machineId = null;
    }

    public void setupMachine(String machineId) {
        this.machineId = new ResourceLocation(machineId);
        this.machineDefinition = level.registryAccess().registry(RegistryHelper.getMachineDefinitionRegistryKey()).get().get(this.machineId);
        // set components
        List<Integer> size = this.machineDefinition.size();
        for (int x = 0; x < size.get(0); x++) {
            for (int y = 0; y < size.get(1); y++) {
                for (int z = 0; z < size.get(2); z++) {
                    this.components.add(this.worldPosition.offset(x, y, z));
                }
            }
        }
//        long fluidModuleAmount = machineDefinition.moduleDefinitions().stream().filter(def -> def.type() == MachineModuleDefinition.ModuleType.FLUID).count();
//        this.fluidContainer = new ModuleFluidContainer(this, (int) fluidModuleAmount);
//        int fluidCounter = 0;
//        for (MachineModuleDefinition moduleDefinition : machineDefinition.moduleDefinitions()) {
//            switch (moduleDefinition.type()) {
//                case ENERGY -> this.energyContainer = new ModuleEnergyContainer(this, moduleDefinition.capacity(), moduleDefinition.inputRate(), moduleDefinition.outputRate());
//                case ITEM -> this.itemContainer = new SimpleItemContainer(this, moduleDefinition.capacity());
//                case FLUID -> this.fluidContainer.setTankCapacity(fluidCounter++, moduleDefinition.capacity());
//            }
//        }
    }

    /**
     * Deploy the block.
     * Default implementation place the component blocks.
     */
    public void deploy() {
        if (this.level != null) {
            for (BlockPos pos : this.components) {
                this.level.setBlock(pos, TechariumBlocks.SELF_DEPLOYING_COMPONENT.get().defaultBlockState(), 3);
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof SelfDeployingComponentBlockEntity componentBlockEntity) {
                    componentBlockEntity.setControllerPosition(this.worldPosition);
                }
            }
        }
    }

    /**
     * Undeploy the block.
     * Default implementation remove the component blocks alongside the controller.
     *
     * @param removeSelf        determine if this block should remove itself
     * @param restoreMultiblock determine if the multiblock associated (if any) should be restored in the world.
     * @param oldState          the state of the core block before it was removed
     * @param initiator         the position of the block that initiated the removal of the self-deployed block
     */
    public void undeploy(boolean removeSelf, boolean restoreMultiblock, BlockState oldState, BlockPos initiator) {
        if (level != null) {
            this.level.removeBlockEntity(this.worldPosition);  // state changed but not yet the block entity so we do it now
            if (removeSelf) {
                // called from Block#onRemove, the state have already been changed. This is mainly used for when it is called from elsewhere.
                level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3);
            }
            // TODO @Ketheroth: 11/06/2022 drop content if there is an inventory
            for (BlockPos pos : components) {
                if (level.getBlockState(pos).getBlock() instanceof SelfDeployingComponentBlock) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    /**
     * Called when the associated block is used.
     */
    public InteractionResult onUse(Player player, InteractionHand hand) {
        System.out.println("deployable machine used : " + this.machineId);
//        System.out.println("energy " + this.energyContainer.getStoredEnergy() + "/" + this.energyContainer.getMaxCapacity());
//        System.out.println("item " + this.itemContainer.getContainerSize());
//        System.out.println("fluid " + this.fluidContainer.getSize());
        return InteractionResult.SUCCESS;
    }

    @Override
    public void writeExtraData(ServerPlayer player, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.worldPosition);
    }

    @Override
    public Component getDisplayName() {
        return Utils.translatableComponent("container.techarium.deployable_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new DeployableMachineMenu(id, inventory, player, this.worldPosition);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("MachineId", this.machineId == null? "" : this.machineId.toString());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.setupMachine(tag.getString("MachineId"));
    }

    public ResourceLocation getMachineId() {
        return this.machineId;
    }

    //    @Override
//    public StatefulEnergyContainer<BlockEntity> getEnergyStorage() {
//        if(energyContainer == null) {
//            // this should never happen
//            this.energyContainer = new SimpleUpdatingEnergyContainer(this, 0);
//        }
//        return this.energyContainer;
//    }
//
//    @Override
//    public UpdatingFluidContainer<BlockEntity> getFluidContainer() {
//        return this.fluidContainer;
//    }
//
//    @Override
//    public void update() {
//        this.setChanged();
//        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
//    }
//
//    @Override
//    public SerializableContainer getContainer() {
//        return this.itemContainer;
//    }

}
