package earth.terrarium.techarium.client;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.client.config.TechariumConfigClient;

public class TechariumClient {

    public static void init() {
        Techarium.CONFIGURATOR.registerConfig(TechariumConfigClient.class);
    }
}
