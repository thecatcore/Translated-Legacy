package fr.catcore.translatedlegacy.ornithe.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.impl.language.SimpleLanguageMetadata;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Locale;

@Mixin(SimpleLanguageMetadata.Serializer.class)
public class SimpleLanguageMetadataSerializerMixin {
    @WrapOperation(method = "deserialize(Lcom/google/gson/JsonObject;)Lnet/ornithemc/osl/localization/api/language/LanguageMetadata;", at = @At(value = "INVOKE", target = "Lnet/ornithemc/osl/localization/api/language/Language$Factory;create(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Lnet/ornithemc/osl/localization/api/language/Language;"))
    private Language tl$fixLanguageCode(Language.Factory instance, String code, String region, String name, boolean bidi, Operation<Language> original) {
        if (ResourcePacks.getSupportedFormat() < 3) {
            boolean hasRegion = code.contains("_");
            if (hasRegion) {
                code = code.split("_")[0] + "_" + code.split("_")[1].toUpperCase(Locale.ENGLISH);
            }
        }

        return original.call(instance, code, region, name, bidi);
    }
}
