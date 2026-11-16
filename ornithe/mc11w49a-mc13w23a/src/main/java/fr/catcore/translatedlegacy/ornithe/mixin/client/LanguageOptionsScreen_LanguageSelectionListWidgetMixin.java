package fr.catcore.translatedlegacy.ornithe.mixin.client;

import net.minecraft.locale.Language;
import net.ornithemc.osl.localization.impl.Localization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.TreeMap;

@Mixin(targets = "net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen__LanguageSelectionListWidget")
public class LanguageOptionsScreen_LanguageSelectionListWidgetMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/locale/Language;getLanguages()Ljava/util/TreeMap;"))
    private TreeMap<String, String> tl$dynamicLanguageList(Language instance) {
        TreeMap<String, String> map = new TreeMap<>();

        for (net.ornithemc.osl.localization.api.language.Language language : Localization.getLanguageManager().getLanguages()) {
            map.put(language.code(), language.name() + " (" + language.region() + ")");
        }

        return map;
    }
}
