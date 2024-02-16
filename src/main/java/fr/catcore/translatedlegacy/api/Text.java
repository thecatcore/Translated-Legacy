package fr.catcore.translatedlegacy.api;

import fr.catcore.translatedlegacy.util.NativeImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Text {
    private final List<CharInfo> charInfos = new ArrayList<>();

    public void add(List<CharInfo> infos) {
        charInfos.addAll(infos);
    }

    public int getWidth() {
        return charInfos.stream().map(CharInfo::getWidth).reduce(0, Integer::sum);
    }

    public int getHeight() {
        return charInfos.stream().map(CharInfo::getHeight).reduce(0, Integer::max);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equals(charInfos, text.charInfos);
    }

    @Override
    public int hashCode() {
        return charInfos.stream().map(CharInfo::hashCode).reduce(0, Integer::sum);
    }

    public static class CharInfo {
        private final Glyph glyph;
//        private final Style style;

        public CharInfo(Glyph glyph/*, Style style*/) {
            this.glyph = glyph;
//            this.style = style;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CharInfo)) return false;

            CharInfo info = (CharInfo) obj;

            if (!Objects.equals(info.glyph.getId(), this.glyph.getId())) return false;

//            return Objects.equals(info.style.toString(), this.style.toString());
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
