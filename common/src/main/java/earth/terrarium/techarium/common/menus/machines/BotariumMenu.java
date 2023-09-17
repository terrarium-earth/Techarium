package earth.terrarium.techarium.common.menus.machines;

import earth.terrarium.techarium.common.blockentities.machines.BotariumBlockEntity;
import earth.terrarium.techarium.common.menus.base.BasicContainerMenu;
import earth.terrarium.techarium.common.registry.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class BotariumMenu extends BasicContainerMenu<BotariumBlockEntity> {

    public BotariumMenu(int id, Inventory inventory, BotariumBlockEntity entity) {
        super(ModMenus.BOTARIUM.get(), id, inventory, entity);
    }

    public BotariumMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        super(ModMenus.BOTARIUM.get(), id, inventory, getBlockEntityFromBuf(inventory.player.level(), buf, BotariumBlockEntity.class));
    }

    @Override
    protected int getContainerInputEnd() {
        return 3;
    }

    @Override
    protected int getInventoryStart() {
        return 3;
    }

    @Override
    protected int startIndex() {
        return 4;
    }

    @Override
    public int getPlayerInvXOffset() {
        return -1;
    }

    @Override
    public int getPlayerInvYOffset() {
        return 102;
    }

    @Override
    protected void addMenuSlots() {
        addSlot(new Slot(entity, 0, 75, 81));
        addSlot(new Slot(entity, 1, 95, 81));
        addSlot(new Slot(entity, 2, 115, 81));
        addSlot(new Slot(entity, 3, 135, 81));

        addSlot(new Slot(entity, 4, 41, 35));
        addSlot(new Slot(entity, 5, 41, 67) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        addSlot(new Slot(entity, 6, 176, 39));
        addSlot(new Slot(entity, 7, 176, 57));
        addSlot(new Slot(entity, 8, 176, 75));
    }
}
