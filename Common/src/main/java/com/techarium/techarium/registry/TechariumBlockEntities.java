package com.techarium.techarium.registry;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.block.entity.selfdeploying.BotariumBlockEntity;
import com.techarium.techarium.block.entity.selfdeploying.ExchangeStationBlockEntity;
import com.techarium.techarium.block.entity.selfdeploying.SelfDeployingComponentBlockEntity;
import com.techarium.techarium.block.entity.multiblock.MachineCoreBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class TechariumBlockEntities {
	public static final TechariumRegistrar<BlockEntityType<?>> REGISTRAR = new TechariumRegistrar<>(Techarium.MOD_ID, Registry.BLOCK_ENTITY_TYPE);

	public static final Supplier<BlockEntityType<MachineCoreBlockEntity>> MACHINE_CORE = REGISTRAR.register("machine_core",
			() -> RegistryHelper.createBlockEntityType(MachineCoreBlockEntity::new, TechariumBlocks.MACHINE_CORE.get()));


	public static final Supplier<BlockEntityType<SelfDeployingComponentBlockEntity>> SELF_DEPLOYING_COMPONENT = REGISTRAR.register("self_deploying_component",
			() -> RegistryHelper.createBlockEntityType(SelfDeployingComponentBlockEntity::new, TechariumBlocks.SELF_DEPLOYING_COMPONENT.get()));

	public static final Supplier<BlockEntityType<ExchangeStationBlockEntity>> EXCHANGE_STATION = REGISTRAR.register("exchange_station",
			() -> RegistryHelper.createBlockEntityType(ExchangeStationBlockEntity::new, TechariumBlocks.EXCHANGE_STATION.get()));

	public static final Supplier<BlockEntityType<BotariumBlockEntity>> BOTARIUM = REGISTRAR.register("botarium",
			() -> RegistryHelper.createBlockEntityType(BotariumBlockEntity::new, TechariumBlocks.BOTARIUM.get()));


}
