package fr.catcore.translatedlegacy.mixin.inventory;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DoubleInventory.class)
public class DoubleInventoryMixin {

    @Shadow private String name;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void translateName(String string, Inventory arg, Inventory arg1, CallbackInfo ci) {
        if (string.equals("Large chest")) {
            this.name = I18n.translate("container.chestDouble");
        }
    }
}
