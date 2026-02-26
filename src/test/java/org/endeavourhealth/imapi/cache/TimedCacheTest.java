package org.endeavourhealth.imapi.cache;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TimedCacheTest {

    @Test
    void putAndGet() {
        try (TimedCache<String, String> cache = new TimedCache<>("test", 10, 10, 10)) {
            cache.put("key1", "value1");
            assertThat(cache.get("key1")).isEqualTo("value1");
        }
    }

    @Test
    void getNonExistent() {
        try (TimedCache<String, String> cache = new TimedCache<>("test", 10, 10, 10)) {
            assertThat(cache.get("key1")).isNull();
        }
    }

    @Test
    void remove() {
        try (TimedCache<String, String> cache = new TimedCache<>("test", 10, 10, 10)) {
            cache.put("key1", "value1");
            cache.remove("key1");
            assertThat(cache.get("key1")).isNull();
        }
    }

    @Test
    void size() {
        try (TimedCache<String, String> cache = new TimedCache<>("test", 10, 10, 10)) {
            cache.put("key1", "value1");
            cache.put("key2", "value2");
            assertThat(cache.size()).isEqualTo(2);
        }
    }

    @Test
    void maxItems() {
        try (TimedCache<String, String> cache = new TimedCache<>("test", 10, 10, 2)) {
            cache.put("key1", "value1");
            cache.put("key2", "value2");
            cache.put("key3", "value3");
            assertThat(cache.size()).isEqualTo(2);
            assertThat(cache.get("key1")).isNull();
            assertThat(cache.get("key2")).isEqualTo("value2");
            assertThat(cache.get("key3")).isEqualTo("value3");
        }
    }

    @Test
    void ttlEviction() throws InterruptedException {
        // TTL = 1 second, cleanup interval = 10 seconds (won't trigger automatically during test)
        try (TimedCache<String, String> cache = new TimedCache<>("test", 1, 10, 10)) {
            cache.put("key1", "value1");
            assertThat(cache.get("key1")).isEqualTo("value1");

            Thread.sleep(1100); // Wait for TTL to expire
            cache.cleanup();

            assertThat(cache.get("key1")).isNull();
            assertThat(cache.size()).isZero();
        }
    }

    @Test
    void lastAccessedUpdate() throws InterruptedException {
        // TTL = 2 seconds
        try (TimedCache<String, String> cache = new TimedCache<>("test", 2, 10, 10)) {
            cache.put("key1", "value1");

            Thread.sleep(1100);
            assertThat(cache.get("key1")).isEqualTo("value1"); // This updates lastAccessed to ~1100ms

            Thread.sleep(1100);
            cache.cleanup(); // Total time ~2200ms. Last accessed ~1100ms. 2200 < 1100 + 2000. Not evicted.

            assertThat(cache.size()).isEqualTo(1);

            Thread.sleep(1100);
            cache.cleanup(); // Total time ~3300ms. Last accessed ~1100ms. 3300 > 1100 + 2000. Should be evicted.
            assertThat(cache.get("key1")).isNull();
        }
    }

    @Test
    void automaticEviction() throws InterruptedException {
        // TTL = 1s, interval = 1s
        try (TimedCache<String, String> cache = new TimedCache<>("test", 1, 1, 10)) {
            cache.put("key1", "value1");
            assertThat(cache.get("key1")).isEqualTo("value1");

            // Wait for 3 seconds to be sure the thread has run and evicted
            Thread.sleep(3000);

            assertThat(cache.get("key1")).isNull();
            assertThat(cache.size()).isZero();
        }
    }
}
