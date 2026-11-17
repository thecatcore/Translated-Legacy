package fr.catcore.translatedlegacy.babric.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.catcore.translatedlegacy.babric.NetworkingHelper;
import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {

    @WrapOperation(method = "keyPressed", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I"))
    public int fixCharInput(String instance, int ch, Operation<Integer> original) {
        return (NetworkingHelper.serverSupportsUnicode || !minecraft.isWorldRemote()) ? TextUtils.isCharacterAllowed(ch) : original.call(instance, ch);
    }
}
