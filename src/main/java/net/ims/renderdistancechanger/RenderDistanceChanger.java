package net.ims.renderdistancechanger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;

public class RenderDistanceChanger implements ClientModInitializer {
    public static Config config;
    @Override
    public void onInitializeClient() {
        config = new Config(FabricLoader.getInstance().getConfigDir().resolve("rdchanger.properties"));

        try {
            config.initialize();
        } catch (IOException e) {
            System.err.println("Failed to initialize RDChanger configuration, default values will be used instead");
            e.printStackTrace();
        }
    }
}
