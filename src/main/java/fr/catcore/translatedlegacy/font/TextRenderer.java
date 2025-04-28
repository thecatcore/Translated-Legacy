package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.font.renderable.*;
import fr.catcore.translatedlegacy.util.AccessWeightedMap;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TextRenderer {
    private static GameProvider game;
    private static final List<GlyphProvider> providers = new ArrayList<>();

    private static final Map<String, List<RenderableItem>> CACHED_TEXT2 = new AccessWeightedMap<>(20000, text -> {});


    public static int getSpaceWidth() {
        return 4;
    }

    public static void setGameProvider(GameProvider provider) {
        game = provider;
    }

    public static GameProvider getGameProvider() {
        return game;
    }

    public static void registerProvider(GlyphProvider provider) {
        providers.add(provider);
    }

    private static GlyphProvider getCharRenderer(Character character) {
        for (GlyphProvider provider : providers) {
            if (provider.provides(character)) return provider;
        }

        return null;
    }

    private static List<TextInfo> parseTextInfos(String text) {
        List<TextInfo> infos = new ArrayList<>();

        StringBuilder currentText = new StringBuilder();
        Style currentStyle = null;

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            if (character == 167 && text.length() > i+1) {
                Character modifier = text.charAt(i+1);

                Style newStyle = Style.VANILLA.get(modifier);

                if (newStyle == null) {
                    newStyle = Style.VANILLA.get('r');
                }

                i += 1;

                if (currentText.length() > 0) {
                    infos.add(new TextInfo(currentText.toString(), currentStyle));
                    currentText = new StringBuilder();
                }

                if (currentStyle != null) {
                    currentStyle = currentStyle.append(newStyle);
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

    private static List<Glyph> parseGlyphs(String text) {
        List<Glyph> glyphs = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            GlyphProvider provider = getCharRenderer((char) character);

            if (provider != null) {
                if (!provider.isLoaded()) {
                    try {
                        provider.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (provider.isLoaded()) {
                    Glyph glyph = provider.getGlyph((char) character);
                    if (glyph != null) glyphs.add(glyph);
                }
            } else {
            }
        }

        return glyphs;
    }

    private static List<RenderableItem> getTextImage2(String text, TextInfo info) {
        List<RenderableItem> images = new ArrayList<>();

        if (CACHED_TEXT2.containsKey(info.getStylePrefix() + text)) {
            return CACHED_TEXT2.get(info.getStylePrefix() + text);
        } else {
            List<Glyph> glyphs = parseGlyphs(text);

            List<RenderableItem> items = glyphs.stream().map(glyph -> {
                if (info.style != null && info.style.random != null && info.style.random) {
                    return new ObfuscatedGlyphRenderer(glyph);
                } else {
                    return new GlyphRenderer(glyph);
                }
            }).collect(Collectors.toList());

            images.addAll(items);

            CACHED_TEXT2.put(info.getStylePrefix() + text, items);
        }

        return images;
    }

    private static GlyphContainer getContainer(TextInfo info) {
        List<RenderableItem> items = new ArrayList<>();

        if (!info.text.contains(" ")) {
            items.addAll(getTextImage2(info.text, info));
        } else {
            String[] texts = info.text.split(" ", -1);

            for (int i = 0; i < texts.length; i++) {
                String text = texts[i];

                items.addAll(getTextImage2(text, info));

                if (i != texts.length - 1) {
                    items.add(new SpaceRenderer());
                }
            }
        }

        return new GlyphContainer(items, info.style);
    }

    private static RenderableText getRenderableText2(String string) {
        return new RenderableText(
                parseTextInfos(string)
                        .stream()
                        .map(TextRenderer::getContainer)
                        .collect(Collectors.toList())
        );
    }

    public static void reload() {
        CACHED_TEXT2.clear();

        providers.forEach(GlyphProvider::unload);
    }

    public static int draw(String string, int x, int y, int color, boolean flag) {
        if (string == null || string.isEmpty()) return 8;

        Style.init();

        RenderableText renderableText = getRenderableText2(string);

        if (flag) {
            int c = color & -16777216;
            color = (color & 16579836) >> 2;
            color += c;
        }

        renderableText.render(x, y, color, 0, game, flag);

        if (renderableText.getHeight() > 8) {
            return renderableText.getHeight() - 2;
        }

        return renderableText.getHeight();
    }

    public static int getTextWidth(String string) {
        RenderableText renderableText = getRenderableText2(string);
        return renderableText.getWidth();
    }

    public static int getTextHeight(String string) {
        if (string == null || string.isEmpty()) return 8;

        RenderableText renderableText = getRenderableText2(string);
        int height = renderableText.getHeight();
        return height > 8 ? height - 2 : height;
    }

    public static final String RANDOM_CHARS_PALLETTE = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000";
    public static final Random fontRandom = new Random();

    public static Glyph getGlyph(char character) {
        GlyphProvider provider = getCharRenderer(character);
        if (provider != null) {
            if (!provider.isLoaded()) {
                try {
                    provider.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (provider.isLoaded()) {
                return provider.getGlyph(character);
            } else {
                return null;
            }
        }

        return null;
    }

    public static Glyph getObfuscated(int width, int height) {
        Glyph glyph = getGlyph(
                (char) RANDOM_CHARS_PALLETTE.indexOf(fontRandom.nextInt(RANDOM_CHARS_PALLETTE.length()))
        );

        while (glyph == null || glyph.getHeight() != height || glyph.getWidth() != width) {
            glyph = getGlyph(
                    (char) RANDOM_CHARS_PALLETTE.indexOf(fontRandom.nextInt(RANDOM_CHARS_PALLETTE.length()))
            );
        }

        return glyph;
    }
}
