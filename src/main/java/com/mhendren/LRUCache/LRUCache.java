package com.mhendren.LRUCache;

import java.util.*;

/**
 * The LRUCache will create a cache of things that can be quickly looked up, but has an expiration mechanism
 * which will prevent it from using up all the memory in the system by continually adding hings until
 * eventually everything is in the cache.
 */
public class LRUCache<KType, VType> {
    private class KeyVal {
        KType key;
        VType val;
    }
    Map<KType, DoubleLinkedListNode<KeyVal>> map = new HashMap<KType, DoubleLinkedListNode<KeyVal>>();
    DoubleLinkedList<KeyVal> list = new DoubleLinkedList<KeyVal>();
    private int capacity;

    LRUCache() {
        this.capacity = 10;
    }

    LRUCache(int capacity) {
        this.capacity = capacity;
    }

    private void moveNodeToHead(DoubleLinkedListNode<KeyVal> node) {
        if(node.prev != null) { node.prev.next = node.next; }
        if(node.next != null) { node.next.prev = node.prev; } else { list.tail = node.prev; }
        node.next = list.head;
        node.prev = null;
        list.head = node;
        if(list.tail == null) list.tail = node;
    }

    private void removeTail() {
        DoubleLinkedListNode<KeyVal> end = list.tail;
        map.remove(end.data.key);
        if(end.prev == null) { list.head = null; list.tail = null; }
        else {
            list.tail = end.prev;
            end.prev.next = null;
        }
        list.nodeCount--;
    }

    /**
     * O(1)
     * Put an element into the LRU Cache.
     *
     * If the element was already in the cache, move its position in the access list to the top of the list. This will
     * update the value if it was already present.
     *
     * If the element was not already in the present, and adding this element would make the cache too large, then the
     * least recently used element is removed from the cache.
     *
     * @param key The key that is used to access the content.
     * @param value The content that is to be accessed by the key
     */
    public synchronized void put(KType key, VType value) {
        if (map.containsKey(key)) {
            DoubleLinkedListNode<KeyVal> node = map.get(key);
            if (node != null) {
                node.data.val = value;
                moveNodeToHead(node);
            }
        } else {
            if ((list.size() + 1) > capacity) {
                removeTail();
            }
            KeyVal keyVal = new KeyVal();
            keyVal.key = key;
            keyVal.val = value;
            DoubleLinkedListNode<KeyVal> node = new DoubleLinkedListNode<KeyVal>(keyVal);
            node.next = list.head;
            if(list.head != null) list.head.prev = node; else { list.tail = node; }
            list.head = node;
            list.nodeCount++;
            map.put(key, node);
        }
    }

    /**
     * Get the contents associated with the key. If the key is located in the cache, then the position of this
     * element will be moved to the top (as it is the furthest away from being least recently used).
     * @param key The key to look for in the cache.
     * @return The value associated with the key (null if the key is not presently in the cache).
     */
    public synchronized VType get(KType key) {
        if (map.containsKey(key)) {
            DoubleLinkedListNode<KeyVal> node = map.get(key);
            if (node != null) {
                moveNodeToHead(node);
                return node.data.val;
            }
        }
        return null;
    }
}
