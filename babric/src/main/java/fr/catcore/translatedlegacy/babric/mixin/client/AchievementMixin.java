package fr.catcore.translatedlegacy.babric.mixin.client;

import fr.catcore.translatedlegacy.babric.accessor.StatAccessor;
import fr.catcore.translatedlegacy.babric.language.LanguageManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.achievement.Achievement;
import net.minecraft.class_241;
import net.minecraft.class_629;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Achievement.class)
public class AchievementMixin {

    @Shadow private class_241 field_1208;
    @Unique
    private String stringId;

    @Inject(method = "<init>(ILjava/lang/String;IILnet/minecraft/item/ItemStack;Lnet/minecraft/achievement/Achievement;)V", at = @At("RETURN"))
    public void getId$ctr(int string, String j, int k, int arg, ItemStack arg2, Achievement par6, CallbackInfo ci) {
        this.stringId = j;

        LanguageManager.registerCallback(code -> {
            ((StatAccessor)(Achievement)(Object) this).setName(class_629.method_2049("achievement." + this.stringId));
        });
    }

    /**
     * @author Cat Core
     * @reason We don't want any of the original code to run.
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public String method_1043() {
        String description = class_629.method_2049("achievement." + this.stringId + ".desc");
        return this.field_1208 != null ? this.field_1208.method_809(description) : description;
    }
}
