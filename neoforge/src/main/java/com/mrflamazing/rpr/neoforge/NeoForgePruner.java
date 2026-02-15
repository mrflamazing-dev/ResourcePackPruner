package com.mrflamazing.rpr.neoforge;

import com.mrflamazing.rpr.Constants;
import com.mrflamazing.rpr.PrunerLogic;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;

import java.util.stream.Collectors;

@Mod(Constants.MOD_ID)
public class NeoForgePruner {
    public NeoForgePruner() {
        // Initialize the common logic
        PrunerLogic.init(
                FMLPaths.CONFIGDIR.get(),
                ModList.get().getMods().stream()
                        .map(IModInfo::getModId)
                        .collect(Collectors.toList())
        );
    }
}
