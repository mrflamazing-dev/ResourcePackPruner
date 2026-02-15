package com.mrflamazing.rpr.mixin;

import com.mrflamazing.rpr.PrunerLogic;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.PackType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({PathPackResources.class, FilePackResources.class})
public class MixinPackResources {

    @ModifyVariable(
            method = "listResources",
            at = @At("HEAD"),
            argsOnly = true // This tells Mixin we are modifying a method Argument
    )
    private PackResources.ResourceOutput wrapResourceOutput(PackResources.ResourceOutput original, PackType type) {
        // We capture 'PackType type' from the method arguments automatically.

        // 1. Check if this is CLIENT_RESOURCES (Assets like textures/models)
        if (type == PackType.CLIENT_RESOURCES) {
            // 2. If Client, wrap the output to apply pruning
            return (id, stream) -> {
                if (!PrunerLogic.shouldPrune(id)) {
                    original.accept(id, stream);
                }
            };
        }

        // 3. If SERVER_DATA (Recipes, Tags, KubeJS), return the original untouched.
        // This prevents the "Data Pack Validation Failed" error.
        return original;
    }
}