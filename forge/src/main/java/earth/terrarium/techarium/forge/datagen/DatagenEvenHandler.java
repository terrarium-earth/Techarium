package earth.terrarium.techarium.forge.datagen;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.machine.definition.MachineDefinition;
import earth.terrarium.techarium.machine.definition.MachineModuleDefinition;
import earth.terrarium.techarium.multiblock.MultiblockElement;
import earth.terrarium.techarium.multiblock.MultiblockStructure;
import earth.terrarium.techarium.registry.RegistryHelper;
import earth.terrarium.techarium.util.Utils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Techarium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DatagenEvenHandler {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        RegistryAccess registryAccess = RegistryAccess.builtinCopy();
        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

        JsonCodecProvider<MultiblockStructure> multiblockProvider = JsonCodecProvider.forDatapackRegistry(
                generator, existingFileHelper, Techarium.MOD_ID, registryOps, RegistryHelper.getMultiblockRegistryKey(), createMultiblocks());
        JsonCodecProvider<MachineDefinition> machineDefinitionProvider = JsonCodecProvider.forDatapackRegistry(
                generator, existingFileHelper, Techarium.MOD_ID, registryOps, RegistryHelper.getMachineDefinitionRegistryKey(), createMachineDefinitions());
        generator.addProvider(event.includeServer(), multiblockProvider);
        generator.addProvider(event.includeServer(), machineDefinitionProvider);
    }

    private static Map<ResourceLocation, MultiblockStructure> createMultiblocks() {
        MultiblockStructure exchange_station = new MultiblockStructure(Utils.resourceLocation("exchange_station"),
                new String[][]{{"d"}, {"@"}},
                Map.of("d", block("techarium:com_device_element")));
        MultiblockElement diamond = block("diamond_block");
        MultiblockStructure test_state_station = new MultiblockStructure(Utils.resourceLocation("exchange_station"),
                new String[][]{{"d "}, {"@s"}},
                Map.of("d", diamond,
                        "s", new MultiblockElement.Builder().block("stone_slab").state("type=bottom").build()));
//						"s", new MultiblockElement.Builder().block(Registry.BLOCK.getKey(Blocks.STONE_SLAB).toString()).state(SlabBlock.TYPE.value(SlabType.BOTTOM).toString()).build()));
        MultiblockStructure test_station = new MultiblockStructure(Utils.resourceLocation("exchange_station"),
                new String[][]{{"ed", "d "}, {"@l", "  "}},
                Map.of("d", new MultiblockElement.Builder().tag("forge:cobblestone").build(),
                        "e", block("techarium:com_device_element"),
                        "l", new MultiblockElement.Builder().block("coal_block", "iron_block").tag("forge:storage_blocks").build()));
        MultiblockStructure test_station2 = new MultiblockStructure(Utils.resourceLocation("exchange_station"),
                new String[][]{{"d"}, {"d"}, {"@"}},
                Map.of("d", diamond));
        MultiblockStructure test_station3 = new MultiblockStructure(Utils.resourceLocation("exchange_station"),
                new String[][]{{"d"}, {"d"}, {"d"}, {"@"}},
                Map.of("d", diamond));
        MultiblockStructure test_station4 = new MultiblockStructure(Utils.resourceLocation("exchange_station"),
                new String[][]{{"d"}, {"d"}, {"d"}, {"d"}, {"@"}},
                Map.of("d", diamond));
        Map<String, MultiblockElement> exampleKeys = new ImmutableMap.Builder<String, MultiblockElement>().put("c", block("orange_wool"))
                .put("d", block("purple_wool")).put("e", block("blue_wool")).put("f", block("brown_wool"))
                .put("l", block("magenta_wool")).put("n", block("yellow_wool")).put("o", block("lime_wool"))
                .put("t", block("gray_wool")).put("u", block("light_gray_wool")).put("v", block("cyan_wool"))
                .put("w", block("white_wool")).put("z", block("cut_copper_slab")).build();
        MultiblockStructure example_station = new MultiblockStructure(Utils.resourceLocation("exchange_station"),
                new String[][]{{"  c", "def", "   "}, {"  l", " @n", "o  "}, {"  t", "uvw", "  z"}},
                exampleKeys);
        return Map.of(Utils.resourceLocation("exchange_station"), exchange_station,
                Utils.resourceLocation("test_state_station"), test_state_station,
                Utils.resourceLocation("test_station"), test_station,
                Utils.resourceLocation("test_station_2"), test_station2,
                Utils.resourceLocation("test_station_3"), test_station3,
                Utils.resourceLocation("test_station_4"), test_station4,
                Utils.resourceLocation("example_station"), example_station
        );
    }

    private static MultiblockElement block(String rl) {
        return new MultiblockElement.Builder().block(rl).build();
    }

    private static Map<ResourceLocation, MachineDefinition> createMachineDefinitions() {
        MachineDefinition test1 = new MachineDefinition(List.of(), List.of(1, 2, 1));
        MachineDefinition test2 = new MachineDefinition(List.of(new MachineModuleDefinition(MachineModuleDefinition.ModuleType.ITEM, 8, 10, 0)), List.of(2, 2, 2));
        MachineDefinition energyCell = new MachineDefinition(List.of(new MachineModuleDefinition(MachineModuleDefinition.ModuleType.ENERGY, 1000, 100, 100)), List.of(1,1,1));
        MachineDefinition chestWithEnergy = new MachineDefinition(List.of(new MachineModuleDefinition(MachineModuleDefinition.ModuleType.ENERGY, 1000, 100, 100),
                new MachineModuleDefinition(MachineModuleDefinition.ModuleType.ITEM, 6, 1, 1)), List.of(1,1,1));
        return Map.of(Utils.resourceLocation("test1"), test1,
                Utils.resourceLocation("test2"), test2,
                Utils.resourceLocation("energy_cell"), energyCell,
                Utils.resourceLocation("chest_with_energy"), chestWithEnergy);
    }

}
