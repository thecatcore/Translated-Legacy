package fr.catcore.translatedlegacy.ornithe.mixin.client;

import fr.catcore.translatedlegacy.language.FilterableEntryList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
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
    private LanguageOptionsScreen.C_07646499 languageSelectionListWidget;

    protected LanguageOptionsScreenMixin(Text text) {
        super(text);
    }

    @Unique
    private TextFieldWidget searchInput;
    private String searchQuery = "";

    @Inject(method = "init", at = @At("RETURN"))
    private void tl$initSearchInput(CallbackInfo ci) {
        searchInput = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 30, 200, 20, "");
        searchInput.setText(searchQuery);
        searchInput.setResponder((text) -> {
            searchQuery = text;
            ((FilterableEntryList) languageSelectionListWidget).tl$filter(searchQuery);
        });
        this.children.add(searchInput);
        this.m_53778949(searchInput);
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
}
