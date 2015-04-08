package com.mhendren.LRUCache;

import java.util.*;

/**
 * The LRUCache will create a cache of things that can be quickly looked up, but has an expiration mechanism
 * which will prevent it from using up all the memory in the system by continually adding hings until
 * eventually everything is in the cache.
 */
public class LRUCache<KType, VType> {
    Map<KType, VType> map = new HashMap<KType, VType>();
    List<KType> list = new LinkedList<KType>();
    private int capacity;

    LRUCache() {
        this.capacity = 10;
    }

    LRUCache(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(KType key, VType value) {
        if (map.containsKey(key)) {
            list.remove(key);
        }
        if (list.size() + 1 > capacity) {
            map.remove(list.remove(list.size() - 1));
        }
        list.add(0, key);
        map.put(key, value);
    }

    public synchronized VType get(KType key) {
        if (map.containsKey(key)) {
            list.remove(key);
            list.add(0, key);
            return map.get(key);
        }
        return null;
    }
}
