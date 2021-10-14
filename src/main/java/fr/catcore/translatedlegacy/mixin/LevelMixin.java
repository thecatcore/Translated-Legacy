package fr.catcore.translatedlegacy.mixin;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Level.class)
public class LevelMixin {

    @ModifyArg(method = "saveLevel", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ProgressListener;notifyIgnoreGameRunning(Ljava/lang/String;)V"))
    private String saveLevel$translate(String string) {
        return I18n.translate("menu.saving");
    }


    @ModifyArg(method = "saveLevel", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ProgressListener;notifySubMessage(Ljava/lang/String;)V"))
    private String saveLevel$translate2(String string) {
        return I18n.translate("menu.saving");
    }
}
