package fr.catcore.translatedlegacy.babric.mixin;

import fr.catcore.translatedlegacy.babric.accessor.StatAccessor;
import fr.catcore.translatedlegacy.babric.language.LanguageManager;
import net.minecraft.class_498;
import net.minecraft.class_542;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Stat.class)
public class StatMixin implements StatAccessor {
    @Mutable
    @Shadow @Final public String field_2026;

    @Unique
    private String stringId;

    @Override
    public void setName(String name) {
        this.field_2026 = name;
    }

    @Inject(method = "<init>(ILjava/lang/String;Lnet/minecraft/class_498;)V", at = @At("RETURN"))
    public void init$callback(int string, String stringId, class_498 par3, CallbackInfo ci) {
        if (((Object)this) instanceof class_542) {
            this.stringId = stringId;
            ((StatAccessor)(Object)this).setName(TranslationStorage.getInstance().get("stat." + this.stringId));
            LanguageManager.registerCallback(code -> {
                ((StatAccessor)(Object)this).setName(TranslationStorage.getInstance().get("stat." + this.stringId));
            });
        }
    }
}
