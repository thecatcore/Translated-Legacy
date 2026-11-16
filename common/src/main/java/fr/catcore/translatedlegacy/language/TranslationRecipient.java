package fr.catcore.translatedlegacy.language;

public interface TranslationRecipient {
    void tl$setLanguage(String language);
    String tl$getLanguage();
    boolean tl$pretranslate();
}
