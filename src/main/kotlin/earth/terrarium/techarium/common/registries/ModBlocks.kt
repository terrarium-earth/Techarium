package earth.terrarium.techarium.common.registries

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry
import com.teamresourceful.resourcefullibkt.common.getValue
import earth.terrarium.techarium.common.TechariumConstants
import earth.terrarium.techarium.common.blocks.machines.deploying.BasicDeployChildBlock
import earth.terrarium.techarium.common.blocks.machines.farming.SprinklerBlock
import earth.terrarium.techarium.common.blocks.machines.farming.StableFarmBlock
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.PushReaction

object ModBlocks {
    val registry: ResourcefulRegistry<Block> = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, TechariumConstants.MOD_ID)

    val basicDeployChildBlock: BasicDeployChildBlock by registry.register("basic_deploy_child_block") {
        BasicDeployChildBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion().pushReaction(PushReaction.BLOCK))
    }

    val sprinkler: SprinklerBlock by registry.register("sprinkler") {
        SprinklerBlock(BlockBehaviour.Properties.of()
            .requiresCorrectToolForDrops()
            .strength(0.25f)
            .noOcclusion()
            .pushReaction(PushReaction.BLOCK)
        )
    }

    val stableFarmland: StableFarmBlock by registry.register("stable_farmland") {
        StableFarmBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND).lootFrom { Blocks.FARMLAND })
    }
}