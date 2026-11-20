package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.language.FilterableEntryList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen;
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
    private LanguageOptionsScreen.LanguageSelectionListWidget languageSelectionListWidget;

    @Unique
    private TextFieldWidget searchInput;
    private String searchQuery = "";

    @Inject(method = "init", at = @At("RETURN"))
    private void tl$initSearchInput(CallbackInfo ci) {
        searchInput = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 30, 200, 20);
        searchInput.setText(searchQuery);
        searchInput.setResponder((text) -> {
            searchQuery = text;
            ((FilterableEntryList) languageSelectionListWidget).tl$filter(searchQuery);
        });
        this.children.add(searchInput);
        ((FilterableEntryList) languageSelectionListWidget).tl$filter(searchQuery);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void tl$renderSearchInput(int i, int j, float f, CallbackInfo ci) {
        searchInput.render(i, j, f);
    }

    @Override
    public void tick() {
        searchInput.tick();
    }

    @Override
    public boolean charTyped(char chr, int mods) {
        if (searchInput.isFocused()) {
            return searchInput.charTyped(chr, mods);
        }

        return super.charTyped(chr, mods);
    }

    @Override
    public boolean keyPressed(int key, int scancode, int mods) {
        if (searchInput.isFocused()) {
            return searchInput.keyPressed(key, scancode, mods);
        }

        return super.keyPressed(key, scancode, mods);
    }
}
