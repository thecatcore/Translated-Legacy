package fr.catcore.translatedlegacy.font;

public class Style {
    public final Color3f color;
    public final Character character;
    public final Boolean random, bold, strikethrough, underline, italic, reset;

    public Style(Color3f color, Character character, Boolean random, Boolean bold, Boolean strikethrough, Boolean underline, Boolean italic, Boolean reset) {
        this.color = color;
        this.character = character;
        this.random = random;
        this.bold = bold;
        this.strikethrough = strikethrough;
        this.underline = underline;
        this.italic = italic;
        this.reset = reset;
    }
}