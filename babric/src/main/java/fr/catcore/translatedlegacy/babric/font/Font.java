package fr.catcore.translatedlegacy.babric.font;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.texture.TextureManager;

public abstract class Font {

    public abstract boolean contains(char c);

    protected abstract Glyph getGlyph(char c);

    protected abstract void loadTextures(GameOptions arg, TextureManager arg1);

    protected abstract int getImagePointer(char c);

    protected abstract int getOtherPointer(char c);

    protected abstract byte getWidth(char c);
}
