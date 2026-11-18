package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.menu.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Conditional(minecraftVersion = @Version("<13w47a"))
@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin {

    @Conditional(minecraftVersion = @Version("<11w49a"))
    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 0))
    public String init$lang1(String string) {
        return L10n.get("backport.menu.returnToMenu");
    }

    @Conditional(minecraftVersion = @Version("<11w49a"))
    @Definition(id = "message", field = "Lnet/minecraft/client/gui/widget/ButtonWidget;message:Ljava/lang/String;")
    @Expression("?.message = ?")
    @WrapOperation(method = "init", at = @At("MIXINEXTRAS:EXPRESSION"))
    public void init$lang2(ButtonWidget instance, String value, Operation<Void> original) {
        original.call(instance, L10n.get("backport.menu.disconnect"));
    }

    @Conditional(minecraftVersion = @Version("<11w49a"))
    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 1))
    public String init$lang3(String string) {
        return L10n.get("backport.menu.returnToGame");
    }

    @Conditional(minecraftVersion = @Version("<11w49a"))
    @ModifyArg(method = "init", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 2))
    public String init$lang4(String string) {
        return L10n.get("menu.options");
    }

//    @ModifyArg(method = "render", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/GameMenuScreen;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
//    public String render$lang1(String string) {
//        return I18n.getTranslation("menu.saving");
//    }
//

    @Conditional(minecraftVersion = @Version("<13w47a"))
    @ModifyArg(method = "render", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/menu/GameMenuScreen;drawCenteredString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"))
    public String render$lang2(String string) {
        return L10n.get("backport.menu.game");
    }
}
