package fr.catcore.translatedlegacy.stapi.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.catcore.translatedlegacy.stapi.TranslatedLegacyNetworkHandler;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    @WrapOperation(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/language/TranslationStorage;get(Ljava/lang/String;)Ljava/lang/String;"))
    private String tl$fixServerMessagesTranslation(TranslationStorage instance, String s, Operation<String> original) {
        // Translated Legacy handle translation keys on the client side
        if (((TranslatedLegacyNetworkHandler) this.networkHandler).isTranslationDelegated()) {
            return s;
        }

        // Original clients assume translation is done on the server side
        return original.call(instance, s);
    }
}
