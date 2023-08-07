package fr.catcore.translatedlegacy.babric.font;

import com.google.gson.JsonObject;

public enum FontType {
    bitmap(BitmapFont::new),
    legacy_unicode(LegacyUnicodeFont::new);

    private final FontBuilder builder;

    FontType(FontBuilder builder) {
        this.builder = builder;
    }

    public static Font create(JsonObject object) {
        String type = object.get("type").getAsString();

        FontType fontType = valueOf(type);

        return fontType.builder.build(object);
    }

    protected interface FontBuilder {
        Font build(JsonObject object);
    }
}
