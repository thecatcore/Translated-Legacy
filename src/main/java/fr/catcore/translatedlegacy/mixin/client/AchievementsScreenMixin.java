package fr.catcore.translatedlegacy.mixin.client;

import net.minecraft.class_609;
import net.minecraft.class_629;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(class_609.class)
public class AchievementsScreenMixin {

    @ModifyArg(method = "method_1999", index = 0, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;III)V"
    ))
    public String method_1999$lang(String string) {
        return class_629.method_2049("gui.achievements");
    }
}
