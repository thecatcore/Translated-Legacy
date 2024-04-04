package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.font.renderable.Renderable;

import java.util.ArrayList;
import java.util.List;

public class MultiText extends Text {
    private final List<Text> texts = new ArrayList<>();

    public MultiText(Style style) {
        super(style);
    }

    public void addText(List<Text> texts) {
        this.texts.addAll(texts);
    }

    @Override
    public void add(List<CharInfo> infos) {
        Text text = new Text(null);
        text.add(infos);
        this.texts.add(text);
    }

    @Override
    public int getWidth() {
        return texts.stream().mapToInt(Text::getWidth).sum();
    }

    @Override
    public int getHeight() {
        return texts.stream().mapToInt(Text::getHeight).max().orElse(0);
    }

    @Override
    public List<Renderable> createRenderable() {
        List<Renderable> renderables = new ArrayList<>();

        texts.stream().map(Text::createRenderable).forEach(renderables::addAll);

        return renderables;
    }

    @Override
    public int hashCode() {
        return texts.stream().mapToInt(Text::hashCode).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiText multiText = (MultiText) o;
        return texts.equals(multiText.texts);
    }
}
