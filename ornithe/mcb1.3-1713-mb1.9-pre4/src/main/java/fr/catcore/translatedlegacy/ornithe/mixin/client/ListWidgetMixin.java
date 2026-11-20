package fr.catcore.translatedlegacy.ornithe.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import net.minecraft.client.gui.widget.ListWidget;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ListWidget.class)
public class ListWidgetMixin {
    @Shadow
    private float scrollAmount;

    @Shadow
    @Final
    protected int entryHeight;

    @Definition(id = "mouseYStart", field = "Lnet/minecraft/client/gui/widget/ListWidget;mouseYStart:F")
    @Expression("?.mouseYStart = -1.0")
    @Inject(method = "render", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void tl$fixMouseScrolling(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        while (Mouse.next()) {
            int var7 = Mouse.getEventDWheel();
            if (var7 != 0) {
                if (var7 > 0) {
                    var7 = -1;
                } else if (var7 < 0) {
                    var7 = 1;
                }

                this.scrollAmount += (float)(var7 * this.entryHeight / 2);
            }
        }
    }
}
