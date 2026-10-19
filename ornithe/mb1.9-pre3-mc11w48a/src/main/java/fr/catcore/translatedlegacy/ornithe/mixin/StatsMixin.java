package fr.catcore.translatedlegacy.ornithe.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.stat.Stats;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Conditional(minecraftVersion = @Version(">=b1.4-1507"))
@Mixin(Stats.class)
public class StatsMixin {
    @WrapOperation(method = {
            "<clinit>"
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/locale/I18n;translate(Ljava/lang/String;)Ljava/lang/String;"))
    private static String tl$cancelTranslation(String key, Operation<String> original) {
        return key;
    }

    @WrapOperation(method = {
            "initItemsCraftedStats",
            "initBlocksMinedStats",
            "initItemsUsedStats",
            "initItemsBrokenStats"
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/locale/I18n;translate(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"))
    private static String tl$cancelTranslationWithArgs(String key, Object[] args, Operation<String> original) {
        return key;
    }
}
