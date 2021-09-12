package fr.catcore.translatedlegacy.mixin.client.container;

import net.minecraft.client.gui.screen.container.ContainerScreen;
import net.minecraft.client.gui.screen.container.DispenserScreen;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.container.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DispenserScreen.class)
public abstract class DispenserScreenMixin extends ContainerScreen {

    public DispenserScreenMixin(Container arg) {
        super(arg);
    }

    /**
     * @author CatCore
     */
    @Overwrite
    protected void renderForeground() {
        TranslationStorage translationStorage = TranslationStorage.getInstance();

        this.textManager.drawText(translationStorage.translate("container.dispenser"), 60, 6, 4210752);
        this.textManager.drawText(translationStorage.translate("container.inventory"), 8, this.containerHeight - 96 + 2, 4210752);
    }
}
