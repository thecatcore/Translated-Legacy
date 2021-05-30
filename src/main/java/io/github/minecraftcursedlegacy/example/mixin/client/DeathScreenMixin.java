package io.github.minecraftcursedlegacy.example.mixin.client;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {

    /**
     * @author CatCore
     */
    @Overwrite
    public void init() {
        TranslationStorage translationStorage = TranslationStorage.getInstance();

        this.buttons.clear();
        this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 72, translationStorage.translate("deathScreen.respawn")));
        this.buttons.add(new Button(2, this.width / 2 - 100, this.height / 4 + 96, translationStorage.translate("deathScreen.titleScreen")));
        if (this.minecraft.session == null) {
            ((Button)this.buttons.get(1)).active = false;
        }
    }

    /**
     * @author CatCore
     */
    @Overwrite
    public void render(int i, int j, float f) {
        TranslationStorage translationStorage = TranslationStorage.getInstance();

        this.fillGradient(0, 0, this.width, this.height, 1615855616, -1602211792);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        this.drawTextWithShadowCentred(this.textManager, translationStorage.translate("deathScreen.title"), this.width / 2 / 2, 30, 16777215);
        GL11.glPopMatrix();
        this.drawTextWithShadowCentred(this.textManager, translationStorage.translate("deathScreen.score") + ": " + this.minecraft.player.method_481(), this.width / 2, 100, 16777215);
        super.render(i, j, f);
    }
}
