package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.font.renderable.Renderable;
import fr.catcore.translatedlegacy.font.renderable.RenderableText;
import fr.catcore.translatedlegacy.util.AccessWeightedMap;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TextRenderer {
    private static GameProvider game;
    private static final List<GlyphProvider> providers = new ArrayList<>();
    private static final Map<String, List<TextImage>> CACHED_TEXT = new AccessWeightedMap<>(20000, text -> {
        text.forEach(t -> {
            try {
                t.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    });


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

                if (newStyle != null) {
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

    private static List<TextImage> getTextImage(String text) {
        List<TextImage> images = new ArrayList<>();

        if (CACHED_TEXT.containsKey(text)) {
            return CACHED_TEXT.get(text);
        } else {
            List<Glyph> glyphs = parseGlyphs(text);

            List<Glyph> temp = new ArrayList<>();
            float currentFactor = 1.0F;

            for (Glyph glyph : glyphs) {
                if (temp.isEmpty()) {
                    temp.add(glyph);
                    currentFactor = glyph.getProvider().scalingFactor();
                } else {
                    if (currentFactor != glyph.getProvider().scalingFactor()) {
                        images.add(new TextImage(new ArrayList<>(temp)));
                        temp.clear();

                        temp.add(glyph);
                        currentFactor = glyph.getProvider().scalingFactor();
                    } else {
                        temp.add(glyph);
                    }
                }
            }

            if (!temp.isEmpty()) {
                images.add(new TextImage(new ArrayList<>(temp)));
                temp.clear();
            }

            CACHED_TEXT.put(text, images);
        }

        return images;
    }

    private static Renderable getRenderable(TextInfo info) {
        List<TextImage> textImages = new ArrayList<>();

        if (!info.text.contains(" ")) {
            textImages.addAll(getTextImage(info.text));
        } else {
            for (String text : info.text.split(" ", -1)) {
                textImages.addAll(getTextImage(text));
            }
        }

        return new Renderable(textImages, info.style);
    }

    private static RenderableText getRenderableText(String string) {
        return new RenderableText(
            parseTextInfos(string)
                    .stream()
                    .map(TextRenderer::getRenderable)
                    .collect(Collectors.toList())
        );
    }

    public static void reload() {
        CACHED_TEXT.clear();

        providers.forEach(GlyphProvider::unload);
    }

    public static int draw(String string, int x, int y, int color, boolean flag) {
        if (string == null || string.isEmpty()) return 8;

        Style.init();

        RenderableText renderableText = getRenderableText(string);

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
        RenderableText renderableText = getRenderableText(string);
        return renderableText.getWidth();
    }

    public static int getTextHeight(String string) {
        if (string == null || string.isEmpty()) return 8;

        RenderableText renderableText = getRenderableText(string);
        int height = renderableText.getHeight();
        return height > 8 ? height - 2 : height;
    }
}
