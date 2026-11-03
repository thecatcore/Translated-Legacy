package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.ToastGui;
import net.ornithemc.osl.localization.api.L10n;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ToastGui.class)
public class ToastGuiMixin {
    @ModifyExpressionValue(method = {"set", "setTutorial"}, at = {
            @At(value = "FIELD", target = "Lnet/minecraft/stat/achievement/AchievementStat;key:Ljava/lang/String;", opcode = Opcodes.GETFIELD)
    })
    private String tl$translate(String original) {
        return L10n.get(original);
    }
}
