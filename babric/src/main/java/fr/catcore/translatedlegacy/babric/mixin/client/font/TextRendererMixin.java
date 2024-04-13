package fr.catcore.translatedlegacy.babric.mixin.client.font;

import fr.catcore.translatedlegacy.babric.TranslatedLegacyBabricClient;
import fr.catcore.translatedlegacy.babric.mixin.client.GameOptionsAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void c$init(GameOptions arg, String string, TextureManager arg1, CallbackInfo ci) {
        if (!FabricLoader.getInstance().isModLoaded("translated-legacy-stapi"))
            TranslatedLegacyBabricClient.setGameProvider(((GameOptionsAccessor)arg).getMinecraft());
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public void draw(String string, int i, int j, int k, boolean flag) {
        fr.catcore.translatedlegacy.font.TextRenderer.draw(string, i, j, k, flag);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public int getWidth(String string) {
        return fr.catcore.translatedlegacy.font.TextRenderer.getTextWidth(string);
    }

//    /**
//     * @author CatCore
//     * @reason We don't want any of the original code to run.
//     */
//    @Overwrite
//    public void method_1904(String string, int i, int j, int k, int i1) {
//        TranslatedLegacyBabric.TEXT_RENDERER.drawText(string, i, j, k, i1);
//    }
//
//    /**
//     * @author CatCore
//     * @reason We don't want any of the original code to run.
//     */
//    @Overwrite
//    public int method_1902(String string, int i) {
//        return TranslatedLegacyBabric.TEXT_RENDERER.getLineWidth(string, i);
//    }
}
