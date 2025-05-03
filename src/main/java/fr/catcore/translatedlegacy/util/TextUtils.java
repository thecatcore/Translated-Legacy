package fr.catcore.translatedlegacy.util;

public class TextUtils {
    public static int isCharacterAllowed(int character) {
        return character != 167 && character >= ' ' && character != 127 ? 0 : -1;
    }
}
