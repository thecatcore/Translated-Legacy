package fr.catcore.translatedlegacy.ornithe.client.language;

import fr.catcore.translatedlegacy.util.TextUtils;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.TextRenderer;

public class TextFieldWidget extends GuiElement {
    private final TextRenderer textRenderer;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private String text;
    private int maxLength;
    private int focusedTicks;
    public boolean focused = false;
    public boolean editable = true;
    private Screen screen;

    public TextFieldWidget(Screen screen, TextRenderer textRenderer, int x, int y, int width, int height, String message) {
        this.screen = screen;
        this.textRenderer = textRenderer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.setText(message);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void tick() {
        ++this.focusedTicks;
    }

    public void keyPressed(char chr, int key) {
        if (this.editable && this.focused) {
            if (chr == 9) {
//                this.screen.cycleFocus();
            }

            if (chr == 22) {
                String string = Screen.getClipboard();
                if (string == null) {
                    string = "";
                }

                int i = 32 - this.text.length();
                if (i > string.length()) {
                    i = string.length();
                }

                if (i > 0) {
                    this.text = this.text + string.substring(0, i);
                }
            }

            if (key == 14 && this.text.length() > 0) {
                this.text = this.text.substring(0, this.text.length() - 1);
            }

            if (TextUtils.isCharacterAllowed(chr) >= 0 && (this.text.length() < this.maxLength || this.maxLength == 0)) {
                this.text = this.text + chr;
            }

        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean i = this.editable && mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
        this.setFocused(i);
    }

    public void setFocused(boolean focused) {
        if (focused && !this.focused) {
            this.focusedTicks = 0;
        }

        this.focused = (boolean)focused;
    }

    public void render() {
        this.fill(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
        this.fill(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
        if (this.editable) {
            int i = this.focused && this.focusedTicks / 6 % 2 == 0 ? 1 : 0;
            this.drawString(this.textRenderer, this.text + (i != 0 ? "_" : ""), this.x + 4, this.y + (this.height - 8) / 2, 14737632);
        } else {
            this.drawString(this.textRenderer, this.text, this.x + 4, this.y + (this.height - 8) / 2, 7368816);
        }

    }

    public void setMaxLength(int maximumLength) {
        this.maxLength = maximumLength;
    }
}
