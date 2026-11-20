package fr.catcore.translatedlegacy.babric.mixin.client.gui.widget;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryListWidget.class)
public class EntryListWidgetMixin {
    @Shadow
    private float scrollAmount;

    @Shadow
    @Final
    protected int itemHeight;

    @Definition(id = "mostYStart", field = "Lnet/minecraft/client/gui/widget/EntryListWidget;mostYStart:F")
    @Expression("?.mostYStart = -1.0")
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

                this.scrollAmount += (float)(var7 * this.itemHeight / 2);
            }
        }
    }
}
