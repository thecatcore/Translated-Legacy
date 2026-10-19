package fr.catcore.translatedlegacy.ornithe.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.stat.achievement.AchievementStat;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AchievementStat.class)
public class AchievementStatMixin {
    @WrapOperation(method = {
            "<init>(ILjava/lang/String;IILnet/minecraft/item/ItemStack;Lnet/minecraft/stat/achievement/AchievementStat;)V"
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/locale/I18n;translate(Ljava/lang/String;)Ljava/lang/String;"))
    private static String tl$cancelTranslationNew(String key, Operation<String> original) {
        return key;
    }

    @Environment(EnvType.CLIENT)
    @ModifyExpressionValue(method = "getDescription", at = @At(value = "FIELD", target = "Lnet/minecraft/stat/achievement/AchievementStat;description:Ljava/lang/String;", opcode = Opcodes.GETFIELD))
    private String tl$translateDescription(String original) {
        return L10n.getOrDefault(original, original);
    }
}
