package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.catcore.translatedlegacy.TranslatedLegacy;
import net.minecraft.client.resource.language.Language;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.resource.metadata.LanguageMetadata;
import net.minecraft.client.resource.metadata.serializer.ResourceMetadataSerializer;
import net.minecraft.resource.manager.ResourceManager;
import net.minecraft.resource.pack.Pack;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @Shadow
    private String currentCode;

    @WrapOperation(method = "reload(Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/pack/Pack;getMetadataSection(Lnet/minecraft/client/resource/metadata/serializer/ResourceMetadataSerializer;)Ljava/lang/Object;"))
    private Object tl$disableDefaultLanguages(Pack instance, ResourceMetadataSerializer<LanguageMetadata> tResourceMetadataSerializer, Operation<LanguageMetadata> original) {
        if (instance.getName().equals("Default")) return null;

        return original.call(instance, tResourceMetadataSerializer);
    }

    @Inject(method = "reload(Lnet/minecraft/resource/manager/ResourceManager;)V", at = @At("HEAD"))
    private void tl$setLanguage$reload(ResourceManager resourceManager, CallbackInfo ci) {
        if (!Objects.equals(currentCode, "en_us")) {
            TranslatedLegacy.getLangFile(MinecraftVersion.resolve().gameVersion(), currentCode);
        }
    }

    @Inject(method = "setLanguage", at = @At("HEAD"))
    private void tl$setLanguage$self(Language language, CallbackInfo ci) {
        if (!Objects.equals(language.getCode(), "en_us")) {
            TranslatedLegacy.getLangFile(MinecraftVersion.resolve().gameVersion(), language.getCode());
        }
    }
}
