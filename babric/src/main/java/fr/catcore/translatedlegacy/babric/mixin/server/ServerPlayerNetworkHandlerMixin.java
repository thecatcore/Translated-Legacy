package fr.catcore.translatedlegacy.babric.mixin.server;

import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayerNetworkHandlerMixin {
    @Redirect(method = "method_1431", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", remap = false))
    private int allowMoreCharactersInChat(String instance, int ch) {
        return TextUtils.isCharacterAllowed(ch);
    }
}
