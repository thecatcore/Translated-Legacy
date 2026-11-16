package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.resource.metadata.ResourceMetadataSection;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializers;
import net.minecraft.client.resource.pack.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @WrapOperation(method = "reload(Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/pack/ResourcePack;getMetadataSection(Lnet/minecraft/client/resource/metadata/ResourceMetadataSerializers;Ljava/lang/String;)Lnet/minecraft/client/resource/metadata/ResourceMetadataSection;"))
    private ResourceMetadataSection tl$disableDefaultLanguages(ResourcePack instance, ResourceMetadataSerializers resourceMetadataSerializers, String s, Operation<ResourceMetadataSection> original) {
        System.out.println(instance + " " + instance.getName());
        return original.call(instance, resourceMetadataSerializers, s);
    }
}
