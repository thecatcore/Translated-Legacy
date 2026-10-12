package fr.catcore.translatedlegacy.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A capped, least-recently-used cache. Access order is tracked natively by {@link LinkedHashMap}
 * (O(1) per get/put), so eviction never needs to sort the whole map like a weight-based scheme would.
 */
public class AccessWeightedMap<K, V> extends LinkedHashMap<K, V> {
    private final int threshold;
    private final Consumer<V> removeCallback;

    public AccessWeightedMap(int threshold, Consumer<V> removeCallback) {
        super(16, 0.75f, true);
        this.threshold = threshold;
        this.removeCallback = removeCallback;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if (size() > threshold) {
            removeCallback.accept(eldest.getValue());
            return true;
        }

        return false;
    }

    @Override
    public V remove(Object key) {
        V value = super.remove(key);
        removeCallback.accept(value);
        return value;
    }

    @Override
    public void clear() {
        for (V value : this.values()) {
            removeCallback.accept(value);
        }

        super.clear();
    }
}