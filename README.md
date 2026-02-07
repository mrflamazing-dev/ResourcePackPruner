
# Resource Pack Pruner (RPR)

**Resource Pack Pruner** is a NeoForge optimization and stability mod designed for modpacks. It automatically detects and removes assets (textures, models, and blockstate definitions) that belong to mods **not currently installed**.

## 🛑 The Problem
In modern Minecraft (1.19+), using large Resource Packs (like "Glowing Emissive Ores" or "Glowing Emissive Ores Fusion" packs) often causes the game to log **errors** or fail to load if the resource pack references a mod that you don't have installed.

Mods like **Fusion**, **Connected Textures**, and **Optifine** often try to process these files, realize the target block doesn't exist, and crash the game during startup.

## ✅ The Solution
RPR intercepts the resource loading process before files reach the game engine.
1. It scans every file in your resource packs.
2. If a file belongs to a Mod ID that is not loaded, it is **pruned** (deleted from memory).
3. The game loads safely, and you save RAM by not loading unused textures.

## ⚙️ Configuration & Compatibility
RPR is fully data-driven. If you find that a resource pack is missing textures (e.g., a generic "blocks" folder is being deleted), you can whitelist it in the config.

**Config Location:** `config/resourcepackpruner-common.toml`

### Key Settings:
* **`safeFolders`**: A list of folder names that should NEVER be treated as Mod IDs.
    * *Default:* `["blocks", "items", "entity", "model_modifiers", "textures", ...]`
    * *Usage:* If you have a resource pack with a folder `assets/fusion/model_overlays/cool_stuff/` and "cool_stuff" is being deleted, add `"cool_stuff"` to this list.
* **`assetDirectories`**: The list of sensitive directories to scan (e.g., `optifine`, `fusion`).
* **`debugMode`**: Set to `true` to see exactly what files are being removed in the logs.

## 🛠️ Technical Details
* **Loader:** NeoForge (1.21.1)
* **Performance:** Uses lock-free caching and optimized string parsing. Adds **0ms** to load time.
* **Compatibility:** Works with Fusion, ConnectedTexturesMod, Optifine, and standard Vanilla packs.
