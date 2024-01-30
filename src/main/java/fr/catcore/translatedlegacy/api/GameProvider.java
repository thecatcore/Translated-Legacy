package fr.catcore.translatedlegacy.api;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface GameProvider {
    boolean anaglyph3d();

    public static GameProvider getInstance() {
        throw new NotImplementedException();
    }
}
