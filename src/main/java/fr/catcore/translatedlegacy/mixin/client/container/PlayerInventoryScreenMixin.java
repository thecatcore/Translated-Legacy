package fr.catcore.translatedlegacy.mixin.client.container;

import net.minecraft.client.gui.screen.container.ContainerScreen;
import net.minecraft.client.gui.screen.container.PlayerInventoryScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.container.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlayerInventoryScreen.class)
public abstract class PlayerInventoryScreenMixin extends ContainerScreen {

    public PlayerInventoryScreenMixin(Container arg) {
        super(arg);
    }

    /**
     * @author CatCore
     */
    @Overwrite
    public void renderForeground() {
        this.textManager.drawText(I18n.translate("container.crafting"), 86, 16, 4210752);
    }
}
