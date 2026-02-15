# Resource Pack Pruner (RPR)

**Resource Pack Pruner** is a lightweight, Multi-Loader (Fabric, Forge, NeoForge) optimization mod designed for modpacks. It automatically detects and removes assets (textures, models, and blockstate definitions) from Resource Packs that belong to mods **not currently installed**.

## 🛑 The Problem
In modern Minecraft, using large Resource Packs (like "Glowing Emissive Ores" or mod-support packs) often causes the game to log **thousands of errors** or fail to load if the resource pack references a mod that you don't have installed.

Mods like **Fusion**, **Connected Textures**, and **Optifine** often try to process these files, realize the target block doesn't exist, and can crash the game during startup or consume unnecessary RAM.

## ✅ The Solution
RPR intercepts the resource loading process before files reach the game engine.
1.  It scans files in your resource packs.
2.  It creates a **Smart Filter** that distinguishes between **Visual Assets** (Textures/Models) and **Data Files** (Recipes/Tags).
3.  If a *Visual Asset* belongs to a Mod ID that is not loaded, it is **pruned** (deleted from memory).
4.  **Data Files are preserved**, preventing "Safe Mode" world crashes caused by missing recipes.

## ⚙️ Configuration
RPR uses a simple JSON configuration file.

**Config Location:** `config/resourcepackpruner.json`

### Key Settings:
* **`safe_namespaces`**: A whitelist of Mod IDs that should **never** be pruned, even if the mod is missing.
  * *Useful for:* KubeJS scripts or Datapacks that "borrow" textures from other mods (e.g., using a `twilightforest` texture for a custom item without installing the mod).
* **`safe_folder_names`**: A list of folder names inside asset directories that should be ignored.
  * *Default:* `["gui", "jei", "curios", ...]`
* **`debug_mode`**: Set to `true` to print a log line for every single file being removed. (Warning: massive log file).

## 🛠️ Technical Details
* **Loaders:** NeoForge, Fabric, Forge (1.21.1+)
* **Performance:** Uses lock-free caching and optimized string parsing. Adds **0ms** to load time.
* **Compatibility:** Fully compatible with KubeJS, Fusion, ConnectedTexturesMod, Optifine, and Sodium/Embeddium.

### 🧩 Using KubeJS?
If you use KubeJS to create items that use textures from a mod you *don't* have installed, RPR will delete those textures by default.
To fix this, add that mod's ID to the **`safe_namespaces`** list in the config.
