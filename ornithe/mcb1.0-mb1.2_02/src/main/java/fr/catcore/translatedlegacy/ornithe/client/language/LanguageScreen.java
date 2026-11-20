package fr.catcore.translatedlegacy.ornithe.client.language;

import fr.catcore.translatedlegacy.language.FilterableEntryList;
import fr.catcore.translatedlegacy.ornithe.common.TranslationHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.render.vertex.Tesselator;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.impl.Localization;

import java.util.Locale;
import java.util.Objects;

public class LanguageScreen extends Screen {
    protected Screen parent;
    protected LanguageList list;

    private TextFieldWidget searchInput;
    private String searchQuery = "";

    public LanguageScreen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void init() {
        this.buttons.add(new OptionButtonWidget(5, this.width / 2 - 74, this.height - 48, L10n.get("gui.done")));

        this.list = new LanguageList();
        this.list.setScrollButtonIds(this.buttons, 7, 8);

        searchInput = new TextFieldWidget(this, this.textRenderer, this.width / 2 - 100, 30, 200, 20, searchQuery);
        searchInput.setMaxLength(30);
        list.tl$filter(searchQuery);
    }

    @Override
    public void tick() {
        searchInput.tick();
    }

    protected void buttonClicked(ButtonWidget arg) {
        if (arg.active) {
            if (arg.id == 5) {
                Localization.reloadLocale();
                this.minecraft.options.save();
                this.minecraft.openScreen(this.parent);
            } else {
                this.list.buttonClicked(arg);
            }

        }
    }

    protected void mouseClicked(int i, int j, int k) {
        searchInput.mouseClicked(i, j, k);

        super.mouseClicked(i, j, k);
    }

    protected void mouseReleased(int i, int j, int k) {
        super.mouseReleased(i, j, k);
    }

    @Override
    public void render(int i, int j, float f) {
        this.list.render(i, j, f);

        this.drawCenteredString(this.textRenderer, TranslationHelper.get("backport.options.language", "options.language"), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.textRenderer, TranslationHelper.get("backport.options.languageWarning", "options.languageWarning"), this.width / 2, this.height - 26, 8421504);
        searchInput.render();
        super.render(i, j, f);
    }

    @Override
    protected void keyPressed(char chr, int key) {
        if (searchInput.focused) {
            searchInput.keyPressed(chr, key);
            searchQuery = searchInput.getText();
            list.tl$filter(searchQuery);
            return;
        }

        super.keyPressed(chr, key);
    }

    class LanguageList extends ListWidget implements FilterableEntryList {
        private Language[] languages;

        public LanguageList() {
            super(LanguageScreen.this.minecraft, LanguageScreen.this.width, LanguageScreen.this.height, 52, LanguageScreen.this.height - 55 + 4, 20);

            languages = Localization.getLanguageManager().getLanguages().toArray(new Language[0]);
        }

        @Override
        protected int size() {
            return languages.length;
        }

        @Override
        protected void entryClicked(int i, boolean flag) {
            Localization.setLanguage(languages[i]);
            LanguageScreen.this.minecraft.textureManager.reload();
            for (Object object : LanguageScreen.this.buttons) {
                if (object instanceof ButtonWidget) {
                    ButtonWidget button = (ButtonWidget) object;
                    if (button.id == 5) button.message = L10n.get("gui.done");
                }
            }
        }

        @Override
        protected boolean isEntrySelected(int index) {
            return Objects.equals(languages[index].code(), Localization.getLanguage().code());
        }

        protected int getHeight() {
            return this.size() * 20;
        }

        @Override
        protected void renderBackground() {
            LanguageScreen.this.renderBackground();
        }

        @Override
        protected void renderEntry(int index, int x, int y, int height, Tesselator tesselator) {
            Language language = languages[index];
            LanguageScreen.this.drawCenteredString(LanguageScreen.this.textRenderer, String.format("%s (%s)", language.name(), language.region()), LanguageScreen.this.width / 2, y, 16777215);
        }

        @Override
        public void tl$filter(String text) {
            if (text.isEmpty()) {
                languages = Localization.getLanguageManager().getLanguages().toArray(new Language[0]);
            } else {
                languages = Localization.getLanguageManager().getLanguages()
                        .stream().filter(language -> (language.code() + " - " + language).toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))
                        .toArray(Language[]::new);
            }
        }
    }
}
