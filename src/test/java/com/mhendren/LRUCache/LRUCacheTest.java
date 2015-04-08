package com.mhendren.LRUCache;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class LRUCacheTest {

    @Test
    public void testPut() throws Exception {
        LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>();
        lruCache.put(5, "Hello");
        assertThat(lruCache.map.get(5), is(equalTo("Hello")));
        assertThat(lruCache.list.contains(5), is(true));
    }

    @Test
    public void testGet() throws Exception {
        LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>();
        lruCache.put(5, "Hello");
        assertThat(lruCache.get(5), is(equalTo("Hello")));
    }

    @Test
    public void testOverrunCapacity() throws Exception {
        LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>(2);
        lruCache.put(1, "A");
        lruCache.put(2, "B");
        lruCache.put(3, "C");
        assertThat(lruCache.get(1), is(nullValue()));
    }

    @Test
    public void testRefreshCapabilityOnGet() throws Exception {
        LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>(2);
        lruCache.put(1, "A");
        lruCache.put(2, "B");
        String aValue = lruCache.get(1); // Don't care, just want to refresh a as used.
        lruCache.put(3, "C");
        assertThat(lruCache.get(1), is(equalTo("A")));
        assertThat(lruCache.get(2), is(nullValue()));
    }

    @Test
    public void testRefreshCapabilityOnPut() throws Exception {
        LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>(2);
        lruCache.put(1, "A");
        lruCache.put(2, "B");
        lruCache.put(1, "D"); // Don't care, just want to refresh a as used.
        lruCache.put(3, "C");
        assertThat(lruCache.get(1), is(equalTo("D")));
        assertThat(lruCache.get(2), is(nullValue()));
    }
}