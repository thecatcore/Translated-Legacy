package fr.catcore.translatedlegacy.language;

import net.minecraft.class_97;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.gui.widgets.OptionButton;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;

public class LanguageScreen extends Screen {
    protected Screen parent;
    protected LanguageList list;

    public LanguageScreen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void init() {
        TranslationStorage var1 = TranslationStorage.getInstance();

        this.buttons.add(new OptionButton(5, this.width / 2 - 74, this.height - 48, var1.translate("gui.done")));

        this.list = new LanguageScreen.LanguageList();
        this.list.method_1258(this.buttons, 7, 8);
    }

    protected void buttonClicked(Button arg) {
        if (arg.active) {
            if (arg.id == 5) {
                this.minecraft.textureManager.method_1096();
                this.minecraft.openScreen(this.parent);
            } else {
                this.list.method_1259(arg);
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
        this.list.method_1256(i, j, f);

        TranslationStorage var4 = TranslationStorage.getInstance();
        this.drawTextWithShadowCentred(this.textManager, var4.translate("options.language"), this.width / 2, 16, 16777215);
        this.drawTextWithShadowCentred(this.textManager, var4.translate("options.languageWarning"), this.width / 2, this.height - 26, 8421504);
        super.render(i, j, f);
    }

    class LanguageList extends class_97 {

        public LanguageList() {
            super(LanguageScreen.this.minecraft, LanguageScreen.this.width, LanguageScreen.this.height, 32, LanguageScreen.this.height - 55 + 4, 20);
        }

        @Override
        protected int method_1266() {
            return LanguageManager.CODE_TO_STORAGE.size();
        }

        @Override
        protected void method_1267(int i, boolean flag) {
            LanguageManager.CURRENT_LANGUAGE = (OldTranslationStorage) LanguageManager.CODE_TO_STORAGE.values().toArray()[i];
            LanguageScreen.this.minecraft.textureManager.method_1096();
            for (Object object : LanguageScreen.this.buttons) {
                if (object instanceof Button) {
                    Button button = (Button) object;
                    if (button.id == 5) button.text = TranslationStorage.getInstance().translate("gui.done");
                }
            }
        }

        @Override
        protected boolean method_1270(int i) {
            return (OldTranslationStorage) LanguageManager.CODE_TO_STORAGE.values().toArray()[i] == LanguageManager.CURRENT_LANGUAGE;
        }

        protected int method_1268() {
            return this.method_1266() * 20;
        }

        @Override
        protected void method_1269() {
            LanguageScreen.this.renderBackground();
        }

        @Override
        protected void method_1264(int i, int j, int k, int i1, Tessellator arg) {
            OldTranslationStorage translationStorage = (OldTranslationStorage) LanguageManager.CODE_TO_STORAGE.values().toArray()[i];

            LanguageScreen.this.drawTextWithShadowCentred(LanguageScreen.this.textManager, String.format("%s (%s)", translationStorage.name, translationStorage.region),
                    LanguageScreen.this.width / 2, k, 16777215);
        }
    }
}
