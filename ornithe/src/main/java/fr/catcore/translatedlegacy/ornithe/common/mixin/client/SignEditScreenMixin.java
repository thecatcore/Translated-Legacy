package fr.catcore.translatedlegacy.ornithe.common.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.catcore.translatedlegacy.ornithe.common.NetworkingHelper;
import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.client.gui.screen.game.SignEditScreen;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Conditional(minecraftVersion = @Version("<12w24a"))
@Mixin(SignEditScreen.class)
public class SignEditScreenMixin {
    @WrapOperation(method = "keyPressed", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I"))
    public int fixCharInput(String instance, int ch, Operation<Integer> original) {
        return NetworkingHelper.serverSupportsUnicode || !ClientPlayNetworking.isPlayReady() ? TextUtils.isCharacterAllowed(ch) : original.call(instance, ch);
    }
}
