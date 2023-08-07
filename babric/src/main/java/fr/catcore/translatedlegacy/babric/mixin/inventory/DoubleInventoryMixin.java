package fr.catcore.translatedlegacy.babric.mixin.inventory;

import net.minecraft.class_320;
import net.minecraft.class_629;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_320.class)
public class DoubleInventoryMixin {

    @Shadow private String field_1215;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void translateName(String string, Inventory arg, Inventory arg1, CallbackInfo ci) {
        if (string.equals("Large chest")) {
            this.field_1215 = class_629.method_2049("container.chestDouble");
        }
    }
}
