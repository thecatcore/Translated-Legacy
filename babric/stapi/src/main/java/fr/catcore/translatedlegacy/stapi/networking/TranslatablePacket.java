package fr.catcore.translatedlegacy.stapi.networking;


public interface TranslatablePacket {
    void setTexts(String... text);
    boolean requiresPacket();
    void writeCustomPacket(TranslationPacket packet);
    default void makeTranslatable() {}
    default void preTranslate() {}
}
