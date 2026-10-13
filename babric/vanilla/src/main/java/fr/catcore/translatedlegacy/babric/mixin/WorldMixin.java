package fr.catcore.translatedlegacy.babric.mixin;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(World.class)
public class WorldMixin {

    @ModifyArg(method = "saveWithLoadingDisplay", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LoadingDisplay;progressStartNoAbort(Ljava/lang/String;)V"))
    private String saveLevel$translate(String string) {
        return I18n.getTranslation("menu.savingLevel");
    }


    @ModifyArg(method = "saveWithLoadingDisplay", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LoadingDisplay;progressStage(Ljava/lang/String;)V"))
    private String saveLevel$translate2(String string) {
        return I18n.getTranslation("menu.savingChunks");
    }
}
