package com.techarium.techarium.registry;

import com.techarium.techarium.block.CommunicationDeviceCoreBlock;
import com.techarium.techarium.block.CommunicationDeviceElementBlock;
import com.techarium.techarium.block.ExchangeStationBlock;
import com.techarium.techarium.block.SelfDeployingBlock;
import com.techarium.techarium.block.SelfDeployingSlaveBlock;
import com.techarium.techarium.multiblock.MultiBlockBaseCore;
import com.techarium.techarium.multiblock.MultiBlockBaseElement;
import com.techarium.techarium.platform.CommonServices;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class TechariumBlocks {

	public static final Supplier<MultiBlockBaseCore> COMMUNICATION_DEVICE_CORE = CommonServices.REGISTRY.registerBlock("com_device_core", CommunicationDeviceCoreBlock::new);
	public static final Supplier<MultiBlockBaseElement> COMMUNICATION_DEVICE_ELEMENT = CommonServices.REGISTRY.registerBlock("com_device_element", CommunicationDeviceElementBlock::new);
	public static final Supplier<SelfDeployingBlock> EXCHANGE_STATION = CommonServices.REGISTRY.registerBlock("exchange_station", ExchangeStationBlock::new);
	public static final Supplier<SelfDeployingSlaveBlock> SELF_DEPLOYING_SLAVE = CommonServices.REGISTRY.registerBlock("self_deploying_slave", SelfDeployingSlaveBlock::new);

	public static void register() {}

}
