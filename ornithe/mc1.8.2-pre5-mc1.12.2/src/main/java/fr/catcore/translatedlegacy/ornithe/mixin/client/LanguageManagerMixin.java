package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.catcore.translatedlegacy.TranslatedLegacy;
import net.minecraft.client.resource.language.Language;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.resource.manager.ResourceManager;
import net.minecraft.client.resource.metadata.ResourceMetadataSection;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializers;
import net.minecraft.client.resource.pack.ResourcePack;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;
import java.util.Objects;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @Shadow
    private String currentCode;

    @WrapOperation(method = "reload(Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/pack/ResourcePack;getMetadataSection(Lnet/minecraft/client/resource/metadata/ResourceMetadataSerializers;Ljava/lang/String;)Lnet/minecraft/client/resource/metadata/ResourceMetadataSection;"))
    private ResourceMetadataSection tl$disableDefaultLanguages(ResourcePack instance, ResourceMetadataSerializers resourceMetadataSerializers, String s, Operation<ResourceMetadataSection> original) {
        if (instance.getName().equals("Default")) return null;

        return original.call(instance, resourceMetadataSerializers, s);
    }

    @Inject(method = "reload(Lnet/minecraft/client/resource/manager/ResourceManager;)V", at = @At("HEAD"))
    private void tl$setLanguage$reload(ResourceManager resourceManager, CallbackInfo ci) {
        if (!Objects.equals(currentCode, "en_us") && !Objects.equals(currentCode, "en_US")) {
            TranslatedLegacy.getLangFile(MinecraftVersion.resolve().gameVersion(), currentCode.toLowerCase(Locale.ENGLISH));
        }
    }

    @Inject(method = "setLanguage", at = @At("HEAD"))
    private void tl$setLanguage$self(Language language, CallbackInfo ci) {
        if (!Objects.equals(language.getCode(), "en_us") && !Objects.equals(language.getCode(), "en_US")) {
            TranslatedLegacy.getLangFile(MinecraftVersion.resolve().gameVersion(), language.getCode().toLowerCase(Locale.ENGLISH));
        }
    }
}
