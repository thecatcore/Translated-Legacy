package fr.catcore.translatedlegacy.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AccessWeightedMap<K, V> extends HashMap<K, V> {
    private final Map<K, Integer> weights = new HashMap<>();
    private final int threshold;
    private final int toKeep;
    private final Consumer<V> removeCallback;
    public AccessWeightedMap(int threshold, Consumer<V> removeCallback) {
        this.threshold = threshold;
        this.toKeep = threshold / 4;
        this.removeCallback = removeCallback;
    }

    @Override
    public V put(K key, V value) {
        if (this.size() >= this.threshold) {
            System.out.println("Cleaning cache of less used values...");

            List<K> sortedList = weights.entrySet().stream().sorted(Entry.comparingByValue()).map(Entry::getKey).collect(Collectors.toList());

            for (int i = 0; i < sortedList.size(); i++) {
                if (i >= this.toKeep) {
                    K k = sortedList.get(i);

                    if (this.containsKey(k)) {
                        this.remove(k);
                    }
                }
            }
        }

        weights.put(key, 0);
        return super.put(key, value);
    }

    @Override
    public V get(Object key) {
        V value = super.get(key);

        if (value != null) {
            weights.replace((K) key, weights.get(key) + 1);
        }

        return value;
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
        weights.clear();
    }
}
