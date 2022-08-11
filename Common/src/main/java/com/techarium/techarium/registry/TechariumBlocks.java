package com.techarium.techarium.registry;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.block.multiblock.MachineCoreBlock;
import com.techarium.techarium.block.selfdeploying.BotariumBlock;
import com.techarium.techarium.block.selfdeploying.ExchangeStationBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingComponentBlock;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class TechariumBlocks {
	public static final TechariumRegistrar<Block> REGISTRAR = new TechariumRegistrar<>(Techarium.MOD_ID, Registry.BLOCK);

	public static final Supplier<MachineCoreBlock> MACHINE_CORE = REGISTRAR.register("machine_core", MachineCoreBlock::new);
	public static final Supplier<Block> COMMUNICATION_DEVICE_ELEMENT = REGISTRAR.register("com_device_element", () -> new Block(BlockBehaviour.Properties.of(Material.METAL)));
	public static final Supplier<SelfDeployingComponentBlock> SELF_DEPLOYING_COMPONENT = REGISTRAR.register("self_deploying_component", () -> new SelfDeployingComponentBlock(BlockBehaviour.Properties.of(Material.METAL)));
	public static final Supplier<SelfDeployingBlock> EXCHANGE_STATION = REGISTRAR.register("exchange_station", ExchangeStationBlock::new);
	public static final Supplier<SelfDeployingBlock> BOTARIUM = REGISTRAR.register("botarium", BotariumBlock::new);

}
