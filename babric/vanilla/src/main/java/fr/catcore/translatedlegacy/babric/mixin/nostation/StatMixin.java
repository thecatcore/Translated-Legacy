package fr.catcore.translatedlegacy.babric.mixin.nostation;

import fr.catcore.translatedlegacy.babric.accessor.StatAccessor;
import fr.catcore.translatedlegacy.babric.language.LanguageManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.stat.SimpleStat;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Stat.class)
public class StatMixin implements StatAccessor {
    @Mutable
    @Shadow @Final public String stringId;

    @Unique
    private String tl$stringId;

    @Override
    public void setName(String name) {
        this.stringId = name;
    }

    @Inject(method = "<init>(ILjava/lang/String;Lnet/minecraft/stat/StatFormatter;)V", at = @At("RETURN"))
    public void init$callback(int string, String stringId, StatFormatter par3, CallbackInfo ci) {
        if (((Object)this) instanceof SimpleStat) {
            this.tl$stringId = stringId;
            this.setName(TranslationStorage.getInstance().get("stat." + this.tl$stringId));
            LanguageManager.registerCallback(code -> {
                this.setName(TranslationStorage.getInstance().get("stat." + this.tl$stringId));
            });
        }
    }
}
