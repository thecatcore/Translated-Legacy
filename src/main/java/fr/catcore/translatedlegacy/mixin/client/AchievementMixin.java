package fr.catcore.translatedlegacy.mixin.client;

import fr.catcore.translatedlegacy.accessor.StatAccessor;
import fr.catcore.translatedlegacy.language.LanguageManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_24;
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

@Mixin(class_24.class)
public class AchievementMixin {

    @Shadow private class_241 field_1208;
    @Unique
    private String stringId;

    @Inject(method = "<init>(ILjava/lang/String;IILnet/minecraft/item/ItemStack;Lnet/minecraft/class_24;)V", at = @At("RETURN"))
    public void getId$ctr(int j, String string, int k, int arg, ItemStack arg2, class_24 par6, CallbackInfo ci) {
        this.stringId = string;

        LanguageManager.registerCallback(code -> {
            ((StatAccessor)(class_24)(Object) this).setName(class_629.method_2049("achievement." + this.stringId));
        });
    }

    /**
     * @author Cat Core
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public String method_1043() {
        String description = class_629.method_2049("achievement." + this.stringId + ".desc");
        return this.field_1208 != null ? this.field_1208.method_809(description) : description;
    }
}
