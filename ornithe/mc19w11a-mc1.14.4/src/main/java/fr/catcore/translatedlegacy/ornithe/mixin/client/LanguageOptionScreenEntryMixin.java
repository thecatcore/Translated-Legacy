package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.language.FilterableEntryList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.resource.language.Language;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Locale;

@Environment(EnvType.CLIENT)
@Mixin(LanguageOptionsScreen.C_07646499.class)
public class LanguageOptionScreenEntryMixin extends EntryListWidget<LanguageOptionsScreen.C_07646499.C_65618544> implements FilterableEntryList {

    @Shadow
    @Final
    LanguageOptionsScreen f_74076424;

    public LanguageOptionScreenEntryMixin(Minecraft minecraft, int width, int height, int minY, int maxY, int itemHeight) {
        super(minecraft, width, height, minY, maxY, itemHeight);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/unmapped/C_08318585;<init>(Lnet/minecraft/client/Minecraft;IIIII)V"), index = 3)
    private static int tl$modifyListHeight(int k) {

        return k + 20;
    }

    @Override
    public void tl$filter(String text) {
        this.clear();

        for(Language language : f_74076424.languageManager.getLanguages()) {
            String entryTitle = (language.getCode() + " - " + language.toString()).toLowerCase(Locale.ROOT);

            if (!text.isEmpty() && !entryTitle.contains(text.toLowerCase(Locale.ROOT))) continue;

            LanguageOptionsScreen.C_07646499.C_65618544 c_65618544 = LanguageOptionScreenEntryAccessor.create((LanguageOptionsScreen.C_07646499) (Object) this, language);
            this.add(c_65618544);
            if (f_74076424.languageManager.getLanguage().getCode().equals(language.getCode())) {
                this.setSelected(c_65618544);
            }
        }

        if (this.getSelected() != null) {
            this.m_47225689(this.getSelected());
        }
    }
}
