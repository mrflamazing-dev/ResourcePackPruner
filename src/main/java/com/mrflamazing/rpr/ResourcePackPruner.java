package com.mrflamazing.rpr;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

@Mod(ResourcePackPruner.MODID)
public class ResourcePackPruner {
    public static final String MODID = "resourcepackpruner";

    public static final ModConfigSpec COMMON_CONFIG;
    public static final ModConfigSpec.ConfigValue<Boolean> CONFIG_DEBUG_MODE;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CONFIG_SAFE_NAMESPACES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CONFIG_ASSET_DIRECTORIES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CONFIG_ZONE_PREFIXES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CONFIG_SAFE_FOLDERS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("General");

        CONFIG_DEBUG_MODE = builder.comment("Enable debug logging.").define("debugMode", false);

        CONFIG_SAFE_NAMESPACES = builder
                .comment("List of namespaces (Mod IDs) that should NEVER be pruned.")
                .defineList("safeNamespaces", Arrays.asList(
                        "minecraft", "realms", "neoforge", "forge", "c", "kubejs",
                        "fusion", "create", "flywheel", "fabric", "jei", "curios"
                ), o -> o instanceof String);

        CONFIG_ASSET_DIRECTORIES = builder
                .comment("If a file starts with one of these folders AND the mod is missing, delete it.")
                .defineList("assetDirectories", Arrays.asList(
                        "optifine", "shaders", "model_overlays", "connecting_textures"
                ), o -> o instanceof String);

        CONFIG_ZONE_PREFIXES = builder
                .comment("If a file is inside one of these folders, check the sub-folders for missing Mod IDs.")
                .defineList("zonePrefixes", Arrays.asList(
                        "fusion",
                        "model_overlays",
                        "connecting_textures",
                        "toms_storage",
                        "optifine"
                ), o -> o instanceof String);

        // UPDATED LIST: Added technical folders that are NOT mod names
        CONFIG_SAFE_FOLDERS = builder
                .comment("Folders names to IGNORE. If the pruner sees these, it assumes the file is safe.")
                .defineList("safeFolders", Arrays.asList(
                        // Vanilla
                        "block", "blocks", "item", "items", "entity", "entities",
                        "misc", "obj", "models", "textures", "particles", "gui",
                        "mob", "mobs", "particle", "environment", "armor", "trinkets",
                        "font", "lang", "sounds",
                        // Fusion / Connected Textures / Optifine
                        "model_modifiers", "model_types", "texture_types",
                        "cit", "cem", "random", "sky", "ctm", "connected"
                ), o -> o instanceof String);

        builder.pop();
        COMMON_CONFIG = builder.build();
    }

    public ResourcePackPruner(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModList.get().getMods().forEach(mod -> PrunerLogic.addLoadedMod(mod.getModId()));

        PrunerLogic.updateConfig(
                CONFIG_DEBUG_MODE.get(),
                CONFIG_SAFE_NAMESPACES.get(),
                CONFIG_ASSET_DIRECTORIES.get(),
                CONFIG_ZONE_PREFIXES.get(),
                CONFIG_SAFE_FOLDERS.get()
        );
    }
}