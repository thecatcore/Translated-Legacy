package fr.catcore.translatedlegacy.babric.mixin;

import fr.catcore.translatedlegacy.babric.language.LanguageManager;
import net.modificationstation.stationapi.api.lang.I18n;
import net.modificationstation.stationapi.api.registry.ModID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(I18n.class)
public class StAPII18nMixin {
    @Inject(method = "addLangFolder(Lnet/modificationstation/stationapi/api/registry/ModID;Ljava/lang/String;)V",
            at = @At("RETURN"), remap = false)
    private static void addLangFolderToList(ModID modID, String langFolder, CallbackInfo ci) {
        if (modID != null) {
            LanguageManager.registerFolderToModid(langFolder, modID.toString());
        }
    }
}
