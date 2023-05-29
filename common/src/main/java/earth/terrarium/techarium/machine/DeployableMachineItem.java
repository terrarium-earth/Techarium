package earth.terrarium.techarium.machine;

import earth.terrarium.techarium.registry.TechariumBlocks;
import earth.terrarium.techarium.registry.TechariumItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DeployableMachineItem extends BlockItem {

    public DeployableMachineItem(Properties properties) {
        super(TechariumBlocks.DEPLOYABLE_MACHINE.get(), properties);
    }

    public static ItemStack toStack(String machineId) {
        return toStack(machineId, 1);
    }

    public static ItemStack toStack(String machineId, int amount) {
        ItemStack stack = new ItemStack(TechariumItems.DEPLOYABLE_MACHINE.get(), amount);
        CompoundTag tag = new CompoundTag();
        tag.putString("machine", machineId);
        stack.setTag(tag);
        return stack;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.getTag()==null) {
            return Component.empty();
        }
        return Component.translatable("machine.techarium." + stack.getTag().getString("machine").replace(":", "."));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = super.useOn(context);
        if (result.consumesAction()) {
            // block was placed, setup the block entity
            BlockPlaceContext placeContext = new BlockPlaceContext(context);
            BlockEntity blockEntity = placeContext.getLevel().getBlockEntity(placeContext.getClickedPos());
            if (blockEntity instanceof DeployableMachineBlockEntity machineBlockEntity) {
                machineBlockEntity.setupMachine(placeContext.getItemInHand().getTag().getString("machine"));
            }
        }
        return result;
    }
}
