package fr.catcore.translatedlegacy.ornithe.client.language;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ListWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.render.vertex.Tesselator;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.impl.Localization;

import java.util.Objects;

public class LanguageScreen extends Screen {
    protected Screen parent;
    protected LanguageList list;

    public LanguageScreen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void init() {
        this.buttons.add(new OptionButtonWidget(5, this.width / 2 - 74, this.height - 48, L10n.get("gui.done")));

        this.list = new LanguageList();
        this.list.setScrollButtonIds(this.buttons, 7, 8);
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
        super.mouseClicked(i, j, k);
    }

    protected void mouseReleased(int i, int j, int k) {
        super.mouseReleased(i, j, k);
    }

    @Override
    public void render(int i, int j, float f) {
        this.list.render(i, j, f);

        this.drawCenteredString(this.textRenderer, L10n.get("options.language"), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.textRenderer, L10n.get("options.languageWarning"), this.width / 2, this.height - 26, 8421504);
        super.render(i, j, f);
    }

    class LanguageList extends ListWidget {
        private final Language[] languages = Localization.getLanguageManager().getLanguages().toArray(new Language[0]);

        public LanguageList() {
            super(LanguageScreen.this.minecraft, LanguageScreen.this.width, LanguageScreen.this.height, 32, LanguageScreen.this.height - 55 + 4, 20);
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
    }
}
