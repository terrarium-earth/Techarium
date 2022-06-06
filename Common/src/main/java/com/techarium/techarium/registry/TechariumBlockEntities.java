package com.techarium.techarium.registry;

import com.techarium.techarium.blockentity.CommunicationDeviceCoreBlockEntity;
import com.techarium.techarium.blockentity.ExchangeStationBlockEntity;
import com.techarium.techarium.blockentity.SelfDeployingSlaveBlockEntity;
import com.techarium.techarium.multiblock.MultiBlockBaseTile;
import com.techarium.techarium.platform.CommonServices;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class TechariumBlockEntities {

	public static final Supplier<BlockEntityType<MultiBlockBaseTile>> COMMUNICATION_DEVICE_CORE = CommonServices.REGISTRY.registerBlockEntity("com_device_codre",
			() -> CommonServices.REGISTRY.createBlockEntityType(CommunicationDeviceCoreBlockEntity::new, TechariumBlocks.COMMUNICATION_DEVICE_CORE.get()));
	public static final Supplier<BlockEntityType<ExchangeStationBlockEntity>> EXCHANGE_STATION = CommonServices.REGISTRY.registerBlockEntity("exchange_station",
			() -> CommonServices.REGISTRY.createBlockEntityType(ExchangeStationBlockEntity::new, TechariumBlocks.EXCHANGE_STATION.get()));
	public static final Supplier<BlockEntityType<SelfDeployingSlaveBlockEntity>> SELF_DEPLOYING_SLAVE = CommonServices.REGISTRY.registerBlockEntity("self_deploying_slave",
			() -> CommonServices.REGISTRY.createBlockEntityType(SelfDeployingSlaveBlockEntity::new, TechariumBlocks.SELF_DEPLOYING_SLAVE.get()));

	public static void register() {}

}
