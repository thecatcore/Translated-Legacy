package fr.catcore.translatedlegacy.font;

import java.util.HashMap;
import java.util.Map;

public class Style {
    public static final Map<Boolean, Map<Character, Style>> VANILLAS = new HashMap<>();
    public final Color3f color;

    public Style(Color3f color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "{}";
    }

    private static final String styleCodes = "0123456789abcdef";

    static {
        VANILLAS.put(true, new HashMap<>());
        VANILLAS.put(false, new HashMap<>());

        for(int var9 = 0; var9 < 32; ++var9) {
            int var10 = (var9 >> 3 & 1) * 85;
            int var11 = (var9 >> 2 & 1) * 170 + var10;
            int var12 = (var9 >> 1 & 1) * 170 + var10;
            int var22 = (var9 >> 0 & 1) * 170 + var10;
            if (var9 == 6) {
                var11 += 85;
            }

            boolean var24 = var9 >= 16;
            if (TextRenderer.getGameProvider().anaglyph3d()) {
                int var15 = (var11 * 30 + var12 * 59 + var22 * 11) / 100;
                int var16 = (var11 * 30 + var12 * 70) / 100;
                int var17 = (var11 * 30 + var22 * 70) / 100;
                var11 = var15;
                var12 = var16;
                var22 = var17;
            }

            if (var24) {
                var11 /= 4;
                var12 /= 4;
                var22 /= 4;
            }

            Character character = styleCodes.charAt(var24 ? var9 - 16 : var9);

            Color3f color = new Color3f((float)var11 / 255.0F, (float)var12 / 255.0F, (float)var22 / 255.0F);
            VANILLAS.get(var24).put(character, new Style(color));
        }
    }
}
