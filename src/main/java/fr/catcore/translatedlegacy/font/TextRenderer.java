package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.font.renderable.Renderable;
import fr.catcore.translatedlegacy.font.renderable.RenderableText;
import fr.catcore.translatedlegacy.util.AccessWeightedMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextRenderer {
    private static GameProvider game;
    private static final List<GlyphProvider> providers = new ArrayList<>();
    private static final Map<String, TextImage> CACHED_TEXT = new AccessWeightedMap<>(20000, text -> {
        try {
            text.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });

    private static TextImage SPACE;

    public static TextImage getSpaceImage() {
        if (SPACE == null) {
            List<Glyph> glyphs = new ArrayList<>();
            glyphs.add(getCharRenderer(' ').getGlyph(' '));
            SPACE = new TextImage(glyphs);
        }

        return SPACE;
    }

    public static int getSpaceWidth() {
        TextImage spaceImage = getSpaceImage();
        return spaceImage != null ? spaceImage.getWidth() : 4;
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

                    currentStyle = newStyle;

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

    private static TextImage getTextImage(String text) {
        TextImage image;

        if (CACHED_TEXT.containsKey(text)) {
            image = CACHED_TEXT.get(text);
        } else {
            List<Glyph> glyphs = parseGlyphs(text);
            image = new TextImage(glyphs);
            CACHED_TEXT.put(text, image);
        }

        return image;
    }

    private static Renderable getRenderable(TextInfo info) {
        List<TextImage> textImages = new ArrayList<>();

        if (!info.text.contains(" ")) {
            textImages.add(getTextImage(info.text));
        } else {
            for (String text : info.text.split(" ", -1)) {
                textImages.add(getTextImage(text));
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

        SPACE = null;
    }

    public static void draw(String string, int x, int y, int color, boolean flag) {
        if (string == null || string.isEmpty()) return;

        Style.init();

        RenderableText renderableText = getRenderableText(string);

        if (flag) {
            int c = color & -16777216;
            color = (color & 16579836) >> 2;
            color += c;
        }

        renderableText.render(x, y, color, 0, game, flag);
    }

    public static int getTextWidth(String string) {
        RenderableText renderableText = getRenderableText(string);
        return renderableText.getWidth();
    }
}
