package earth.terrarium.techarium.common.networking;

import com.teamresourceful.resourcefullib.common.networking.NetworkChannel;
import earth.terrarium.techarium.Techarium;

public final class NetworkHandler {
    public static final NetworkChannel CHANNEL = new NetworkChannel(Techarium.MOD_ID, 1, "main");

    public static void init() {
    }
}
