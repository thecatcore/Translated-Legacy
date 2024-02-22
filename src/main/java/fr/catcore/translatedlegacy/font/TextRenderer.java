package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.font.renderable.TextTexture;
import fr.catcore.translatedlegacy.util.NativeImage;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextRenderer {
    private static GameProvider game;
    private static final List<GlyphProvider> providers = new ArrayList<>();
    private static final Map<Text, TextTexture> CACHED = new HashMap<>();

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
        List<Glyph> glyphs = new ArrayList<>();

        Style style = null;

        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);

            if (character == 167 && string.length() > i+1) {
                Character modifier = string.charAt(i+1);

                style = Style.VANILLAS.get(flag).get(modifier);

                if (style != null) {
                    i += 2;
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
            }
        }

        Text text = new Text();
        text.add(glyphs.stream().map(Text.CharInfo::new).collect(Collectors.toList()));
        return text;
    }

    private static TextTexture getTexture(String string, boolean flag) {
        Text text = createTextFromString(string, flag);

        if (!CACHED.containsKey(text)) {
            NativeImage image = text.createImage();
            CACHED.put(text, new TextTexture(image));
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

        TextTexture texture = getTexture(string, flag);

        if (flag) {
            int c = color & -16777216;
            color = (color & 16579836) >> 2;
            color += c;
        }

        texture.bind();

        float var10 = (float)(color >> 16 & 255) / 255.0F;
        float var7 = (float)(color >> 8 & 255) / 255.0F;
        float var8 = (float)(color & 255) / 255.0F;
        float var9 = (float)(color >> 24 & 255) / 255.0F;
        if (var9 == 0.0F) {
            var9 = 1.0F;
        }

        GL11.glColor4f(var10, var7, var8, var9);
        game.draw(x, y, texture.getWidth(), texture.getHeight(), 0.0F);
    }
}
