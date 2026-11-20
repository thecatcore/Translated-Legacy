package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.language.FilterableEntryList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen__LanguageSelectionListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LanguageOptionsScreen.class)
public class LanguageOptionsScreenMixin extends Screen {

    @Shadow
    private LanguageOptionsScreen__LanguageSelectionListWidget languageSelectionListWidget;

    @Unique
    private TextFieldWidget searchInput;
    private String searchQuery = "";

    @Inject(method = "init", at = @At("RETURN"))
    private void tl$initSearchInput(CallbackInfo ci) {
        searchInput = new TextFieldWidget(this, this.textRenderer, this.width / 2 - 100, 30, 200, 20, searchQuery);
        searchInput.setMaxLength(30);
        ((FilterableEntryList) languageSelectionListWidget).tl$filter(searchQuery);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void tl$renderSearchInput(int i, int j, float f, CallbackInfo ci) {
        searchInput.render();
    }

    @Override
    public void tick() {
        searchInput.tick();
    }

    @Override
    protected void keyPressed(char chr, int key) {
        if (searchInput.focused) {
            searchInput.keyPressed(chr, key);
            searchQuery = searchInput.getText();
            ((FilterableEntryList) languageSelectionListWidget).tl$filter(searchQuery);
            return;
        }

        super.keyPressed(chr, key);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void tl$mouseClicked(int mouseX, int mouseY, int button, CallbackInfo ci) {
        searchInput.mouseClicked(mouseX, mouseY, button);
    }
}
