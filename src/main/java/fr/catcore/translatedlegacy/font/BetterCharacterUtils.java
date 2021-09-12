package fr.catcore.translatedlegacy.font;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BetterCharacterUtils {
    public static final String field_298 = method_350();
    public static final char[] field_299 = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':'};

    private static String method_350() {
        String var0 = "";

        try {
            BufferedReader var1 = new BufferedReader(new InputStreamReader(BetterCharacterUtils.class.getResourceAsStream("/assets/modid/font/font.txt"), "UTF-8"));
            String var2 = "";

            while((var2 = var1.readLine()) != null) {
                if (!var2.startsWith("#")) {
                    var0 = var0 + var2;
                }
            }

            var1.close();
        } catch (Exception var3) {
        }

        return var0;
    }
}
