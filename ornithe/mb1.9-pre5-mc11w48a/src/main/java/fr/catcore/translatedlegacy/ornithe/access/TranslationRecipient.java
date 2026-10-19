package fr.catcore.translatedlegacy.ornithe.access;

public interface TranslationRecipient {
    void setLanguage(String language);
    String getLanguage();
    boolean pretranslate();
}
