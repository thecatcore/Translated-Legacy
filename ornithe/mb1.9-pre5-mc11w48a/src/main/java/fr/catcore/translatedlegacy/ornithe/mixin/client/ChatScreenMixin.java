package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.catcore.translatedlegacy.ornithe.NetworkingHelper;
import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.client.gui.screen.ChatScreen;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @WrapOperation(method = "keyPressed", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I"))
    public int fixCharInput(String instance, int ch, Operation<Integer> original) {
        return NetworkingHelper.serverSupportsUnicode || ClientPlayNetworking.isPlayReady() ? TextUtils.isCharacterAllowed(ch) : original.call(instance, ch);
    }
}
