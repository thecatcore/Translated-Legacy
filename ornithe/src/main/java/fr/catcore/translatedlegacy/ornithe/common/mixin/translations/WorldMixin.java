package fr.catcore.translatedlegacy.ornithe.common.mixin.translations;

import fr.catcore.translatedlegacy.ornithe.common.TranslationHelper;
import net.minecraft.world.World;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.localization.api.L10n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Conditional(minecraftVersion = @Version("<12w18a"))
@Mixin(World.class)
public class WorldMixin {

    @ModifyArg(method = "save", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ProgressListener;progressStartNoAbort(Ljava/lang/String;)V"))
    private String saveLevel$translate(String string) {
        return L10n.get("menu.savingLevel");
    }


    @ModifyArg(method = "save", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ProgressListener;progressStage(Ljava/lang/String;)V"))
    private String saveLevel$translate2(String string) {
        return TranslationHelper.get("backport.menu.savingChunks", "menu.savingChunks");
    }
}
