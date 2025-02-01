package fr.catcore.translatedlegacy.babric;

import fr.catcore.translatedlegacy.font.TextRenderer;
import fr.catcore.translatedlegacy.font.provider.AsciiGlyphProvider;
import fr.catcore.translatedlegacy.font.provider.BitmapGlyphProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TranslatedLegacyBabricClient {
    public static void setGameProvider(Minecraft minecraft) {
        TextRenderer.setGameProvider(new BabricGameProvider(minecraft) {
            @Override
            public InputStream getResource(String path) {
                return FabricLoader.getInstance().getGameInstance().getClass().getResourceAsStream(path);
            }
        });

        registerGlyphProviders();
    }

    public static void registerGlyphProviders() {
        String[] lines = (
                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\n" +
                        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\n" +
                        "\u0000!\"#$%&'()*+,-./\n" +
                        "0123456789:;<=>?\n" +
                        "@ABCDEFGHIJKLMNO\n" +
                        "PQRSTUVWXYZ[\\]^_\n" +
                        "'abcdefghijklmno\n" +
                        "pqrstuvwxyz{|}~⌂\n" +
                        "ÇüéâäàåçêëèïîìÄÅ\n" +
                        "ÉæÆôöòûùÿÖÜø£Ø×ƒ\n" +
                        "áíóúñÑªº¿®¬½¼¡«»" +
                        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\n" +
                        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\n" +
                        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\n" +
                        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\n" +
                        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
        ).split("\n");

        List<int[]> chars = Arrays.stream(lines).map(s -> s.chars().toArray()).collect(Collectors.toList());

        TextRenderer.registerProvider(new BitmapGlyphProvider(
                "/font/default.png",
                7, 8, chars
        ));

//        String[] lines = (
//                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\n" +
//                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\n" +
//                "\u0000\u0021\"\u0023\u0024\u0025\u0026\u0027\u0028\u0029\u002a\u002b\u002c\u002d\u002e\u002f\n" +
//                "\u0030\u0031\u0032\u0033\u0034\u0035\u0036\u0037\u0038\u0039\u003a\u003b\u003c\u003d\u003e\u003f\n" +
//                "\u0040\u0041\u0042\u0043\u0044\u0045\u0046\u0047\u0048\u0049\u004a\u004b\u004c\u004d\u004e\u004f\n" +
//                "PQRSTUVWXYZ[\\]^_\n" +
//                "\u0060\u0061\u0062\u0063\u0064\u0065\u0066\u0067\u0068\u0069\u006a\u006b\u006c\u006d\u006e\u006f\n" +
//                "\u0070\u0071\u0072\u0073\u0074\u0075\u0076\u0077\u0078\u0079\u007a\u007b\u007c\u007d\u007e\u0000\n" +
//                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\n" +
//                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00a3\u0000\u0000\u0192\n" +
//                "\u0000\u0000\u0000\u0000\u0000\u0000\u00aa\u00ba\u0000\u0000\u00ac\u0000\u0000\u0000\u00ab\u00bb\n" +
//                "\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\n" +
//                "\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\n" +
//                "\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\n" +
//                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u2205\u2208\u0000\n" +
//                "\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u0000\u221a\u207f\u00b2\u25a0\u0000"
//        ).split("\n");
//
//        List<int[]> chars = Arrays.stream(lines).map(s -> s.chars().toArray()).collect(Collectors.toList());
//
//        TextRenderer.registerProvider(new BitmapGlyphProvider(
//                "/assets/minecraft/textures/font/ascii.png",
//                7, 8, chars
//        ));

//        TextRenderer.registerProvider(new AsciiGlyphProvider("/font/default.png"));
    }
}
