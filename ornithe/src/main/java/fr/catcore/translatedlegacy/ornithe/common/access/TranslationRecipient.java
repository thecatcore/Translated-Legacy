package fr.catcore.translatedlegacy.ornithe.common.access;

public interface TranslationRecipient {
    void setLanguage(String language);
    String getLanguage();
    boolean pretranslate();
}
