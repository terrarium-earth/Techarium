package earth.terrarium.techarium.fabric.client;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.client.screen.TechariumMenuScreens;
import earth.terrarium.techarium.fabric.client.render.BotariumRenderer;
import earth.terrarium.techarium.fabric.client.render.ExchangeStationRenderer;
import earth.terrarium.techarium.machine.DeployableMachineBlock;
import earth.terrarium.techarium.machine.DeployableMachineItem;
import earth.terrarium.techarium.machine.definition.MachineDefinition;
import earth.terrarium.techarium.registry.RegistryHelper;
import earth.terrarium.techarium.registry.TechariumBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

public class TechariumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(TechariumBlockEntities.EXCHANGE_STATION.get(), (renderer) -> new ExchangeStationRenderer());
        BlockEntityRendererRegistry.register(TechariumBlockEntities.BOTARIUM.get(), (renderer) -> new BotariumRenderer());

        TechariumMenuScreens.register();
        Techarium.setPopulateMachinesConsumer(items -> {
            Registry<MachineDefinition> registry = Minecraft.getInstance().level.registryAccess().registry(RegistryHelper.getMachineDefinitionRegistryKey()).get();
            for (Map.Entry<ResourceKey<MachineDefinition>, MachineDefinition> entry : registry.entrySet()) {
                items.add(DeployableMachineItem.toStack(entry.getKey().location().toString()));
            }
        });
    }

}
