package fr.catcore.translatedlegacy.ornithe.mixin.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.catcore.translatedlegacy.ornithe.access.TranslationRecipient;
import net.minecraft.locale.Language;
import net.minecraft.server.entity.mob.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    @WrapOperation(method = "addMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/locale/Language;translate(Ljava/lang/String;)Ljava/lang/String;"))
    private String tl$fixServerMessagesTranslation(Language instance, String key, Operation<String> original) {
        // Original clients assume translation is done on the server side
        if (((TranslationRecipient) this.networkHandler).pretranslate()) {
            return original.call(instance, key);
        }

        // Translated Legacy handle translation keys on the client side
        return key;
    }
}
