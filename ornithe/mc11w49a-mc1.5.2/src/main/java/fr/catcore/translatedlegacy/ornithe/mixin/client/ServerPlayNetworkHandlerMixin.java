package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.catcore.translatedlegacy.ornithe.common.NetworkingHelper;
import fr.catcore.translatedlegacy.util.TextUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import net.ornithemc.osl.lifecycle.api.client.MinecraftInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//@Conditional(minecraftVersion = @Version(">=12w18a"))
@Conditional(minecraftVersion = @Version(">=12w24a"))
@Environment(EnvType.CLIENT)
@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @WrapOperation(method = {"handleSignBlockEntityUpdate"}, at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", remap = false))
    private int allowMoreCharactersInChat(String instance, int ch, Operation<Integer> original) {
        return NetworkingHelper.serverSupportsUnicode || MinecraftInstance.get().isSingleplayer() ? TextUtils.isCharacterAllowed(ch) : original.call(instance, ch);
    }
}
