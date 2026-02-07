package com.mrflamazing.rpr.mixin;

import com.mrflamazing.rpr.PrunerLogic;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PathPackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({PathPackResources.class, FilePackResources.class})
public class MixinPackResources {

    @ModifyVariable(
            method = "listResources",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private PackResources.ResourceOutput wrapResourceOutput(PackResources.ResourceOutput original) {
        return (id, stream) -> {
            if (!PrunerLogic.shouldPrune(id)) {
                original.accept(id, stream);
            }
        };
    }
}