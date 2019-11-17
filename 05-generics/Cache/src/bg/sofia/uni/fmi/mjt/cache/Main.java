package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.enums.EvictionPolicy;

public class Main {
    private static final long TEST_CAP = 1;

    public static void main(String[] args) {
        Cache<Integer, String> cache = CacheFactory.getInstance(TEST_CAP, EvictionPolicy.LEAST_FREQUENTLY_USED);
        Cache<Integer, String> rCache = CacheFactory.getInstance(TEST_CAP, EvictionPolicy.RANDOM_REPLACEMENT);

        rCache.set(1, "1");
        rCache.set(1, "2");
        rCache.get(2);

        cache.set(1, "one");
        cache.set(2, "two");
//        cache.set(1, "oneee");
//        cache.get(1);
//        cache.get(10);
//        cache.set(4, "four");
//        cache.set(5, "five");

        double rate = cache.getHitRate();
        double rRate = rCache.getHitRate();
        System.out.println("WOW");

    }
}
