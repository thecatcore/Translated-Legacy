package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fr.catcore.translatedlegacy.ornithe.common.TranslationHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.DoubleInventory;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Conditional(minecraftVersion = @Version("<12w24a"))
@Mixin(DoubleInventory.class)
public class DoubleInventoryMixin {
    @ModifyReturnValue(method = "getInventoryName", at = @At("RETURN"))
    private String tl$translateName(String original) {
        if (original.equals("Large chest")) {
            return TranslationHelper.get("backport.container.chestDouble", "container.chestDouble");
        }

        return original;
    }
}
