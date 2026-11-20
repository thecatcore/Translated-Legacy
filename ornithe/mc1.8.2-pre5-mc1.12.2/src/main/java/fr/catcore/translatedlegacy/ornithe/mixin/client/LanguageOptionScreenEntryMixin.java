package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.language.FilterableEntryList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen;
import net.minecraft.client.gui.widget.ListWidget;
import net.minecraft.client.resource.language.Language;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

@Environment(EnvType.CLIENT)
@Mixin(LanguageOptionsScreen.LanguageSelectionListWidget.class)
public abstract class LanguageOptionScreenEntryMixin extends ListWidget implements FilterableEntryList {
    @Shadow
    @Final
    private List<String> languageCodes;

    @Shadow
    @Final
    LanguageOptionsScreen f_29342237;

    public LanguageOptionScreenEntryMixin(Minecraft minecraft, int width, int height, int minY, int maxY, int itemHeight) {
        super(minecraft, width, height, minY, maxY, itemHeight);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ListWidget;<init>(Lnet/minecraft/client/Minecraft;IIIII)V"), index = 3)
    private static int tl$modifyListHeight(int k) {
        return k + 20;
    }

    @Override
    public void tl$filter(String text) {
        this.languageCodes.clear();

        for (Language language : f_29342237.languageManager.getLanguages()) {
            String entryTitle = (language.getCode() + " - " + language.toString()).toLowerCase(Locale.ROOT);

            if (!text.isEmpty() && !entryTitle.contains(text.toLowerCase(Locale.ROOT))) continue;

            languageCodes.add(language.getCode());
        }
    }
}
