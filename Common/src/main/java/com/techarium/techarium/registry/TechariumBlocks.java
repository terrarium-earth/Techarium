package com.techarium.techarium.registry;

import com.techarium.techarium.block.selfdeploying.BotariumBlock;
import com.techarium.techarium.block.selfdeploying.ExchangeStationBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingSlaveBlock;
import com.techarium.techarium.block.multiblock.MachineCoreBlock;
import com.techarium.techarium.platform.CommonServices;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class TechariumBlocks {

	public static final Supplier<MachineCoreBlock> MACHINE_CORE = CommonServices.REGISTRY.registerBlock("machine_core", MachineCoreBlock::new);
	public static final Supplier<Block> COMMUNICATION_DEVICE_ELEMENT = CommonServices.REGISTRY.registerBlock("com_device_element", () -> new Block(BlockBehaviour.Properties.of(Material.METAL)));
	public static final Supplier<SelfDeployingSlaveBlock> SELF_DEPLOYING_SLAVE = CommonServices.REGISTRY.registerBlock("self_deploying_slave", SelfDeployingSlaveBlock::new);
	public static final Supplier<SelfDeployingBlock> EXCHANGE_STATION = CommonServices.REGISTRY.registerBlock("exchange_station", ExchangeStationBlock::new);
	public static final Supplier<SelfDeployingBlock> BOTARIUM = CommonServices.REGISTRY.registerBlock("botarium", BotariumBlock::new);

	public static void register() {}

}
