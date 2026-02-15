package com.mrflamazing.rpr;

import net.minecraft.resources.ResourceLocation;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrunerLogic {
    private static boolean DEBUG = false;
    private static final Set<String> LOADED_MODS = new HashSet<>();
    private static final Set<String> SAFE_NAMESPACES = new HashSet<>();
    private static final Set<String> ASSET_DIRS = new HashSet<>();
    private static final Set<String> ZONE_PREFIXES = new HashSet<>();
    private static final Set<String> SAFE_FOLDERS = new HashSet<>();

    // --- CENTRAL INIT METHOD ---
    public static void init(Path configDir, Iterable<String> modIds) {
        // 1. Load Config
        PrunerConfig.load(configDir);

        // 2. Register Loaded Mods
        for (String id : modIds) {
            addLoadedMod(id);
        }

        System.out.println("[ResourcePackPruner] Initialized. Pruning ready.");
    }

    public static void updateConfig(boolean debug, List<String> safeMods, List<String> assets, List<String> zones, List<String> safeFolders) {
        DEBUG = debug;
        SAFE_NAMESPACES.clear(); SAFE_NAMESPACES.addAll(safeMods);
        ASSET_DIRS.clear(); ASSET_DIRS.addAll(assets);
        ZONE_PREFIXES.clear(); ZONE_PREFIXES.addAll(zones);
        SAFE_FOLDERS.clear(); SAFE_FOLDERS.addAll(safeFolders);
    }

    public static void addLoadedMod(String modId) {
        LOADED_MODS.add(modId);
    }

    public static boolean shouldPrune(ResourceLocation id) {
        if (id == null) return false;
        String namespace = id.getNamespace();

        // 1. SAFETY FIRST: Always allow specific namespaces defined in Config
        // (e.g. "minecraft", "realms", "forge", "fabric", "fusion")
        if (SAFE_NAMESPACES.contains(namespace)) return false;

        // 2. CHECK LOADED MODS:
        // If the asset belongs to a mod that is currently running, allow it.
        if (LOADED_MODS.contains(namespace)) return false;

        // 3. PRUNE:
        // If it's not safe, and the mod isn't loaded, it's orphan data. Block it.
        if (DEBUG) {
            System.out.println("[RPR] Pruning orphaned asset: " + id);
        }
        return true;
    }
}
