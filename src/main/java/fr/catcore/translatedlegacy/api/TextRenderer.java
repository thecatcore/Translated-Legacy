package fr.catcore.translatedlegacy.api;

import fr.catcore.translatedlegacy.util.NativeImage;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextRenderer {
    private static final GameProvider game = GameProvider.getInstance();
    private static final List<GlyphProvider> providers = new ArrayList<>();
    private static final Map<Text, TextTexture> CACHED = new HashMap<>();

    public static void registerProvider(GlyphProvider provider) {
        providers.add(provider);
    }

    private static GlyphProvider getCharRenderer(Character character) {
        for (GlyphProvider provider : providers) {
            if (provider.provides(character)) return provider;
        }

        return null;
    }

    private static Text createTextFromString(String string) {
        List<Glyph> glyphs = new ArrayList<>();

        for (int character : string.toCharArray()) {
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

    private static TextTexture getTexture(String string) {
        Text text = createTextFromString(string);

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

        TextTexture texture = getTexture(string);

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
