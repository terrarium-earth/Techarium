package earth.terrarium.techarium.common.networking;

import com.teamresourceful.resourcefullib.common.networking.NetworkChannel;
import com.teamresourceful.resourcefullib.common.networking.base.NetworkDirection;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.networking.messages.ServerboundClearFluidTankPacket;

public final class NetworkHandler {
    public static final NetworkChannel CHANNEL = new NetworkChannel(Techarium.MOD_ID, 1, "main");

    public static void init() {
        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, ServerboundClearFluidTankPacket.ID, ServerboundClearFluidTankPacket.HANDLER, ServerboundClearFluidTankPacket.class);
    }
}
