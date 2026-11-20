package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.resource.language.Language;
import net.minecraft.client.resource.metadata.serializer.LanguageMetadataSerializer;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Locale;

@Mixin(LanguageMetadataSerializer.class)
public class LanguageMetadataSerializerMixin {
    @Definition(id = "Language", type = net.minecraft.client.resource.language.Language.class)
    @Expression("new Language(?, ?, ?, ?)")
    @WrapOperation(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/resource/metadata/LanguageMetadata;",
            at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private Language tl$fixLanguageCode(String code, String name, String region, boolean bidirectional, Operation<Language> original) {
        if (ResourcePacks.getSupportedFormat() < 3) {
            boolean hasRegion = code.contains("_");
            if (hasRegion) {
                code = code.split("_")[0] + "_" + code.split("_")[1].toUpperCase(Locale.ENGLISH);
            }
        }

        return original.call(code, name, region, bidirectional);
    }
}
