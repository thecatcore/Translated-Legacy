package fr.catcore.translatedlegacy.font;

import java.util.HashMap;
import java.util.Map;

public class Style {
    public static final Map<Character, Style> VANILLA = new HashMap<>();
    public final Color3f color;
    public final Character character;
    public final Boolean random, bold, strikethrough, underline, italic, reset;

    public Style(Color3f color, Character character, Boolean random, Boolean bold, Boolean strikethrough, Boolean underline, Boolean italic, Boolean reset) {
        this.color = color;
        this.character = character;
        this.random = random;
        this.bold = bold;
        this.strikethrough = strikethrough;
        this.underline = underline;
        this.italic = italic;
        this.reset = reset;
    }

    public Style append(Style style) {
        Color3f newColor = this.color;

        if (style.reset != null && style.reset) {
            newColor = null;
        }

        if (style.color != null) {
            newColor = style.color;
            style = VANILLA.get('r');
        }

        Boolean newRandom = this.random;

        if (style.random != null) {
            newRandom = style.random;
        }

        Boolean newBold = this.bold;

        if (style.bold != null) {
            newBold = style.bold;
        }

        Boolean newStrikethrough = this.strikethrough;

        if (style.strikethrough != null) {
            newStrikethrough = style.strikethrough;
        }

        Boolean newUnderline = this.underline;

        if (style.underline != null) {
            newUnderline = style.underline;
        }

        Boolean newItalic = this.italic;

        if (style.italic != null) {
            newItalic = style.italic;
        }

        return new Style(newColor, null, newRandom, newBold, newStrikethrough, newUnderline, newItalic, false);
    }

    private static boolean init = false;

    private static void color(Character character, int color) {
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        VANILLA.put(character, new Style(
                new Color3f(r, g, b),
                character,
                false,
                false,
                false,
                false,
                false,
                false
        ));
    }

    protected static void init() {
        if (init) return;
        init = true;

        color('0', 0);
        color('1', 170);
        color('2', 43520);
        color('3', 43690);
        color('4', 11141120);
        color('5', 11141290);
        color('6', 16755200);
        color('7', 11184810);
        color('8', 5592405);
        color('9', 5592575);
        color('a', 5635925);
        color('b', 5636095);
        color('c', 16733525);
        color('d', 16733695);
        color('e', 16777045);
        color('f', 16777215);

        VANILLA.put('k', new Style(null, 'k', true, null, null, null, null, null));
        VANILLA.put('l', new Style(null, 'l', null, true, null, null, null, null));
        VANILLA.put('m', new Style(null, 'm', null, null, true, null, null, null));
        VANILLA.put('n', new Style(null, 'n', null, null, null, true, null, null));
        VANILLA.put('o', new Style(null, 'o', null, null, null, null, true, null));
        VANILLA.put('r', new Style(null, 'r', false, false, false, false, false, true));
    }
}
