package bg.sofia.uni.fmi.mjt.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RandomReplacementCache<K, V> implements Cache<K, V> {
    private final long capacity;
    private long currentCount;

    private Map<K, V> cache;

    private long hitsCount;
    private long missesCount;

    public RandomReplacementCache() {
        capacity = Long.MAX_VALUE;
        currentCount = 0;
        cache = new HashMap<>();

        hitsCount = 0;
        missesCount = 0;
    }

    public RandomReplacementCache(long capacity) {
        this.capacity = capacity;
        currentCount = 0;
        cache = new HashMap<>();

        hitsCount = 0;
        missesCount = 0;
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);

        if (Objects.isNull(value)) {
            missesCount++;
        } else {
            hitsCount++;
        }

        return value;
    }

    @Override
    public void set(K key, V value) {
        if (Objects.isNull(key) || Objects.isNull(value)) {
            return;
        }

        // we have the entry
        if (!Objects.isNull(cache.get(key))) {
            cache.put(key, value);
            hitsCount++;
            return;
        }

        // if the entry isn't present
        if (currentCount == capacity) {
            removeRandomEntry();
        }
        cache.put(key, value);
        currentCount++;
    }

    private void removeRandomEntry() {
        for (K key : cache.keySet()) {
            cache.remove(key);
            break;
        }

        currentCount--;
    }

    @Override
    public boolean remove(Object key) {
        currentCount--;
        if (Objects.isNull(cache.remove(key))) {
            return false;
        }

        return true;
    }

    @Override
    public long size() {
        return currentCount;
    }

    @Override
    public void clear() {
        cache.clear();
        hitsCount = 0;
        missesCount = 0;
        currentCount = 0;
    }

    @Override
    public double getHitRate() {
        if (hitsCount + missesCount == 0) {
            return 0;
        }
        return (double) hitsCount / (hitsCount + missesCount);
    }

    @Override
    public long getUsesCount(Object key) {
        throw new UnsupportedOperationException("RandomReplacementCache does not support uses counts.");
    }
}
