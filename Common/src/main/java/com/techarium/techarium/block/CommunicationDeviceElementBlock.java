package com.techarium.techarium.block;

import com.techarium.techarium.multiblock.MultiBlockBaseElement;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class CommunicationDeviceElementBlock extends MultiBlockBaseElement {

	public CommunicationDeviceElementBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL));
	}

}
