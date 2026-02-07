package com.mrflamazing.rpr;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PrunerLogic {
    private static final Logger LOGGER = LoggerFactory.getLogger("ResourcePackPruner");

    private static final Set<String> LOADED_MODS = ConcurrentHashMap.newKeySet();
    private static final List<String> ASSET_DIRS = new ArrayList<>();
    private static final List<String> ZONES = new ArrayList<>();
    private static final Set<String> SAFE_FOLDERS = new HashSet<>();

    private static boolean debugMode = false;
    private static volatile boolean isConfigLoaded = false;

    static {
        // Safe Defaults to prevent crashes before Config Load
        LOADED_MODS.addAll(Arrays.asList("minecraft", "realms", "neoforge", "forge", "c", "kubejs", "fusion", "create", "flywheel"));
        ASSET_DIRS.addAll(Arrays.asList("optifine", "shaders", "model_overlays", "connecting_textures"));
        ZONES.addAll(Arrays.asList("fusion", "model_overlays", "connecting_textures"));

        // Expanded Safe List
        SAFE_FOLDERS.addAll(Arrays.asList(
                "block", "blocks", "item", "items", "entity", "entities", "models", "textures", "gui",
                "model_modifiers", "cit", "cem", "ctm", "connected"
        ));
    }

    public static void updateConfig(boolean debug, List<? extends String> safeMods, List<? extends String> assets, List<? extends String> zones, List<? extends String> safeFolders) {
        debugMode = debug;
        LOADED_MODS.addAll(safeMods);
        ASSET_DIRS.clear(); ASSET_DIRS.addAll(assets);
        ZONES.clear(); ZONES.addAll(zones);
        SAFE_FOLDERS.clear(); SAFE_FOLDERS.addAll(safeFolders);
        isConfigLoaded = true;
        LOGGER.info("[RPR] Config loaded. Rules updated.");
    }

    public static void addLoadedMod(String modId) {
        LOADED_MODS.add(modId);
    }

    public static boolean shouldPrune(ResourceLocation id) {
        String namespace = id.getNamespace();
        String path = id.getPath();

        // 1. NAMESPACE CHECK
        if (!LOADED_MODS.contains(namespace)) {
            for (String dir : ASSET_DIRS) {
                if (path.startsWith(dir)) {
                    if (debugMode) LOGGER.info("[RPR] PRUNING (Namespace): {} (Mod '{}' missing)", id, namespace);
                    return true;
                }
            }
            // Check zones inside missing namespaces
            for (String zone : ZONES) {
                if (path.contains(zone)) {
                    if (debugMode) LOGGER.info("[RPR] PRUNING (Namespace/Zone): {} (Mod '{}' missing)", id, namespace);
                    return true;
                }
            }
        }

        // 2. ZONE CHECK (Fusion/Optimization)
        for (String zone : ZONES) {
            if (path.contains(zone)) {
                String targetMod = extractTargetMod(path, zone);

                if (targetMod != null && !targetMod.isEmpty()) {
                    // Check whitelist first!
                    if (SAFE_FOLDERS.contains(targetMod)) {
                        return false; // It's just a folder name like "model_modifiers", keep it.
                    }

                    if (!LOADED_MODS.contains(targetMod)) {
                        if (debugMode) LOGGER.info("[RPR] PRUNING (Zone): {} (Target '{}' missing)", id, targetMod);
                        return true;
                    }
                }
                break;
            }
        }

        return false;
    }

    private static String extractTargetMod(String path, String zoneMatch) {
        int zoneIndex = path.indexOf(zoneMatch);
        if (zoneIndex == -1) return null;

        String remaining = path.substring(zoneIndex + zoneMatch.length());
        if (remaining.startsWith("/")) remaining = remaining.substring(1);

        int slashIndex = remaining.indexOf('/');
        if (slashIndex > 0) return remaining.substring(0, slashIndex);

        if (remaining.endsWith(".json")) return remaining.substring(0, remaining.length() - 5);
        if (remaining.endsWith(".png")) return remaining.substring(0, remaining.length() - 4);
        if (remaining.endsWith(".mcmeta")) return remaining.substring(0, remaining.length() - 7);
        if (remaining.endsWith(".properties")) return remaining.substring(0, remaining.length() - 11);

        return null;
    }
}