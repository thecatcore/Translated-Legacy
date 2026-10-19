package fr.catcore.translatedlegacy.ornithe.common;

import fr.catcore.translatedlegacy.font.Color3f;
import fr.catcore.translatedlegacy.font.Style;
import fr.catcore.translatedlegacy.font.TextInfo;
import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.TextColor;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;
import net.ornithemc.osl.text.api.TextVisitResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses formatting codes with OSL's text components.
 */
public class TextComponentParser {
    public static List<TextInfo> parse(String text) {
        return parse(TextComponents.resolve(replaceUnknownCodes(text)));
    }

    public static List<TextInfo> parse(TextComponent text) {
        List<TextInfo> infos = new ArrayList<>();

        text.visit(net.ornithemc.osl.text.api.Style.EMPTY, (style, part) -> {
            part = removeFormattingCodes(part);

            if (!part.isEmpty()) {
                infos.add(new TextInfo(part, toStyle(style)));
            }

            return TextVisitResults.CONTINUE;
        });

        return infos;
    }

    private static Style toStyle(net.ornithemc.osl.text.api.Style style) {
        TextColor textColor = style.getColor();
        Color3f color = null;

        if (textColor != null) {
            int rgb = textColor.getColor();
            color = new Color3f((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F);
        }

        return new Style(
                color,
                null,
                style.isObfuscated(),
                style.isBold(),
                style.isStrikethrough(),
                style.isUnderlined(),
                style.isItalic(),
                false
        );
    }

    /**
     * OSL throws on codes it doesn't know, treat them as a reset instead.
     */
    private static String replaceUnknownCodes(String text) {
        StringBuilder builder = null;

        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) != Formatting.PREFIX) continue;

            if (!isKnownCode(text.charAt(i + 1))) {
                if (builder == null) builder = new StringBuilder(text);
                builder.setCharAt(i + 1, Formatting.RESET.getCode());
            }

            i++;
        }

        return builder == null ? text : builder.toString();
    }

    private static boolean isKnownCode(char code) {
        for (Formatting formatting : Formatting.values()) {
            if (formatting.getCode() == code) return true;
        }

        return false;
    }

    /**
     * OSL keeps a code that directly follows another one in the text, with the style it already applied.
     */
    private static String removeFormattingCodes(String text) {
        if (text.indexOf(Formatting.PREFIX) < 0) return text;

        StringBuilder builder = new StringBuilder(text.length());

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == Formatting.PREFIX) {
                i++;
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }
}