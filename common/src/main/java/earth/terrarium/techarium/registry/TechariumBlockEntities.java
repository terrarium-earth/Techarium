package earth.terrarium.techarium.registry;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.block.entity.selfdeploying.BotariumBlockEntity;
import earth.terrarium.techarium.block.entity.selfdeploying.ExchangeStationBlockEntity;
import earth.terrarium.techarium.block.entity.selfdeploying.SelfDeployingComponentBlockEntity;
import earth.terrarium.techarium.block.entity.multiblock.MachineCoreBlockEntity;
import earth.terrarium.techarium.block.entity.singleblock.GravMagnetBlockEntity;
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

	public static final Supplier<BlockEntityType<GravMagnetBlockEntity>> GRAVMAGNET = REGISTRAR.register("gravmagnet",
			() -> RegistryHelper.createBlockEntityType(GravMagnetBlockEntity::new, TechariumBlocks.GRAVMAGNET.get()));


}
