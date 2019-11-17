package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.enums.EvictionPolicy;

public class CacheFactory {
    private static final int DEFAULT_CAPACITY = 10000;

    /**
     * Constructs a new Cache<K, V> with maximum capacity of 10_000 items and the specified eviction policy
     */
    public static <K, V> Cache<K, V> getInstance(EvictionPolicy policy) {
        if (policy.equals(EvictionPolicy.RANDOM_REPLACEMENT)) {
            return new RandomReplacementCache<>(DEFAULT_CAPACITY);
        }
        return new LeastFrequentlyUsedCache<>(DEFAULT_CAPACITY);
    }

    /**
     * Constructs a new Cache<K, V> with the specified maximum capacity and eviction policy
     *
     * @throws IllegalArgumentException if the given capacity is less than or equal to zero
     */
    public static <K, V> Cache<K, V> getInstance(long capacity, EvictionPolicy policy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Cache's Capacity cannot be less than one.");
        }
        if (policy.equals(EvictionPolicy.RANDOM_REPLACEMENT)) {
            return new RandomReplacementCache<>(capacity);
        }
        return new LeastFrequentlyUsedCache<>(capacity);
    }
}
