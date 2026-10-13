package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.font.api.GameProvider;
import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.font.renderable.*;
import fr.catcore.translatedlegacy.util.AccessWeightedMap;
import fr.catcore.translatedlegacy.util.TextUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TextRenderer {
    private static GameProvider game;
    private static final List<GlyphProvider> providers = new ArrayList<>();

    private static final Map<String, List<RenderableItem>> CACHED_TEXT = new AccessWeightedMap<>(20000, text -> {});
    private static final Map<String, RenderableText> RENDERABLE_TEXT_CACHE = new AccessWeightedMap<>(8192, text -> {});


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

    private static List<RenderableItem> getTextImage(String text, TextInfo info) {
        String key = info.getStylePrefix() + text;

        List<RenderableItem> cached = CACHED_TEXT.get(key);
        if (cached != null) {
            return cached;
        }

        boolean obfuscated = info.style != null && info.style.random != null && info.style.random;
        List<RenderableItem> items = new ArrayList<>();

        int clusterStart = 0;

        while (clusterStart < text.length()) {
            int clusterEnd = clusterStart + 1;

            while (clusterEnd < text.length() && TextUtils.continuesCluster(text.charAt(clusterEnd))) {
                clusterEnd++;
            }

            if (obfuscated) {
                // Only the base letter: getObfuscated() may never find a random glyph as narrow as a mark
                Glyph glyph = getVisibleGlyph(text.charAt(clusterStart));
                if (glyph != null) items.add(new ObfuscatedGlyphRenderer(glyph));
            } else {
                addCluster(text, clusterStart, clusterEnd, items);
            }

            clusterStart = clusterEnd;
        }

        CACHED_TEXT.put(key, items);

        return items;
    }

    private static Glyph getVisibleGlyph(char character) {
        if (TextUtils.isBidiControl(character) || character == '\u200C' || character == '\u200D') return null;

        return getGlyph(character);
    }

    /**
     * Lays out one grapheme cluster: a base letter followed by the combining marks drawn on it.
     * <p>
     * Marks are positioned using the dotted circle the font drew them around (see
     * {@link Glyph#getMarkCircleX()}): marks that take no space are centered on the base letter,
     * spacing marks (most Indic vowel signs) are split at the circle's center, the half before it
     * is put before the base letter and the half after it after the base letter.
     */
    private static void addCluster(String text, int start, int end, List<RenderableItem> items) {
        char baseChar = text.charAt(start);
        Glyph base = getVisibleGlyph(baseChar);

        List<Glyph> marks = new ArrayList<>();
        List<Character> markChars = new ArrayList<>();

        for (int i = start + 1; i < end; i++) {
            char c = text.charAt(i);
            Glyph glyph = TextUtils.isCombiningMark(c) ? getVisibleGlyph(c) : null;

            if (glyph != null) {
                marks.add(glyph);
                markChars.add(c);
            }
        }

        if (base == null || TextUtils.isCombiningMark(baseChar) || marks.isEmpty()) {
            // Nothing to combine: a lone letter, or marks with no letter to attach to
            if (base != null) items.add(new GlyphRenderer(base));
            for (Glyph mark : marks) items.add(new GlyphRenderer(mark));
            return;
        }

        // A font's marks are drawn to fit its own letters, so take the letter from the marks' font if it has one
        GlyphProvider markProvider = marks.get(0).getProvider();

        if (base.getProvider() != markProvider && markProvider.provides(baseChar)) {
            Glyph sameFontBase = markProvider.getGlyph(baseChar);
            if (sameFontBase != null && sameFontBase.getWidth() > 0) base = sameFontBase;
        }

        float baseScale = base.getProvider().scalingFactor();
        float baseLeft = base.getInkLeft() * baseScale;
        float baseRight = base.getInkRight() * baseScale;
        float baseAnchor = base.getAnchorX() * baseScale;

        GlyphRenderer baseRenderer = new GlyphRenderer(base);
        List<GlyphRenderer> parts = new ArrayList<>();
        parts.add(baseRenderer);

        float left = 0;
        float right = baseRenderer.getWidth();

        for (int i = 0; i < marks.size(); i++) {
            Glyph mark = marks.get(i);
            float scale = mark.getProvider().scalingFactor();
            int circle = mark.getMarkCircleX();
            boolean spacing = Character.getType(markChars.get(i)) == Character.COMBINING_SPACING_MARK;

            if (circle < 0) {
                if (spacing) {
                    GlyphRenderer renderer = new GlyphRenderer(mark, right);
                    parts.add(renderer);
                    right += renderer.getWidth();
                } else {
                    parts.add(new GlyphRenderer(mark, baseAnchor - mark.getAnchorX() * scale));
                }
            } else if (!spacing) {
                parts.add(new GlyphRenderer(mark, baseAnchor - (circle + 3) * scale));
            } else {
                int middle = circle + 3;
                int width = mark.getWidth();

                // The circle's left edge goes on the base letter's left edge...
                float beforeX = baseLeft - circle * scale;
                parts.add(new GlyphRenderer(mark, beforeX, 0, Math.min(middle, width)));

                if (mark.getInkLeft() < circle) {
                    left = Math.min(left, beforeX + mark.getInkLeft() * scale);
                }

                // ...and its right edge on the base letter's right edge
                float afterX = baseRight - (circle + 6) * scale;

                if (width > middle) {
                    parts.add(new GlyphRenderer(mark, afterX, middle, width));
                }

                if (width > circle + 6) {
                    right = Math.max(right, afterX + width * scale);
                }
            }
        }

        float shift = -(float) Math.floor(left);
        int advance = (int) Math.ceil(right + shift);

        for (GlyphRenderer part : parts) {
            part.shift(shift);
            part.setAdvance(0);
        }

        // Every part is drawn from the cluster's start, then the last one moves the pen past the whole cluster
        parts.get(parts.size() - 1).setAdvance(advance);

        items.addAll(parts);
    }

    private static GlyphContainer getContainer(TextInfo info) {
        List<RenderableItem> items = new ArrayList<>();

        if (!info.text.contains(" ")) {
            items.addAll(getTextImage(info.text, info));
        } else {
            String[] texts = info.text.split(" ", -1);

            for (int i = 0; i < texts.length; i++) {
                String text = texts[i];

                items.addAll(getTextImage(text, info));

                if (i != texts.length - 1) {
                    items.add(new SpaceRenderer());
                }
            }
        }

        return new GlyphContainer(items, info.style);
    }

    private static RenderableText getRenderableText(String string) {
        RenderableText cached = RENDERABLE_TEXT_CACHE.get(string);
        if (cached != null) {
            return cached;
        }

        RenderableText renderableText = new RenderableText(
                BidiReorderer.toVisualOrder(parseTextInfos(string))
                        .stream()
                        .map(TextRenderer::getContainer)
                        .collect(Collectors.toList())
        );

        RENDERABLE_TEXT_CACHE.put(string, renderableText);

        return renderableText;
    }

    public static void reload() {
        CACHED_TEXT.clear();
        RENDERABLE_TEXT_CACHE.clear();

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
        if (string == null || string.isEmpty()) return 0;

        RenderableText renderableText = getRenderableText(string);
        return renderableText.getWidth();
    }

    public static int getTextHeight(String string) {
        if (string == null || string.isEmpty()) return 8;

        RenderableText renderableText = getRenderableText(string);
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
                RANDOM_CHARS_PALLETTE.charAt(fontRandom.nextInt(RANDOM_CHARS_PALLETTE.length()))
        );

        while (glyph == null || glyph.getHeight() != height || glyph.getWidth() != width) {
            glyph = getGlyph(
                    RANDOM_CHARS_PALLETTE.charAt(fontRandom.nextInt(RANDOM_CHARS_PALLETTE.length()))
            );
        }

        return glyph;
    }
}
