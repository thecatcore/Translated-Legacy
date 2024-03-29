package fr.catcore.translatedlegacy.babric.mixin.client.container;

import net.minecraft.class_585;
import net.minecraft.class_629;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(class_585.class)
public abstract class PlayerInventoryScreenMixin {

    @ModifyArg(
            method = "drawForeground",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;III)V"
            )
    )
    public String renderForeground$lang(String string) {
        return class_629.method_2049("container.crafting");
    }
}
