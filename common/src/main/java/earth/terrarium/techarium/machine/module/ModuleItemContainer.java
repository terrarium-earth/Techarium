package earth.terrarium.techarium.machine.module;

import earth.terrarium.botarium.api.Updatable;
import earth.terrarium.botarium.api.item.SimpleItemContainer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ModuleItemContainer extends SimpleItemContainer {

    public <T extends BlockEntity & Updatable> ModuleItemContainer(T entity, int size) {
        super(entity, size);
    }

}
