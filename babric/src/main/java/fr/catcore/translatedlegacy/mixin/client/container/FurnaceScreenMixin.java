package fr.catcore.translatedlegacy.mixin.client.container;

import net.minecraft.class_524;
import net.minecraft.class_629;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(class_524.class)
public abstract class FurnaceScreenMixin {

    @ModifyArg(
            method = "drawForeground",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;III)V",
                    ordinal = 0
            )
    )
    public String renderForeground$lang1(String string) {
        return class_629.method_2049("container.furnace");
    }

    @ModifyArg(
            method = "drawForeground",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;III)V",
                    ordinal = 1
            )
    )
    public String renderForeground$lang2(String string) {
        return class_629.method_2049("container.inventory");
    }
}
