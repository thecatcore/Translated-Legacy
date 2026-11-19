package fr.catcore.translatedlegacy.babric.mixin.client.inventory;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fr.catcore.translatedlegacy.babric.TranslationHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.DoubleInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(DoubleInventory.class)
public class DoubleInventoryMixin {
    @ModifyReturnValue(method = "getName", at = @At("RETURN"))
    private String tl$translateName(String original) {
        if (original.equals("Large chest")) {
            return TranslationHelper.get("backport.container.chestDouble", "container.chestDouble");
        }

        return original;
    }
}
