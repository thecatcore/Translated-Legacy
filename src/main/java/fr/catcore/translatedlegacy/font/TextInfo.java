package fr.catcore.translatedlegacy.font;

public class TextInfo {
    public final String text;
    public final Style style;

    public TextInfo(String text, Style style) {
        this.text = text;
        this.style = style;
    }

    public String getFull() {
        String full = this.text;

        if (style != null) full = "§" + style.character + full;

        return full;
    }
}
