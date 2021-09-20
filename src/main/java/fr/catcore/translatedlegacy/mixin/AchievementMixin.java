package fr.catcore.translatedlegacy.mixin;

import fr.catcore.translatedlegacy.accessor.StatAccessor;
import fr.catcore.translatedlegacy.language.LanguageManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.FormattedString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Achievement.class)
public class AchievementMixin {

    @Shadow private FormattedString formattedString;
    @Unique
    private String stringId;

    @Inject(method = "<init>(ILjava/lang/String;IILnet/minecraft/item/ItemInstance;Lnet/minecraft/achievement/Achievement;)V", at = @At("RETURN"))
    public void getId$ctr(int i, String string, int j, int k, ItemInstance arg, Achievement arg1, CallbackInfo ci) {
        this.stringId = string;

        LanguageManager.registerCallback(code -> {
            ((StatAccessor)(Achievement)(Object) this).setName(I18n.translate("achievement." + this.stringId));
        });
    }

    /**
     * @author Cat Core
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public String getDescription() {
        String description = I18n.translate("achievement." + this.stringId + ".desc");
        return this.formattedString != null ? this.formattedString.format(description) : description;
    }
}
