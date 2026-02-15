package com.mrflamazing.rpr.forge;

import com.mrflamazing.rpr.Constants;
import com.mrflamazing.rpr.PrunerLogic;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.stream.Collectors;

@Mod(Constants.MOD_ID)
public class ForgePruner {
    public ForgePruner() {
        // One-liner setup, just like Fabric!
        PrunerLogic.init(
                FMLPaths.CONFIGDIR.get(),
                ModList.get().getMods().stream()
                        .map(IModInfo::getModId)
                        .collect(Collectors.toList())
        );
    }
}
