package fr.catcore.translatedlegacy.font;

public class TextInfo {
    public final String text;
    public final Style style;

    public TextInfo(String text, Style style) {
        this.text = text;
        this.style = style;
    }

    public String getStylePrefix() {
        if (style != null && style.random != null && style.random) {
            return "§k";
        }

        return "";
    }
}
