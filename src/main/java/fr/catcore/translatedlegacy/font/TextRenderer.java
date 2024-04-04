package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.font.renderable.RenderableText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextRenderer {
    private static GameProvider game;
    private static final List<GlyphProvider> providers = new ArrayList<>();
    private static final Map<Text, RenderableText> CACHED = new HashMap<>();

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

    private static Text createTextFromString(String string, boolean flag) {
        List<Text> texts = new ArrayList<>();
        List<Glyph> glyphs = new ArrayList<>();

        Style style = null;

        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);

            if (character == 167 && string.length() > i+1) {
                Character modifier = string.charAt(i+1);

                Style newStyle = Style.VANILLA.get(modifier);

                if (newStyle != null) {
                    i += 1;

                    if (!glyphs.isEmpty()) {
                        Text tempText = new Text(style);

                        tempText.add(glyphs.stream().map(Text.CharInfo::new).collect(Collectors.toList()));

                        glyphs.clear();

                        texts.add(tempText);
                    }

                    style = newStyle;

                    continue;
                }
            }

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

        Text text = new Text(style);
        text.add(glyphs.stream().map(Text.CharInfo::new).collect(Collectors.toList()));

        if (texts.isEmpty()) return text;

        texts.add(text);
        MultiText multiText = new MultiText(null);
        multiText.addText(texts);
        return multiText;
    }

    private static RenderableText getRenderableText(String string, boolean flag) {
        Text text = createTextFromString(string, flag);

        if (!CACHED.containsKey(text)) {
            System.out.println("'" + string + "' -> " + text);
            RenderableText renderableText = new RenderableText(text.createRenderable());
            CACHED.put(text, renderableText);
        }

        return CACHED.get(text);
    }

    public static void reload() {
        CACHED.forEach((t, r) -> {
            try {
                r.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        CACHED.clear();

        providers.forEach(GlyphProvider::unload);
    }

    public static void draw(String string, int x, int y, int color, boolean flag) {
        if (string == null || string.isEmpty()) return;

        Style.init();

        RenderableText renderableText = getRenderableText(string, flag);

        if (flag) {
            int c = color & -16777216;
            color = (color & 16579836) >> 2;
            color += c;
        }

        renderableText.render(x, y, color, 0, game, flag);
    }
}
