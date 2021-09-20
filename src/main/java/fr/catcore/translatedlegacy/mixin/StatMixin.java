package fr.catcore.translatedlegacy.mixin;

import fr.catcore.translatedlegacy.accessor.StatAccessor;
import fr.catcore.translatedlegacy.language.LanguageManager;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.stat.RegisteringStat;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StringFormatter;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Stat.class)
public class StatMixin implements StatAccessor {
    @Mutable
    @Shadow @Final public String name;

    @Unique
    private String stringId;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Inject(method = "<init>(ILjava/lang/String;Lnet/minecraft/stat/StringFormatter;)V", at = @At("RETURN"))
    public void init$callback(int i, String string, StringFormatter arg, CallbackInfo ci) {
        if (((Object)this) instanceof RegisteringStat) {
            this.stringId = string;
            ((StatAccessor)(Object)this).setName(TranslationStorage.getInstance().translate("stat." + this.stringId));
            LanguageManager.registerCallback(code -> {
                ((StatAccessor)(Object)this).setName(TranslationStorage.getInstance().translate("stat." + this.stringId));
            });
        }
    }
}
