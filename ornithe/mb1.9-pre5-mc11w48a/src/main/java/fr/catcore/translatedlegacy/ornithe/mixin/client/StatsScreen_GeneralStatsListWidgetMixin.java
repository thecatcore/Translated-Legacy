package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.gui.screen.menu.StatsScreen__GeneralStatsListWidget")
public class StatsScreen_GeneralStatsListWidgetMixin {
    @ModifyExpressionValue(method = "renderEntry", at = @At(value = "FIELD", target = "Lnet/minecraft/stat/Stat;key:Ljava/lang/String;", opcode = Opcodes.GETFIELD))
    private String tl$translate(String original) {
        return L10n.get(original);
    }
}
