package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.font.api.Glyph;
import fr.catcore.translatedlegacy.font.api.GlyphProvider;
import fr.catcore.translatedlegacy.font.renderable.Renderable;
import fr.catcore.translatedlegacy.font.renderable.TextTexture;
import fr.catcore.translatedlegacy.util.NativeImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Text {
    private final List<CharInfo> charInfos = new ArrayList<>();
    private final Style style;

    public Text(Style style) {
        this.style = style;
    }

    public void add(List<CharInfo> infos) {
        charInfos.addAll(infos);
    }

    public int getWidth() {
        return charInfos.stream().mapToInt(CharInfo::getWidth).sum();
    }

    public int getHeight() {
        return charInfos.stream().mapToInt(CharInfo::getHeight).max().orElse(0);
    }

    public NativeImage createImage() {
        int width = getWidth();
        int height = getHeight();

        NativeImage image = new NativeImage(width, height, false);

        int currentX = 0;

        for (CharInfo charInfo : charInfos) {
            Glyph glyph = charInfo.glyph;
            GlyphProvider provider = glyph.getProvider();

            provider.upload(glyph, image, currentX, 0);

            currentX += glyph.getFullWidth();
        }

        return image;
    }

    public List<Renderable> createRenderable() {
        TextTexture textTexture = new TextTexture(createImage());
        Renderable renderable = new Renderable(textTexture, style);

        List<Renderable> renderables = new ArrayList<>();
        renderables.add(renderable);
        return renderables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equals(charInfos, text.charInfos) && Objects.equals(style, text.style);
    }

    @Override
    public int hashCode() {
        return charInfos.stream().map(CharInfo::hashCode).reduce(0, Integer::sum) + (style != null ? style.hashCode() : 0);
    }

    public static class CharInfo {
        private final Glyph glyph;

        public CharInfo(Glyph glyph) {
            this.glyph = glyph;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CharInfo)) return false;

            CharInfo info = (CharInfo) obj;

            if (!Objects.equals(info.glyph.getId(), this.glyph.getId())) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return glyph.getId().hashCode();
        }

        public int getWidth() {
            return glyph.getFullWidth();
        }

        public int getHeight() {
            return glyph.getHeight();
        }
    }
}
