package earth.terrarium.techarium.common.networking.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ServerboundClearFluidTankPacket(
    BlockPos machine, int tank
) implements Packet<ServerboundClearFluidTankPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Techarium.MOD_ID, "clear_fluid_tank");
    public static final Handler HANDLER = new ServerboundClearFluidTankPacket.Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundClearFluidTankPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ServerboundClearFluidTankPacket> {
        @Override
        public void encode(ServerboundClearFluidTankPacket packet, FriendlyByteBuf buf) {
            buf.writeBlockPos(packet.machine());
            buf.writeByte(packet.tank());
        }

        @Override
        public ServerboundClearFluidTankPacket decode(FriendlyByteBuf buf) {
            return new ServerboundClearFluidTankPacket(
                buf.readBlockPos(),
                buf.readByte());
        }

        @Override
        public PacketContext handle(ServerboundClearFluidTankPacket packet) {
            return (player, level) -> ModUtils.getMachineFromMenuPacket(packet.machine(), player, level).ifPresent(
                machine -> {
                    if (!(machine instanceof BotariumFluidBlock<?> fluidBlock)) return;
                    FluidContainer container = fluidBlock.getFluidContainer();
                    if (packet.tank() > container.getSize()) return;
                    container.internalExtract(container.getFluids().get(packet.tank()), false);
                    container.extractFluid(container.getFluids().get(packet.tank()), false);
                }
            );
        }
    }
}
