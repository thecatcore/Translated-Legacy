package fr.catcore.translatedlegacy.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Text {
    private final List<CharInfo> charInfos = new ArrayList<>();

    public static class CharInfo {
        private final Glyph glyph;
        private final Style style;

        public CharInfo(Glyph glyph, Style style) {
            this.glyph = glyph;
            this.style = style;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CharInfo)) return false;

            CharInfo info = (CharInfo) obj;

            if (!Objects.equals(info.glyph.getId(), this.glyph.getId())) return false;

            return Objects.equals(info.style.toString(), this.style.toString());
        }
    }
}
