package com.techarium.techarium.registry;

import com.techarium.techarium.block.multiblock.CommunicationDeviceCoreBlock;
import com.techarium.techarium.block.multiblock.CommunicationDeviceElementBlock;
import com.techarium.techarium.block.selfdeploying.ExchangeStationBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingSlaveBlock;
import com.techarium.techarium.block.multiblock.MultiBlockCoreBlock;
import com.techarium.techarium.block.multiblock.MultiBlockElementBlock;
import com.techarium.techarium.platform.CommonServices;

import java.util.function.Supplier;

public class TechariumBlocks {

	public static final Supplier<MultiBlockCoreBlock> COMMUNICATION_DEVICE_CORE = CommonServices.REGISTRY.registerBlock("com_device_core", CommunicationDeviceCoreBlock::new);
	public static final Supplier<MultiBlockElementBlock> COMMUNICATION_DEVICE_ELEMENT = CommonServices.REGISTRY.registerBlock("com_device_element", CommunicationDeviceElementBlock::new);
	public static final Supplier<SelfDeployingBlock> EXCHANGE_STATION = CommonServices.REGISTRY.registerBlock("exchange_station", ExchangeStationBlock::new);
	public static final Supplier<SelfDeployingSlaveBlock> SELF_DEPLOYING_SLAVE = CommonServices.REGISTRY.registerBlock("self_deploying_slave", SelfDeployingSlaveBlock::new);

	public static void register() {}

}
