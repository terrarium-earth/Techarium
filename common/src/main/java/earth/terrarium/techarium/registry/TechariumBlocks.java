package earth.terrarium.techarium.registry;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.block.multiblock.MachineCoreBlock;
import earth.terrarium.techarium.block.selfdeploying.BotariumBlock;
import earth.terrarium.techarium.block.selfdeploying.ExchangeStationBlock;
import earth.terrarium.techarium.block.selfdeploying.SelfDeployingBlock;
import earth.terrarium.techarium.block.selfdeploying.SelfDeployingComponentBlock;
import earth.terrarium.techarium.block.singleblock.GravMagnetBlock;
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
	public static final Supplier<GravMagnetBlock> GRAVMAGNET = REGISTRAR.register("gravmagnet", () -> new GravMagnetBlock(BlockBehaviour.Properties.of(Material.METAL)));

}
