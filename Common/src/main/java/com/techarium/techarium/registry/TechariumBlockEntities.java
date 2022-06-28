package com.techarium.techarium.registry;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.blockentity.multiblock.CommunicationDeviceCoreBlockEntity;
import com.techarium.techarium.blockentity.multiblock.TestStationCoreBlockEntity;
import com.techarium.techarium.blockentity.selfdeploying.BotariumBlockEntity;
import com.techarium.techarium.blockentity.selfdeploying.ExchangeStationBlockEntity;
import com.techarium.techarium.blockentity.selfdeploying.SelfDeployingSlaveBlockEntity;
import com.techarium.techarium.blockentity.multiblock.MultiBlockCoreBlockEntity;
import com.techarium.techarium.platform.CommonServices;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class TechariumBlockEntities {

	public static final Supplier<BlockEntityType<MultiBlockCoreBlockEntity>> COMMUNICATION_DEVICE_CORE = CommonServices.REGISTRY.registerBlockEntity("com_device_core",
			() -> CommonServices.REGISTRY.createBlockEntityType(CommunicationDeviceCoreBlockEntity::new, TechariumBlocks.COMMUNICATION_DEVICE_CORE.get()));
	public static final Supplier<BlockEntityType<SelfDeployingSlaveBlockEntity>> SELF_DEPLOYING_SLAVE = CommonServices.REGISTRY.registerBlockEntity("self_deploying_slave",
			() -> CommonServices.REGISTRY.createBlockEntityType(SelfDeployingSlaveBlockEntity::new, TechariumBlocks.SELF_DEPLOYING_SLAVE.get()));
	public static final Supplier<BlockEntityType<ExchangeStationBlockEntity>> EXCHANGE_STATION = CommonServices.REGISTRY.registerBlockEntity("exchange_station",
			() -> CommonServices.REGISTRY.createBlockEntityType(ExchangeStationBlockEntity::new, TechariumBlocks.EXCHANGE_STATION.get()));
	public static final Supplier<BlockEntityType<BotariumBlockEntity>> BOTARIUM = CommonServices.REGISTRY.registerBlockEntity("botarium",
			() -> CommonServices.REGISTRY.createBlockEntityType(BotariumBlockEntity::new, TechariumBlocks.BOTARIUM.get()));

	public static final Supplier<BlockEntityType<MultiBlockCoreBlockEntity>> TEST_STATION_CORE = Techarium.DEBUG_MODE ? CommonServices.REGISTRY.registerBlockEntity("test_station",
			() -> CommonServices.REGISTRY.createBlockEntityType(TestStationCoreBlockEntity::new, TechariumBlocks.TEST_STATION_CORE.get())) : () -> null;

	public static void register() {}

}
