package com.mrflamazing.rpr.fabric;

import com.mrflamazing.rpr.PrunerLogic;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.stream.Collectors;

public class FabricPruner implements ModInitializer {
    @Override
    public void onInitialize() {
        PrunerLogic.init(
                FabricLoader.getInstance().getConfigDir(),
                FabricLoader.getInstance().getAllMods().stream()
                        .map(ModContainer::getMetadata)
                        .map(ModMetadata::getId)
                        .collect(Collectors.toList())
        );
    }
}
