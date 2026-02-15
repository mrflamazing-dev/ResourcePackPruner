package com.mrflamazing.rpr;

import com.google.gson.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrunerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final List<String> SAFE_MODS = new ArrayList<>();
    private static final List<String> ASSET_DIRS = new ArrayList<>();
    private static final List<String> ZONES = new ArrayList<>();
    private static final List<String> SAFE_FOLDERS = new ArrayList<>();
    public static boolean debugMode = false;

    public static void load(Path configDir) {
        Path file = configDir.resolve("resourcepackpruner.json");

        // Defaults
        SAFE_MODS.addAll(Arrays.asList("minecraft", "realms", "neoforge", "forge", "c", "kubejs", "fusion", "create", "flywheel", "fabric", "jei", "curios"));
        ASSET_DIRS.addAll(Arrays.asList("optifine", "shaders", "model_overlays", "connecting_textures"));
        ZONES.addAll(Arrays.asList("fusion", "model_overlays", "connecting_textures", "toms_storage", "optifine"));
        SAFE_FOLDERS.addAll(Arrays.asList("block", "blocks", "item", "items", "entity", "entities", "misc", "obj", "models", "textures", "particles", "gui", "mob", "mobs", "particle", "environment", "armor", "trinkets", "font", "lang", "sounds", "model_modifiers", "model_types", "texture_types", "cit", "cem", "random", "sky", "ctm", "connected"));

        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                readList(json, "safe_namespaces", SAFE_MODS);
                readList(json, "asset_directories", ASSET_DIRS);
                readList(json, "zone_folders", ZONES);
                readList(json, "safe_folder_names", SAFE_FOLDERS);
                if (json.has("debug_mode")) debugMode = json.get("debug_mode").getAsBoolean();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            save(file);
        }

        // Push to Logic
        PrunerLogic.updateConfig(debugMode, SAFE_MODS, ASSET_DIRS, ZONES, SAFE_FOLDERS);
    }

    private static void save(Path file) {
        try (Writer writer = Files.newBufferedWriter(file)) {
            JsonObject json = new JsonObject();
            json.addProperty("debug_mode", debugMode);
            writeList(json, "safe_namespaces", SAFE_MODS);
            writeList(json, "asset_directories", ASSET_DIRS);
            writeList(json, "zone_folders", ZONES);
            writeList(json, "safe_folder_names", SAFE_FOLDERS);
            GSON.toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readList(JsonObject json, String key, List<String> list) {
        if (json.has(key)) {
            list.clear();
            for (JsonElement e : json.getAsJsonArray(key)) list.add(e.getAsString());
        }
    }

    private static void writeList(JsonObject json, String key, List<String> list) {
        JsonArray array = new JsonArray();
        list.forEach(array::add);
        json.add(key, array);
    }
}
