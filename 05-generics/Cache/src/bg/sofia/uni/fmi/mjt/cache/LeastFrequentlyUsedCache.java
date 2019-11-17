package bg.sofia.uni.fmi.mjt.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class LeastFrequentlyUsedCache<K, V> implements Cache<K, V> {
    private final long capacity;
    private long currentCount;

    private Map<K, V> cache;
    private TreeMap<K, Long> usagesCount;
    private long hitsCount;
    private long queriesCount;

    public LeastFrequentlyUsedCache() {
        capacity = Long.MAX_VALUE;
        currentCount = 0;

        cache = new HashMap<>();
        usagesCount = new TreeMap<>();

        hitsCount = 0;
        queriesCount = 0;
    }

    public LeastFrequentlyUsedCache(long capacity) {
        this.capacity = capacity;
        currentCount = 0;

        cache = new HashMap<>();
        usagesCount = new TreeMap<>();

        hitsCount = 0;
        queriesCount = 0;
    }

    @Override
    public V get(K key) {
        queriesCount++;
        V value = cache.get(key);

        if (!Objects.isNull(value)) {
            hitsCount++;
            addOneUsage(key);
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
            addOneUsage(key);
            hitsCount++;
            queriesCount++;
            return;
        }

        // if the entry isn't present
        if (currentCount == capacity) {
            removeLeastUsedEntry();
        }
        cache.put(key, value);
        addOneUsage(key);
        currentCount++;
    }

    private void removeLeastUsedEntry() {
        K leastUsed = usagesCount.lastKey();
        usagesCount.remove(leastUsed);
        cache.remove(leastUsed);
        currentCount--;
    }

    private void addOneUsage(K key) {
        Long count = 1L;
        if (!Objects.isNull(usagesCount.get(key))) {
            count = usagesCount.get(key) + 1;
        }
        usagesCount.put(key, count);
    }

    @Override
    public boolean remove(K key) {
        if (Objects.isNull(usagesCount.remove(key))) {
            return false;
        }

        cache.remove(key);
        currentCount--;
        return true;
    }

    @Override
    public long size() {
        return currentCount;
    }

    @Override
    public void clear() {
        cache.clear();
        usagesCount.clear();
        currentCount = 0;

        hitsCount = 0;
        queriesCount = 0;
    }

    @Override
    public double getHitRate() {
        if (queriesCount == 0) {
            return 0;
        }
        return (double) hitsCount / queriesCount;
    }

    @Override
    public long getUsesCount(K key) {
        if (Objects.isNull(key)) {
            return 0;
        }

        return usagesCount.getOrDefault(key, 0L);
    }
}
