package fr.catcore.translatedlegacy.font;

import java.util.HashMap;
import java.util.Map;

public class Style {
    public static final Map<Character, Style> VANILLA = new HashMap<>();
    public final Color3f color;

    public Style(Color3f color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "{}";
    }

    private static final String styleCodes = "0123456789abcdef";

    private static boolean init = false;

    private static void color(Character character, int color) {
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        VANILLA.put(character, new Style(new Color3f(r, g, b)));
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
    }
}
