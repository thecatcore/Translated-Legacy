package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.texture.TextureManager;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(TextRenderer.class)
public abstract class TextRendererMixin {
    @Unique
    private String texturePath;

    @Conditional(minecraftVersion = @Version(">=b1.9-pre3"))
    @Inject(method = "<init>", at = @At("TAIL"))
    private void tl$init(GameOptions options, String fontPath, TextureManager textureManager, CallbackInfo ci) {
        this.texturePath = fontPath;
    }

    private boolean overwrite() {
        return this.texturePath == null || Objects.equals(texturePath, "/font/default.png");
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @WrapMethod(method = "drawLayer")
    public void tl$drawLayer(String text, int x, int y, int color, boolean shadow, Operation<Void> original) {
        if (overwrite()) {
            fr.catcore.translatedlegacy.font.TextRenderer.draw(text, x, y, color, shadow);
            return;
        }

        original.call(text, x, y, color, shadow);
    }

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @WrapMethod(method = "getWidth")
    public int tl$getWidth(String text, Operation<Integer> original) {
        if (overwrite()) {
            return fr.catcore.translatedlegacy.font.TextRenderer.getTextWidth(text);
        }

        return original.call(text);
    }
}
