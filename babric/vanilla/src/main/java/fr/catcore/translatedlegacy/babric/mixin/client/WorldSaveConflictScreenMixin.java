package fr.catcore.translatedlegacy.babric.mixin.client;

import net.minecraft.client.gui.screen.world.WorldSaveConflictScreen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WorldSaveConflictScreen.class)
public class WorldSaveConflictScreenMixin {

    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V"))
    public String init$lang(String string) {
        return I18n.getTranslation("gui.toMenu");
    }
}
