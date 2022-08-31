package earth.terrarium.techarium.registry;

import earth.terrarium.botarium.api.RegistryHelpers;
import earth.terrarium.botarium.api.RegistryHolder;
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
	public static final RegistryHolder<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryHolder<>(Registry.BLOCK_ENTITY_TYPE, Techarium.MOD_ID);

	public static final Supplier<BlockEntityType<MachineCoreBlockEntity>> MACHINE_CORE = BLOCK_ENTITIES.register("machine_core",
			() -> RegistryHelpers.createBlockEntityType(MachineCoreBlockEntity::new, TechariumBlocks.MACHINE_CORE.get()));


	public static final Supplier<BlockEntityType<SelfDeployingComponentBlockEntity>> SELF_DEPLOYING_COMPONENT = BLOCK_ENTITIES.register("self_deploying_component",
			() -> RegistryHelpers.createBlockEntityType(SelfDeployingComponentBlockEntity::new, TechariumBlocks.SELF_DEPLOYING_COMPONENT.get()));

	public static final Supplier<BlockEntityType<ExchangeStationBlockEntity>> EXCHANGE_STATION = BLOCK_ENTITIES.register("exchange_station",
			() -> RegistryHelpers.createBlockEntityType(ExchangeStationBlockEntity::new, TechariumBlocks.EXCHANGE_STATION.get()));

	public static final Supplier<BlockEntityType<BotariumBlockEntity>> BOTARIUM = BLOCK_ENTITIES.register("botarium",
			() -> RegistryHelpers.createBlockEntityType(BotariumBlockEntity::new, TechariumBlocks.BOTARIUM.get()));

	public static final Supplier<BlockEntityType<GravMagnetBlockEntity>> GRAVMAGNET = BLOCK_ENTITIES.register("gravmagnet",
			() -> RegistryHelpers.createBlockEntityType(GravMagnetBlockEntity::new, TechariumBlocks.GRAVMAGNET.get()));


}
