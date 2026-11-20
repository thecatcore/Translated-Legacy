package fr.catcore.translatedlegacy.ornithe.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen;
import net.minecraft.client.resource.language.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(LanguageOptionsScreen.C_07646499.C_65618544.class)
public interface LanguageOptionScreenEntryAccessor {
    @Invoker("<init>")
    static LanguageOptionsScreen.C_07646499.C_65618544 create(LanguageOptionsScreen.C_07646499 c_07646499, Language language) {
        throw new UnsupportedOperationException();
    }
}
