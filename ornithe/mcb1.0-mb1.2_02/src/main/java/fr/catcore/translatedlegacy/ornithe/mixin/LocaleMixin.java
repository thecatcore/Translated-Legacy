package fr.catcore.translatedlegacy.ornithe.mixin;

import net.ornithemc.osl.localization.impl.Locale;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Mixin(Locale.class)
public abstract class LocaleMixin {
    @Shadow
    public abstract void loadFromResources(String path, List<InputStream> resources);

    @Inject(method = "loadLanguage", at = @At("HEAD"))
    private void tl$fixMissingTranslations(ResourceManager resourceManager, String language, CallbackInfo ci) {
        if (language.equals("en_US")) {
            try {
                this.loadFromResources("lang/stats_US.lang", resourceManager.getResourceStack("lang/stats_US.lang"));
            } catch (IOException ignored) {
            }
        }
    }
}
