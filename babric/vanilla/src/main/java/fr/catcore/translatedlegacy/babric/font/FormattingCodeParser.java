package fr.catcore.translatedlegacy.babric.font;

import fr.catcore.translatedlegacy.font.Color3f;
import fr.catcore.translatedlegacy.font.Style;
import fr.catcore.translatedlegacy.font.TextInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses {@code §} formatting codes, for game versions without a text components library.
 */
public class FormattingCodeParser {
    private static final Map<Character, Style> VANILLA = new HashMap<>();

    static {
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

    public static List<TextInfo> parse(String text) {
        List<TextInfo> infos = new ArrayList<>();

        StringBuilder currentText = new StringBuilder();
        Style currentStyle = null;

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            if (character == 167 && text.length() > i+1) {
                Character modifier = text.charAt(i+1);

                Style newStyle = VANILLA.get(modifier);

                if (newStyle == null) {
                    newStyle = VANILLA.get('r');
                }

                i += 1;

                if (currentText.length() > 0) {
                    infos.add(new TextInfo(currentText.toString(), currentStyle));
                    currentText = new StringBuilder();
                }

                if (currentStyle != null) {
                    currentStyle = append(currentStyle, newStyle);
                } else {
                    currentStyle = newStyle;
                }

                continue;
            }

            currentText.append(character);
        }

        if (currentText.length() > 0) {
            infos.add(new TextInfo(currentText.toString(), currentStyle));
        }

        return infos;
    }

    private static Style append(Style current, Style style) {
        Color3f newColor = current.color;

        if (style.reset != null && style.reset) {
            newColor = null;
        }

        if (style.color != null) {
            newColor = style.color;
            style = VANILLA.get('r');
        }

        Boolean newRandom = current.random;

        if (style.random != null) {
            newRandom = style.random;
        }

        Boolean newBold = current.bold;

        if (style.bold != null) {
            newBold = style.bold;
        }

        Boolean newStrikethrough = current.strikethrough;

        if (style.strikethrough != null) {
            newStrikethrough = style.strikethrough;
        }

        Boolean newUnderline = current.underline;

        if (style.underline != null) {
            newUnderline = style.underline;
        }

        Boolean newItalic = current.italic;

        if (style.italic != null) {
            newItalic = style.italic;
        }

        return new Style(newColor, null, newRandom, newBold, newStrikethrough, newUnderline, newItalic, false);
    }
}