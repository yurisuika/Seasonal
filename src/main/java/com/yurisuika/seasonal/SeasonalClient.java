package com.yurisuika.seasonal;

import com.yurisuika.seasonal.colors.SeasonGrassColormapResourceSupplier;
import com.yurisuika.seasonal.utils.ModConfig;
import com.yurisuika.seasonal.utils.Season;
import com.yurisuika.seasonal.colors.SeasonFoliageColormapResourceSupplier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

import static com.yurisuika.seasonal.Seasonal.ASK_FOR_CONFIG;

public class SeasonalClient implements ClientModInitializer {

    private static boolean isServerConfig = false;
    private static ModConfig clientConfig = null;
    private static final Map<RegistryKey<World>, Season> lastRenderedSeasonMap = new HashMap<>();

    @Override
    public void onInitializeClient() {
        clientConfig = Seasonal.CONFIG;
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SeasonGrassColormapResourceSupplier());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SeasonFoliageColormapResourceSupplier());

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            Seasonal.SEEDS_MAP.clear();
            Registry.ITEM.forEach(item -> {
                if(item instanceof BlockItem) {
                    Block block = ((BlockItem) item).getBlock();
                    if(block instanceof CropBlock || block instanceof StemBlock || block instanceof CocoaBlock || block instanceof SaplingBlock) {
                        Seasonal.SEEDS_MAP.put(item, ((BlockItem) item).getBlock());
                    }
                }
            });
        });

        ClientTickEvents.END_WORLD_TICK.register((clientWorld) -> {
            if(Seasonal.getCurrentSeason(clientWorld) != lastRenderedSeasonMap.get(clientWorld.getRegistryKey())) {
                lastRenderedSeasonMap.put(clientWorld.getRegistryKey(), Seasonal.getCurrentSeason(clientWorld));
                MinecraftClient.getInstance().worldRenderer.reload();
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(Seasonal.ANSWER_CONFIG, (client, handler, buf, responseSender) -> {
            int stringSize = buf.readInt();
            String configJson = buf.readString(stringSize);
            Seasonal.CONFIG = Seasonal.GSON.fromJson(configJson, ModConfig.class);
            isServerConfig = true;
            Seasonal.LOGGER.info("Received dedicated server config.");
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if(!client.isIntegratedServerRunning()) {
                Seasonal.LOGGER.info("Joined dedicated server, asking for config.");
                ClientPlayNetworking.send(ASK_FOR_CONFIG, PacketByteBufs.empty());
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> {
            if(isServerConfig && clientConfig != null) {
                Seasonal.LOGGER.info("Left dedicated server, restoring config.");
                Seasonal.CONFIG = clientConfig;
                isServerConfig = false;
            }
        }));

        //Since we're replacing the Blocks.ICE entry we have to manually add the default ice block to the translucent render layer
        BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier("ice")), RenderLayer.getTranslucent());
    }
}
