package fr.catcore.translatedlegacy.mixin.compat;

import net.minecraft.client.gui.screen.container.ContainerScreen;
import net.minecraft.client.gui.screen.container.PlayerInventoryScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.container.Container;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Pseudo
@Mixin(PlayerInventoryScreen.class)
public abstract class CreativePlayerInventoryMixin extends ContainerScreen {

    @Shadow
    private float mouseX;
    @Shadow
    private float mouseY;

    public CreativePlayerInventoryMixin(Container arg) {
        super(arg);
    }

    private void creative_renderString(String string) {
        if (string != null) {
            if (Objects.equals(string, "Inventory")) string = I18n.translate("container.inventory");
            else if (Objects.equals(string, "Creative")) string = I18n.translate("title.creative:selectEorld.creative");
            GL11.glDisable(2929);
            if (string.length() > 0) {
                int var9 = (int)this.mouseX + 12;
                int var10 = (int)this.mouseY - 12;
                int var11 = this.textManager.getTextWidth(string);
                this.fillGradient(var9 - 3, var10 - 3, var9 + var11 + 3, var10 + 8 + 3, -1073741824, -1073741824);
                this.textManager.drawTextWithShadow(string, var9, var10, -1);
            }

            GL11.glEnable(2929);
        }
    }
}
