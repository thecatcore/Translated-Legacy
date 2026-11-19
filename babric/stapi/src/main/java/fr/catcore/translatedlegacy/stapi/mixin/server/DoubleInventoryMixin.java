package fr.catcore.translatedlegacy.stapi.mixin.server;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.DoubleInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.SERVER)
@Mixin(DoubleInventory.class)
public class DoubleInventoryMixin {
    @ModifyReturnValue(method = "getName", at = @At("RETURN"))
    private String tl$translateName(String original) {
        if (original.equals("Large chest")) {
            return "container.chestDouble";
        }

        return original;
    }
}
