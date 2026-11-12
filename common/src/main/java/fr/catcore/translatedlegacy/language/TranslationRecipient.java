package fr.catcore.translatedlegacy.language;

public interface TranslationRecipient {
    void setLanguage(String language);
    String getLanguage();
    boolean pretranslate();
}
