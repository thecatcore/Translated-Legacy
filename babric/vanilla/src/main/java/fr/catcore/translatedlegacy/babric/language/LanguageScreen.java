package fr.catcore.translatedlegacy.babric.language;

import fr.catcore.translatedlegacy.babric.TranslationHelper;
import fr.catcore.translatedlegacy.language.FilterableEntryList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;

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
        TranslationStorage var1 = TranslationStorage.getInstance();

        this.buttons.add(new OptionButtonWidget(5, this.width / 2 - 74, this.height - 48, var1.get("gui.done")));

        this.list = new LanguageScreen.LanguageList();
        this.list.registerButtons(this.buttons, 7, 8);

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
                this.minecraft.setScreen(this.parent);
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

        this.drawCenteredTextWithShadow(this.textRenderer, TranslationHelper.get("backport.options.language", "options.language"), this.width / 2, 16, 16777215);
        this.drawCenteredTextWithShadow(this.textRenderer, TranslationHelper.get("backport.options.languageWarning", "options.languageWarning"), this.width / 2, this.height - 26, 8421504);
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

    class LanguageList extends EntryListWidget implements FilterableEntryList {
        private OldTranslationStorage[] languages;

        public LanguageList() {
            super(LanguageScreen.this.minecraft, LanguageScreen.this.width, LanguageScreen.this.height, 52, LanguageScreen.this.height - 55 + 4, 20);

            languages = LanguageManager.CODE_TO_STORAGE.values().toArray(new OldTranslationStorage[0]);
        }

        @Override
        protected int getEntryCount() {
            return languages.length;
        }

        @Override
        protected void entryClicked(int i, boolean flag) {
            LanguageManager.switchLanguage(languages[i].code);
            LanguageScreen.this.minecraft.options.save();
            LanguageScreen.this.minecraft.textureManager.reload();
            for (Object object : LanguageScreen.this.buttons) {
                if (object instanceof ButtonWidget) {
                    ButtonWidget button = (ButtonWidget) object;
                    if (button.id == 5) button.text = TranslationStorage.getInstance().get("gui.done");
                }
            }
        }

        @Override
        protected boolean isSelectedEntry(int i) {
            return Objects.equals(languages[i].code, LanguageManager.CURRENT_LANGUAGE.code);
        }

        protected int getEntriesHeight() {
            return this.getEntryCount() * 20;
        }

        @Override
        protected void renderBackground() {
            LanguageScreen.this.renderBackground();
        }

        @Override
        protected void renderEntry(int i, int j, int k, int i1, Tessellator arg) {
            OldTranslationStorage translationStorage = languages[i];

            LanguageScreen.this.drawCenteredTextWithShadow(LanguageScreen.this.textRenderer, String.format("%s (%s)", translationStorage.name, translationStorage.region),
                    LanguageScreen.this.width / 2, k, 16777215);
        }

        @Override
        public void tl$filter(String text) {
            if (text.isEmpty()) {
                languages = LanguageManager.CODE_TO_STORAGE.values().toArray(new OldTranslationStorage[0]);
            } else {
                languages = LanguageManager.CODE_TO_STORAGE.values()
                        .stream().filter(language -> (language.code + " - " + language).toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))
                        .toArray(OldTranslationStorage[]::new);
            }
        }
    }
}
