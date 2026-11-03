package fr.catcore.translatedlegacy.stapi.mixin;

import net.modificationstation.stationapi.api.resource.language.LanguageManager;
import net.modificationstation.stationapi.api.util.Namespace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @Inject(method = "addPath(Ljava/lang/String;Lnet/modificationstation/stationapi/api/util/Namespace;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void translatedlegacy$addPath(String path, Namespace namespace, CallbackInfo ci) {
        if (namespace == null) namespace = Namespace.MINECRAFT;

        fr.catcore.translatedlegacy.babric.language.LanguageManager.registerFolderToModid(path, namespace.toString());
        ci.cancel();
    }
    @Inject(method = "addPath(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void translatedlegacy$addPath(String path, CallbackInfo ci) {
        fr.catcore.translatedlegacy.babric.language.LanguageManager.registerFolderToModid(path, Namespace.MINECRAFT.toString());
        ci.cancel();
    }
}
