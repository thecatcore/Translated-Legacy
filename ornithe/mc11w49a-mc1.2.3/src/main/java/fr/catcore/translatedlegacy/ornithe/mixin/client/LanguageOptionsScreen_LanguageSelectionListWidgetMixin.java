package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.language.FilterableEntryList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen__LanguageSelectionListWidget;
import net.minecraft.locale.Language;
import net.ornithemc.osl.localization.impl.Localization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Environment(EnvType.CLIENT)
@Mixin(LanguageOptionsScreen__LanguageSelectionListWidget.class)
public class LanguageOptionsScreen_LanguageSelectionListWidgetMixin implements FilterableEntryList {
    @Shadow
    private ArrayList languageCodes;

    @Shadow
    private TreeMap languages;

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ListWidget;<init>(Lnet/minecraft/client/Minecraft;IIIII)V"), index = 3)
    private static int tl$modifyListHeight(int k) {
        return k + 20;
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/locale/Language;getLanguages()Ljava/util/TreeMap;"))
    private TreeMap<String, String> tl$dynamicLanguageList(Language instance) {
        TreeMap<String, String> map = new TreeMap<>();

        for (net.ornithemc.osl.localization.api.language.Language language : Localization.getLanguageManager().getLanguages()) {
            map.put(language.code(), language.name() + " (" + language.region() + ")");
        }

        return map;
    }

    @Override
    public void tl$filter(String text) {
        this.languageCodes.clear();

        for (Map.Entry<String, String> entry : ((TreeMap<String, String>) languages).entrySet()) {
            String entryTitle = (entry.getKey() + " - " + entry.getValue()).toLowerCase(Locale.ROOT);

            if (!text.isEmpty() && !entryTitle.contains(text.toLowerCase(Locale.ROOT))) continue;

            languageCodes.add(entry.getKey());
        }
    }
}
