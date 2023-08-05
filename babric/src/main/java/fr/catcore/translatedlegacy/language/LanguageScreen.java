package fr.catcore.translatedlegacy.language;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.Objects;

public class LanguageScreen extends Screen {
    protected Screen parent;
    protected LanguageList list;

    public LanguageScreen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void init() {
        TranslationStorage var1 = TranslationStorage.getInstance();

        this.buttons.add(new OptionButtonWidget(5, this.width / 2 - 74, this.height - 48, var1.get("gui.done")));

        this.list = new LanguageScreen.LanguageList();
        this.list.registerButtons(this.buttons, 7, 8);
    }

    protected void buttonClicked(ButtonWidget arg) {
        if (arg.active) {
            if (arg.id == 5) {
                this.minecraft.textureManager.method_1096();
                this.minecraft.setScreen(this.parent);
            } else {
                this.list.buttonClicked(arg);
            }

        }
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
    }

    protected void mouseReleased(int i, int j, int k) {
        super.mouseReleased(i, j, k);
    }

    @Override
    public void render(int i, int j, float f) {
        this.list.render(i, j, f);

        TranslationStorage var4 = TranslationStorage.getInstance();
        this.drawCenteredTextWithShadow(this.textRenderer, var4.get("options.language"), this.width / 2, 16, 16777215);
        this.drawCenteredTextWithShadow(this.textRenderer, var4.get("options.languageWarning"), this.width / 2, this.height - 26, 8421504);
        super.render(i, j, f);
    }

    class LanguageList extends EntryListWidget {

        public LanguageList() {
            super(LanguageScreen.this.minecraft, LanguageScreen.this.width, LanguageScreen.this.height, 32, LanguageScreen.this.height - 55 + 4, 20);
        }

        @Override
        protected int getEntryCount() {
            return LanguageManager.CODE_TO_STORAGE.size();
        }

        @Override
        protected void entryClicked(int i, boolean flag) {
            LanguageManager.switchLanguage(((OldTranslationStorage) LanguageManager.CODE_TO_STORAGE.values().toArray()[i]).code);
            LanguageScreen.this.minecraft.textureManager.method_1096();
            for (Object object : LanguageScreen.this.buttons) {
                if (object instanceof ButtonWidget) {
                    ButtonWidget button = (ButtonWidget) object;
                    if (button.id == 5) button.text = TranslationStorage.getInstance().get("gui.done");
                }
            }
        }

        @Override
        protected boolean isSelectedEntry(int i) {
            return Objects.equals(((OldTranslationStorage) LanguageManager.CODE_TO_STORAGE.values().toArray()[i]).code, LanguageManager.CURRENT_LANGUAGE.code);
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
            OldTranslationStorage translationStorage = (OldTranslationStorage) LanguageManager.CODE_TO_STORAGE.values().toArray()[i];

            LanguageScreen.this.drawCenteredTextWithShadow(LanguageScreen.this.textRenderer, String.format("%s (%s)", translationStorage.name, translationStorage.region),
                    LanguageScreen.this.width / 2, k, 16777215);
        }
    }
}
